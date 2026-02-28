import java.sql.*;
import java.util.*;

/**
 * Direct Transbase → SQLite migration (no intermediate CSV)
 */
public class TransbaseToSqlite {
    static final String TB_URL = "jdbc:transbase://192.168.101.150:2024/etk_publ";
    static final String TB_USER = "tbadmin";
    static final String TB_PASS = "altabe";
    
    public static void main(String[] args) throws Exception {
        String sqliteFile = args.length > 0 ? args[0] : "etk.sqlite";
        
        System.out.println("=== Transbase → SQLite Direct Migration ===\n");
        
        // Connect to Transbase
        Class.forName("transbase.jdbc.Driver");
        Connection tbConn = DriverManager.getConnection(TB_URL, TB_USER, TB_PASS);
        System.out.println("Connected to Transbase");
        
        // Connect to SQLite
        Class.forName("org.sqlite.JDBC");
        Connection sqliteConn = DriverManager.getConnection("jdbc:sqlite:" + sqliteFile);
        
        // Performance tuning BEFORE turning off autocommit
        Statement pragma = sqliteConn.createStatement();
        pragma.execute("PRAGMA journal_mode = OFF");
        pragma.execute("PRAGMA synchronous = OFF");
        pragma.execute("PRAGMA cache_size = 1000000");
        pragma.execute("PRAGMA temp_store = MEMORY");
        pragma.close();
        
        // Now turn off autocommit
        sqliteConn.setAutoCommit(false);
        
        System.out.println("Created SQLite: " + sqliteFile + "\n");
        
        // Get table list
        DatabaseMetaData tbMeta = tbConn.getMetaData();
        ResultSet tablesRs = tbMeta.getTables(null, null, "%", new String[]{"TABLE"});
        List<String> tables = new ArrayList<>();
        while (tablesRs.next()) {
            tables.add(tablesRs.getString("TABLE_NAME"));
        }
        tablesRs.close();
        
        System.out.println("Tables to migrate: " + tables.size() + "\n");
        
        long totalRows = 0;
        long totalBlobs = 0;
        
        for (int i = 0; i < tables.size(); i++) {
            String table = tables.get(i);
            System.out.printf("[%d/%d] %s... ", i + 1, tables.size(), table);
            System.out.flush();
            
            // Get column info
            ResultSet colsRs = tbMeta.getColumns(null, null, table, "%");
            List<String> colNames = new ArrayList<>();
            List<String> colTypes = new ArrayList<>();
            
            while (colsRs.next()) {
                colNames.add(colsRs.getString("COLUMN_NAME"));
                colTypes.add(colsRs.getString("TYPE_NAME").toUpperCase());
            }
            colsRs.close();
            
            // Create table in SQLite
            StringBuilder createSql = new StringBuilder();
            createSql.append("CREATE TABLE IF NOT EXISTS ").append(table).append(" (");
            for (int c = 0; c < colNames.size(); c++) {
                if (c > 0) createSql.append(", ");
                createSql.append(colNames.get(c)).append(" ");
                createSql.append(mapType(colTypes.get(c)));
            }
            createSql.append(")");
            
            Statement sqliteStmt = sqliteConn.createStatement();
            sqliteStmt.execute("DROP TABLE IF EXISTS " + table);
            sqliteStmt.execute(createSql.toString());
            sqliteStmt.close();
            
            // Prepare insert
            StringBuilder insertSql = new StringBuilder();
            insertSql.append("INSERT INTO ").append(table).append(" VALUES (");
            for (int c = 0; c < colNames.size(); c++) {
                if (c > 0) insertSql.append(",");
                insertSql.append("?");
            }
            insertSql.append(")");
            
            PreparedStatement insertStmt = sqliteConn.prepareStatement(insertSql.toString());
            
            // Copy data
            Statement selectStmt = tbConn.createStatement();
            ResultSet dataRs = selectStmt.executeQuery("SELECT * FROM " + table);
            
            int rowCount = 0;
            int blobCount = 0;
            
            while (dataRs.next()) {
                for (int c = 0; c < colNames.size(); c++) {
                    String type = colTypes.get(c);
                    
                    if (type.contains("BLOB") || type.contains("BINARY")) {
                        byte[] blob = dataRs.getBytes(c + 1);
                        if (blob != null) {
                            insertStmt.setBytes(c + 1, blob);
                            blobCount++;
                        } else {
                            insertStmt.setNull(c + 1, Types.BLOB);
                        }
                    } else if (type.contains("INT")) {
                        long val = dataRs.getLong(c + 1);
                        if (dataRs.wasNull()) {
                            insertStmt.setNull(c + 1, Types.INTEGER);
                        } else {
                            insertStmt.setLong(c + 1, val);
                        }
                    } else if (type.contains("FLOAT") || type.contains("DOUBLE") || type.contains("NUMERIC")) {
                        double val = dataRs.getDouble(c + 1);
                        if (dataRs.wasNull()) {
                            insertStmt.setNull(c + 1, Types.REAL);
                        } else {
                            insertStmt.setDouble(c + 1, val);
                        }
                    } else {
                        insertStmt.setString(c + 1, dataRs.getString(c + 1));
                    }
                }
                insertStmt.addBatch();
                rowCount++;
                
                if (rowCount % 10000 == 0) {
                    insertStmt.executeBatch();
                }
            }
            insertStmt.executeBatch();
            
            dataRs.close();
            selectStmt.close();
            insertStmt.close();
            
            sqliteConn.commit();
            
            totalRows += rowCount;
            totalBlobs += blobCount;
            
            System.out.printf("%,d rows", rowCount);
            if (blobCount > 0) System.out.printf(" (%,d blobs)", blobCount);
            System.out.println();
        }
        
        // Create indexes
        System.out.println("\nCreating indexes...");
        Statement idxStmt = sqliteConn.createStatement();
        String[][] indexes = {
            {"w_teil", "teil_sachnr"},
            {"w_bildtaf", "bildtaf_btnr"},
            {"w_btzeilen", "btzeilen_btnr"},
            {"w_btzeilen", "btzeilen_sachnr"},
            {"w_grafik", "grafik_grafikid"},
            {"w_publben", "publben_textcode"},
            {"w_fztyp", "fztyp_mospid"},
            {"w_baureihe", "baureihe_baureihe"}
        };
        for (String[] idx : indexes) {
            try {
                idxStmt.execute("CREATE INDEX idx_" + idx[0] + "_" + idx[1] + 
                    " ON " + idx[0] + "(" + idx[1] + ")");
                System.out.println("  idx_" + idx[0] + "_" + idx[1]);
            } catch (Exception e) { /* ignore */ }
        }
        idxStmt.close();
        sqliteConn.commit();
        
        tbConn.close();
        sqliteConn.close();
        
        System.out.println("\n=== Migration Complete ===");
        System.out.printf("Tables: %d\n", tables.size());
        System.out.printf("Rows: %,d\n", totalRows);
        System.out.printf("BLOBs: %,d\n", totalBlobs);
        System.out.println("Output: " + sqliteFile);
    }
    
    static String mapType(String tbType) {
        if (tbType.contains("INT")) return "INTEGER";
        if (tbType.contains("FLOAT") || tbType.contains("DOUBLE") || tbType.contains("NUMERIC")) return "REAL";
        if (tbType.contains("BLOB") || tbType.contains("BINARY")) return "BLOB";
        return "TEXT";
    }
}
