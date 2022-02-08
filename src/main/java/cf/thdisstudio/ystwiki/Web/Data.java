package cf.thdisstudio.ystwiki.Web;

import cf.thdisstudio.ystwiki.Main.Main;

import java.sql.*;

public class Data {
    public static String title = "YST WIKI";
    public static Connection conn;
    static Statement stmt;

    public static void init(){
        Main.logger.info("Initializing Data");
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            Main.logger.info("Connecting to Database...");
            conn = DriverManager.getConnection(
                    "jdbc:mariadb://localhost:3306/ystwiki?useUnicode=true&passwordCharacterEncoding=utf-8", "root", "root");
            Main.logger.info("Success to Connect Database!");
            stmt = conn.createStatement();
        } catch (Exception se) {
            Main.logger.error("Error while %s\n\nMessage: %s".formatted(se.getCause(), se.getMessage()));
            //Handle errors for JDBC
            se.printStackTrace();
        }//Handle errors for Class.forName

        Main.logger.info("Data Init has been finished");
    }

    public static boolean isFileExits(String fileName) throws SQLException {
        ResultSet rt = stmt.executeQuery("SELECT `Name` FROM resources WHERE `Name` LIKE '%"+fileName+"%'");
        conn.commit();
        return rt.first();
    }

    public static String getFile(String fileName) throws SQLException{
        ResultSet rt = stmt.executeQuery("SELECT `Path` FROM resources WHERE `Name` LIKE '%"+fileName+"%'");
        rt.first();
        conn.commit();
        return rt.getString("Path");
    }
}
