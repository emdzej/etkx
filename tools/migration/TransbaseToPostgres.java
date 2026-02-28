import java.sql.*;
import java.util.*;
import java.io.*;

/**
 * Direct Transbase → PostgreSQL migration
 * 
 * Usage:
 *   java -cp "tbjdbc.jar:postgresql.jar:." TransbaseToPostgres [pg_host] [pg_db] [pg_user] [pg_pass]
 * 
 * Defaults:
 *   pg_host = localhost:5432
 *   pg_db = etk
 *   pg_user = postgres
 *   pg_pass = postgres
 * 
 * Prerequisites:
 *   - PostgreSQL database must exist (CREATE DATABASE etk;)
 *   - PostgreSQL JDBC driver (postgresql-XX.jar)
 *   - Transbase JDBC driver (tbjdbc.jar)
 */
public class TransbaseToPostgres {
    // Transbase connection
    static final String TB_URL = "jdbc:transbase://192.168.101.150:2024/etk_publ";
    static final String TB_USER = "tbadmin";
    static final String TB_PASS = "altabe";
    
    // Batch size for inserts
    static final int BATCH_SIZE = 5000;
    
    public static void main(String[] args) throws Exception {
        // PostgreSQL connection params
        String pgHost = args.length > 0 ? args[0] : "localhost:5432";
        String pgDb = args.length > 1 ? args[1] : "etk";
        String pgUser = args.length > 2 ? args[2] : "postgres";
        String pgPass = args.length > 3 ? args[3] : "postgres";
        String pgSchema = args.length > 4 ? args[4] : "public";
        
        String pgUrl = "jdbc:postgresql://" + pgHost + "/" + pgDb;
        
        System.out.println("=== Transbase → PostgreSQL Migration ===\n");
        System.out.println("Source: " + TB_URL);
        System.out.println("Target: " + pgUrl + " (schema: " + pgSchema + ")\n");
        
        // Load drivers
        Class.forName("transbase.jdbc.Driver");
        Class.forName("org.postgresql.Driver");
        
        // Connect to Transbase
        Connection tbConn = DriverManager.getConnection(TB_URL, TB_USER, TB_PASS);
        System.out.println("✓ Connected to Transbase");
        
        // Connect to PostgreSQL
        Connection pgConn = DriverManager.getConnection(pgUrl, pgUser, pgPass);
        pgConn.setAutoCommit(false);
        System.out.println("✓ Connected to PostgreSQL\n");
        
        // Create schema if not exists
        Statement schemaStmt = pgConn.createStatement();
        schemaStmt.execute("CREATE SCHEMA IF NOT EXISTS " + pgSchema);
        schemaStmt.execute("SET search_path TO " + pgSchema);
        schemaStmt.close();
        pgConn.commit();
        
        // Get table list from Transbase
        DatabaseMetaData tbMeta = tbConn.getMetaData();
        ResultSet tablesRs = tbMeta.getTables(null, null, "%", new String[]{"TABLE"});
        List<String> tables = new ArrayList<>();
        while (tablesRs.next()) {
            tables.add(tablesRs.getString("TABLE_NAME"));
        }
        tablesRs.close();
        
        System.out.println("Tables to migrate: " + tables.size() + "\n");
        
        // Track statistics
        long totalRows = 0;
        long totalBlobs = 0;
        long startTime = System.currentTimeMillis();
        List<String> failedTables = new ArrayList<>();
        
        // Migrate each table
        for (int i = 0; i < tables.size(); i++) {
            String table = tables.get(i);
            String pgTable = table.toLowerCase(); // PostgreSQL prefers lowercase
            
            System.out.printf("[%d/%d] %s → %s... ", i + 1, tables.size(), table, pgTable);
            System.out.flush();
            
            try {
                int[] result = migrateTable(tbConn, pgConn, tbMeta, table, pgTable);
                totalRows += result[0];
                totalBlobs += result[1];
                
                System.out.printf("%,d rows", result[0]);
                if (result[1] > 0) System.out.printf(" (%,d blobs)", result[1]);
                System.out.println();
            } catch (Exception e) {
                System.out.println("FAILED: " + e.getMessage());
                failedTables.add(table + ": " + e.getMessage());
                pgConn.rollback();
            }
        }
        
        // Create indexes
        System.out.println("\nCreating indexes...");
        createIndexes(pgConn, pgSchema);
        
        // Analyze tables for query optimization
        System.out.println("\nAnalyzing tables...");
        Statement analyzeStmt = pgConn.createStatement();
        analyzeStmt.execute("ANALYZE");
        analyzeStmt.close();
        pgConn.commit();
        
        // Close connections
        tbConn.close();
        pgConn.close();
        
        // Summary
        long elapsed = System.currentTimeMillis() - startTime;
        System.out.println("\n=== Migration Complete ===");
        System.out.printf("Tables: %d (failed: %d)\n", tables.size(), failedTables.size());
        System.out.printf("Rows: %,d\n", totalRows);
        System.out.printf("BLOBs: %,d\n", totalBlobs);
        System.out.printf("Time: %.1f minutes\n", elapsed / 60000.0);
        System.out.println("Target: " + pgUrl);
        
        if (!failedTables.isEmpty()) {
            System.out.println("\nFailed tables:");
            for (String f : failedTables) {
                System.out.println("  - " + f);
            }
        }
    }
    
    static int[] migrateTable(Connection tbConn, Connection pgConn, 
            DatabaseMetaData tbMeta, String tbTable, String pgTable) throws Exception {
        
        // Get column info from Transbase
        ResultSet colsRs = tbMeta.getColumns(null, null, tbTable, "%");
        List<String> colNames = new ArrayList<>();
        List<String> colTypes = new ArrayList<>();
        List<Integer> colSizes = new ArrayList<>();
        
        while (colsRs.next()) {
            colNames.add(colsRs.getString("COLUMN_NAME").toLowerCase());
            colTypes.add(colsRs.getString("TYPE_NAME").toUpperCase());
            colSizes.add(colsRs.getInt("COLUMN_SIZE"));
        }
        colsRs.close();
        
        if (colNames.isEmpty()) {
            throw new Exception("No columns found");
        }
        
        // Build CREATE TABLE statement for PostgreSQL
        StringBuilder createSql = new StringBuilder();
        createSql.append("CREATE TABLE IF NOT EXISTS ").append(pgTable).append(" (\n");
        for (int c = 0; c < colNames.size(); c++) {
            if (c > 0) createSql.append(",\n");
            createSql.append("  ").append(colNames.get(c)).append(" ");
            createSql.append(mapTypeToPostgres(colTypes.get(c), colSizes.get(c)));
        }
        createSql.append("\n)");
        
        // Drop and create table
        Statement ddlStmt = pgConn.createStatement();
        ddlStmt.execute("DROP TABLE IF EXISTS " + pgTable + " CASCADE");
        ddlStmt.execute(createSql.toString());
        ddlStmt.close();
        pgConn.commit();
        
        // Build INSERT statement
        StringBuilder insertSql = new StringBuilder();
        insertSql.append("INSERT INTO ").append(pgTable).append(" (");
        for (int c = 0; c < colNames.size(); c++) {
            if (c > 0) insertSql.append(", ");
            insertSql.append(colNames.get(c));
        }
        insertSql.append(") VALUES (");
        for (int c = 0; c < colNames.size(); c++) {
            if (c > 0) insertSql.append(", ");
            insertSql.append("?");
        }
        insertSql.append(")");
        
        PreparedStatement insertStmt = pgConn.prepareStatement(insertSql.toString());
        
        // Select data from Transbase
        Statement selectStmt = tbConn.createStatement();
        selectStmt.setFetchSize(BATCH_SIZE);
        ResultSet dataRs = selectStmt.executeQuery("SELECT * FROM " + tbTable);
        
        int rowCount = 0;
        int blobCount = 0;
        
        while (dataRs.next()) {
            for (int c = 0; c < colNames.size(); c++) {
                String type = colTypes.get(c);
                int paramIdx = c + 1;
                
                if (type.contains("BLOB") || type.contains("BINARY")) {
                    byte[] blob = dataRs.getBytes(paramIdx);
                    if (blob != null) {
                        insertStmt.setBytes(paramIdx, blob);
                        blobCount++;
                    } else {
                        insertStmt.setNull(paramIdx, Types.BINARY);
                    }
                } else if (type.contains("BOOL")) {
                    boolean val = dataRs.getBoolean(paramIdx);
                    if (dataRs.wasNull()) {
                        insertStmt.setNull(paramIdx, Types.BOOLEAN);
                    } else {
                        insertStmt.setBoolean(paramIdx, val);
                    }
                } else if (type.equals("TINYINT") || type.equals("SMALLINT")) {
                    short val = dataRs.getShort(paramIdx);
                    if (dataRs.wasNull()) {
                        insertStmt.setNull(paramIdx, Types.SMALLINT);
                    } else {
                        insertStmt.setShort(paramIdx, val);
                    }
                } else if (type.contains("INT")) {
                    long val = dataRs.getLong(paramIdx);
                    if (dataRs.wasNull()) {
                        insertStmt.setNull(paramIdx, Types.BIGINT);
                    } else {
                        insertStmt.setLong(paramIdx, val);
                    }
                } else if (type.contains("FLOAT") || type.contains("DOUBLE") || 
                           type.contains("NUMERIC") || type.contains("DECIMAL")) {
                    double val = dataRs.getDouble(paramIdx);
                    if (dataRs.wasNull()) {
                        insertStmt.setNull(paramIdx, Types.DOUBLE);
                    } else {
                        insertStmt.setDouble(paramIdx, val);
                    }
                } else if (type.contains("DATE")) {
                    Date val = dataRs.getDate(paramIdx);
                    if (val != null) {
                        insertStmt.setDate(paramIdx, val);
                    } else {
                        insertStmt.setNull(paramIdx, Types.DATE);
                    }
                } else if (type.contains("TIME")) {
                    Timestamp val = dataRs.getTimestamp(paramIdx);
                    if (val != null) {
                        insertStmt.setTimestamp(paramIdx, val);
                    } else {
                        insertStmt.setNull(paramIdx, Types.TIMESTAMP);
                    }
                } else {
                    // String types
                    String val = dataRs.getString(paramIdx);
                    insertStmt.setString(paramIdx, val);
                }
            }
            
            insertStmt.addBatch();
            rowCount++;
            
            // Execute batch periodically
            if (rowCount % BATCH_SIZE == 0) {
                insertStmt.executeBatch();
                pgConn.commit();
            }
        }
        
        // Execute remaining batch
        insertStmt.executeBatch();
        pgConn.commit();
        
        dataRs.close();
        selectStmt.close();
        insertStmt.close();
        
        return new int[] { rowCount, blobCount };
    }
    
    static String mapTypeToPostgres(String tbType, int size) {
        // Boolean
        if (tbType.contains("BOOL")) return "BOOLEAN";
        
        // Integer types
        if (tbType.equals("TINYINT")) return "SMALLINT";
        if (tbType.equals("SMALLINT")) return "SMALLINT";
        if (tbType.equals("INTEGER") || tbType.equals("INT")) return "INTEGER";
        if (tbType.contains("BIGINT")) return "BIGINT";
        
        // Floating point
        if (tbType.contains("FLOAT")) return "REAL";
        if (tbType.contains("DOUBLE")) return "DOUBLE PRECISION";
        if (tbType.contains("NUMERIC") || tbType.contains("DECIMAL")) {
            return "NUMERIC";
        }
        
        // Binary/BLOB
        if (tbType.contains("BLOB") || tbType.contains("BINARY")) return "BYTEA";
        
        // Date/Time
        if (tbType.equals("DATE")) return "DATE";
        if (tbType.contains("TIMESTAMP")) return "TIMESTAMP";
        if (tbType.equals("TIME")) return "TIME";
        
        // Character types
        if (tbType.contains("CHAR")) {
            if (size > 0 && size <= 10485760) {
                return "VARCHAR(" + size + ")";
            }
            return "TEXT";
        }
        if (tbType.contains("CLOB") || tbType.contains("TEXT")) return "TEXT";
        
        // Default to TEXT
        return "TEXT";
    }
    
    static void createIndexes(Connection pgConn, String schema) throws Exception {
        Statement stmt = pgConn.createStatement();
        
        // Define indexes for common queries
        String[][] indexes = {
            // Parts
            {"w_teil", "teil_sachnr"},
            {"w_teil", "teil_sachnrbasis"},
            
            // Diagrams
            {"w_bildtaf", "bildtaf_btnr"},
            {"w_bildtaf", "bildtaf_hgfg"},
            
            // Diagram lines (parts in diagrams)
            {"w_btzeilen", "btzeilen_btnr"},
            {"w_btzeilen", "btzeilen_sachnr"},
            {"w_btzeilen", "btzeilen_lfdnr"},
            
            // Graphics
            {"w_grafik", "grafik_grafikid"},
            {"w_grafik_hs", "grafik_hs_grafikid"},
            
            // Multilingual names
            {"w_publben", "publben_textcode"},
            {"w_publben", "publben_sprache"},
            
            // Vehicle types
            {"w_fztyp", "fztyp_mospid"},
            {"w_fztyp", "fztyp_fztyp"},
            
            // Series
            {"w_baureihe", "baureihe_baureihe"},
            
            // Part supersession
            {"w_teileersetzung", "teileersetzung_sachnr_alt"},
            {"w_teileersetzung", "teileersetzung_sachnr_neu"},
            
            // Main/sub groups
            {"w_hgfg_mosp", "hgfg_mosp_mospid"},
            {"w_hgfg_mosp", "hgfg_mosp_hgfg"},
            
            // Search tables
            {"w_bildtaf_suche", "bildtaf_suche_btnr"},
            {"w_teil_marken", "teil_marken_sachnr"},
        };
        
        for (String[] idx : indexes) {
            String table = idx[0].toLowerCase();
            String column = idx[1].toLowerCase();
            String indexName = "idx_" + table + "_" + column;
            
            try {
                stmt.execute("CREATE INDEX IF NOT EXISTS " + indexName + 
                    " ON " + schema + "." + table + "(" + column + ")");
                System.out.println("  ✓ " + indexName);
            } catch (Exception e) {
                System.out.println("  ✗ " + indexName + " - " + e.getMessage());
            }
        }
        
        stmt.close();
        pgConn.commit();
    }
}
