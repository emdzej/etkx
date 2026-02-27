import java.sql.*;
import java.io.*;

/**
 * Export w_grafik BLOBs with proper naming: <grafik_id>_<art>.<format>
 * 
 * Usage: java -cp .:tbjdbc.jar ExportBlobs <output_dir>
 * 
 * Output: 
 *   <output_dir>/
 *     123_T.png      (Thumbnail)
 *     123_Z.tiff     (Zeichnung/Drawing)
 *     456_T.png
 *     ...
 */
public class ExportBlobs {
    static String URL = "jdbc:transbase://192.168.101.150:2024/etk_publ";
    static String USER = "tbadmin";
    static String PASS = "altabe";
    
    public static void main(String[] args) throws Exception {
        String outDir = args.length > 0 ? args[0] : "blobs";
        new File(outDir).mkdirs();
        
        Class.forName("transbase.jdbc.Driver");
        Connection conn = DriverManager.getConnection(URL, USER, PASS);
        
        System.out.println("Exporting BLOBs from w_grafik...");
        System.out.println("Output: " + outDir + "/");
        System.out.println();
        
        // Get total count first
        Statement countStmt = conn.createStatement();
        ResultSet countRs = countStmt.executeQuery("SELECT COUNT(*) FROM w_grafik WHERE grafik_blob IS NOT NULL");
        countRs.next();
        int total = countRs.getInt(1);
        countRs.close();
        countStmt.close();
        
        System.out.println("Total BLOBs: " + total);
        System.out.println();
        
        // Export BLOBs
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
            "SELECT grafik_grafikid, grafik_art, grafik_format, grafik_blob " +
            "FROM w_grafik WHERE grafik_blob IS NOT NULL"
        );
        
        int count = 0;
        int errors = 0;
        long totalBytes = 0;
        
        while (rs.next()) {
            int grafikId = rs.getInt("grafik_grafikid");
            String art = rs.getString("grafik_art");        // T=Thumbnail, Z=Zeichnung
            String format = rs.getString("grafik_format");  // PNG, TIF, etc.
            byte[] blob = rs.getBytes("grafik_blob");
            
            if (blob != null && blob.length > 0) {
                // Determine extension
                String ext = format.equalsIgnoreCase("TIF") ? "tiff" : format.toLowerCase();
                
                // Filename: <grafik_id>_<art>.<ext>
                String filename = grafikId + "_" + art + "." + ext;
                
                try {
                    FileOutputStream fos = new FileOutputStream(outDir + "/" + filename);
                    fos.write(blob);
                    fos.close();
                    totalBytes += blob.length;
                    count++;
                } catch (Exception e) {
                    errors++;
                    System.err.println("Error writing " + filename + ": " + e.getMessage());
                }
            }
            
            // Progress every 1000
            if (count % 1000 == 0) {
                System.out.printf("Progress: %d/%d (%.1f%%) - %.1f MB\n", 
                    count, total, (100.0 * count / total), totalBytes / 1024.0 / 1024.0);
            }
        }
        
        rs.close();
        stmt.close();
        conn.close();
        
        System.out.println();
        System.out.println("=== Export complete ===");
        System.out.println("Files: " + count);
        System.out.println("Errors: " + errors);
        System.out.printf("Total size: %.1f MB\n", totalBytes / 1024.0 / 1024.0);
    }
}
