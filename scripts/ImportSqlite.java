import java.sql.*;
import java.io.*;
import java.util.*;

/**
 * Import ETK CSVs to SQLite with 1:1 schema from Transbase
 * 
 * Usage: java -cp .:tbjdbc.jar:sqlite-jdbc.jar ImportSqlite <csv_dir> <output.db>
 */
public class ImportSqlite {
    static String TB_URL = "jdbc:transbase://192.168.101.150:2024/etk_publ";
    static String TB_USER = "tbadmin";
    static String TB_PASS = "altabe";
    
    public static void main(String[] args) throws Exception {
        String csvDir = args.length > 0 ? args[0] : "/tmp/etk_data/csv";
        String sqliteDb = args.length > 1 ? args[1] : "etk.sqlite";
        
        // Connect to Transbase to get schema
        Class.forName("transbase.jdbc.Driver");
        Connection tbConn = DriverManager.getConnection(TB_URL, TB_USER, TB_PASS);
        DatabaseMetaData tbMeta = tbConn.getMetaData();
        
        // Connect to SQLite
        Class.forName("org.sqlite.JDBC");
        Connection sqliteConn = DriverManager.getConnection("jdbc:sqlite:" + sqliteDb);
        sqliteConn.setAutoCommit(false);
        Statement sqliteStmt = sqliteConn.createStatement();
        
        // Get all tables
        ResultSet tables = tbMeta.getTables(null, null, "%", new String[]{"TABLE"});
        List<String> tableNames = new ArrayList<>();
        while (tables.next()) {
            tableNames.add(tables.getString("TABLE_NAME"));
        }
        tables.close();
        
        System.out.println("Creating " + tableNames.size() + " tables in SQLite...\n");
        
        for (String table : tableNames) {
            System.out.print(table + "... ");
            
            // Get columns from Transbase
            ResultSet cols = tbMeta.getColumns(null, null, table, "%");
            StringBuilder createSql = new StringBuilder();
            createSql.append("CREATE TABLE IF NOT EXISTS ").append(table).append(" (\n");
            
            List<String> colNames = new ArrayList<>();
            boolean first = true;
            while (cols.next()) {
                if (!first) createSql.append(",\n");
                first = false;
                
                String colName = cols.getString("COLUMN_NAME");
                String typeName = cols.getString("TYPE_NAME");
                int size = cols.getInt("COLUMN_SIZE");
                boolean nullable = cols.getInt("NULLABLE") == 1;
                
                colNames.add(colName);
                
                // Map Transbase types to SQLite
                String sqliteType = mapType(typeName, size);
                
                createSql.append("  ").append(colName).append(" ").append(sqliteType);
                if (!nullable) createSql.append(" NOT NULL");
            }
            cols.close();
            createSql.append("\n)");
            
            // Create table
            sqliteStmt.execute("DROP TABLE IF EXISTS " + table);
            sqliteStmt.execute(createSql.toString());
            
            // Import CSV if exists
            String csvFile = csvDir + "/etk_publ_" + table + ".csv";
            File csv = new File(csvFile);
            if (csv.exists()) {
                int rows = importCsv(sqliteConn, table, colNames, csvFile);
                System.out.println(rows + " rows");
            } else {
                System.out.println("no CSV");
            }
        }
        
        sqliteConn.commit();
        
        // Create indexes for common lookup columns
        System.out.println("\nCreating indexes...");
        String[] indexCols = {"sachnr", "btnr", "grafikid", "textcode", "mospid", "baureihe"};
        for (String table : tableNames) {
            for (String col : indexCols) {
                try {
                    ResultSet colCheck = tbMeta.getColumns(null, null, table, "%" + col + "%");
                    while (colCheck.next()) {
                        String fullCol = colCheck.getString("COLUMN_NAME");
                        String idxName = "idx_" + table + "_" + fullCol;
                        sqliteStmt.execute("CREATE INDEX IF NOT EXISTS " + idxName + 
                            " ON " + table + "(" + fullCol + ")");
                    }
                    colCheck.close();
                } catch (Exception e) { /* ignore */ }
            }
        }
        sqliteConn.commit();
        
        tbConn.close();
        sqliteConn.close();
        
        System.out.println("\nDone! Output: " + sqliteDb);
    }
    
    static String mapType(String tbType, int size) {
        tbType = tbType.toUpperCase();
        if (tbType.contains("INT")) return "INTEGER";
        if (tbType.contains("CHAR") || tbType.contains("VARCHAR")) return "TEXT";
        if (tbType.contains("FLOAT") || tbType.contains("DOUBLE") || tbType.contains("NUMERIC")) return "REAL";
        if (tbType.contains("BLOB") || tbType.contains("BINARY")) return "BLOB";
        if (tbType.contains("DATE") || tbType.contains("TIME")) return "TEXT";
        return "TEXT";
    }
    
    static int importCsv(Connection conn, String table, List<String> cols, String csvFile) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
            new FileInputStream(csvFile), "UTF-8"));
        
        // Skip header
        reader.readLine();
        
        // Prepare insert
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(table).append(" (");
        sql.append(String.join(",", cols));
        sql.append(") VALUES (");
        for (int i = 0; i < cols.size(); i++) {
            if (i > 0) sql.append(",");
            sql.append("?");
        }
        sql.append(")");
        
        PreparedStatement pstmt = conn.prepareStatement(sql.toString());
        
        int count = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = parseCsvLine(line);
            for (int i = 0; i < cols.size() && i < values.length; i++) {
                String val = values[i];
                // Handle BLOB references
                if (val.startsWith("[BLOB:")) {
                    pstmt.setString(i + 1, val); // Store reference as text
                } else if (val.isEmpty()) {
                    pstmt.setNull(i + 1, Types.NULL);
                } else {
                    pstmt.setString(i + 1, val);
                }
            }
            pstmt.addBatch();
            count++;
            
            if (count % 10000 == 0) {
                pstmt.executeBatch();
            }
        }
        pstmt.executeBatch();
        pstmt.close();
        reader.close();
        
        return count;
    }
    
    static String[] parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());
        
        return result.toArray(new String[0]);
    }
}
