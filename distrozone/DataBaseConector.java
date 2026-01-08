package distrozone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database Connector - Connection Pool Pattern
 * Setiap request dapat connection BARU, bukan singleton!
 */
public class DataBaseConector {
    
    private static final String URL = "jdbc:mysql://localhost:3306/distrozone_db";
    private static final String USER = "root";
    private static final String PASS = "";
    
    // Static initializer - load driver sekali saat class di-load
    static {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            System.out.println("✅ MySQL Driver loaded");
        } catch (SQLException e) {
            System.err.println("❌ Failed to load MySQL Driver: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Get NEW connection setiap kali dipanggil
     * PENTING: Yang memanggil method ini WAJIB close connection!
     */
    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Koneksi Berhasil");
            return conn; // Return connection BARU
            
        } catch (SQLException e) {
            System.err.println("Koneksi Gagal: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Close connection dengan aman
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                    System.out.println("✅ Connection closed");
                }
            } catch (SQLException e) {
                System.err.println("❌ Error closing connection: " + e.getMessage());
            }
        }
    }
}