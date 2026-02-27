///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS transbase:tbjdbc:LATEST

import java.sql.*;
import java.io.*;
import java.util.*;

public class export {
    static String URL = "jdbc:transbase://192.168.101.150:2024/";
    static String USER = "tbadmin";
    static String PASS = "altabe";
    
    public static void main(String[] args) throws Exception {
        String outDir = args.length > 0 ? args[0] : "etk_export";
        new File(outDir + "/csv").mkdirs();
        new File(outDir + "/blobs").mkdirs();
        
        Class.forName("transbase.jdbc.Driver");
        
        String[] dbs = {"etk_publ", "etk_nutzer", "etk_preise"};
        
        for (String db : dbs) {
            System.out.println("\n=== Database: " + db + " ===");
            Connection conn = DriverManager.getConnection(URL + db, USER, PASS);
            
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet tables = meta.getTables(null, null, "%", new String[]{"TABLE"});
            
            List<String> tableNames = new ArrayList<>();
            while (tables.next()) {
                tableNames.add(tables.getString("TABLE_NAME"));
            }
            
            System.out.println("Found " + tableNames.size() + " tables");
            
            for (String table : tableNames) {
                exportTable(conn, table, outDir + "/csv/" + db + "_" + table + ".csv", outDir + "/blobs");
            }
            
            conn.close();
        }
        
        System.out.println("\n=== Export complete! ===");
    }
    
    static void exportTable(Connection conn, String table, String csvFile, String blobDir) {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + table);
            ResultSetMetaData meta = rs.getMetaData();
            int cols = meta.getColumnCount();
            
            PrintWriter out = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(csvFile), "UTF-8"));
            
            // Header
            for (int i = 1; i <= cols; i++) {
                if (i > 1) out.print(",");
                out.print(quote(meta.getColumnName(i)));
            }
            out.println();
            
            int rows = 0;
            int blobs = 0;
            while (rs.next()) {
                for (int i = 1; i <= cols; i++) {
                    if (i > 1) out.print(",");
                    
                    int type = meta.getColumnType(i);
                    if (type == Types.BLOB || type == Types.BINARY || type == Types.LONGVARBINARY) {
                        byte[] data = rs.getBytes(i);
                        if (data != null && data.length > 0) {
                            String blobFile = table + "_" + meta.getColumnName(i) + "_" + rows + ".bin";
                            FileOutputStream fos = new FileOutputStream(blobDir + "/" + blobFile);
                            fos.write(data);
                            fos.close();
                            out.print(quote("[BLOB:" + blobFile + "]"));
                            blobs++;
                        } else {
                            out.print("");
                        }
                    } else {
                        String val = rs.getString(i);
                        out.print(val == null ? "" : quote(val));
                    }
                }
                out.println();
                rows++;
            }
            
            out.close();
            rs.close();
            stmt.close();
            
            String info = rows + " rows";
            if (blobs > 0) info += ", " + blobs + " blobs";
            System.out.println("  " + table + ": " + info);
            
        } catch (Exception e) {
            System.out.println("  " + table + ": ERROR - " + e.getMessage());
        }
    }
    
    static String quote(String s) {
        if (s == null) return "";
        if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
            return "\"" + s.replace("\"", "\"\"") + "\"";
        }
        return s;
    }
}
