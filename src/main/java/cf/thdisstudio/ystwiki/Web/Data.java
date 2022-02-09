package cf.thdisstudio.ystwiki.Web;

import cf.thdisstudio.ystwiki.Main.Main;
import cf.thdisstudio.ystwiki.Web.Wiki.WikiDocument;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public static boolean isDocExits(String docLoc) throws SQLException {
        ResultSet rt =stmt.executeQuery("SELECT `pageid` FROM documents WHERE `path` LIKE '"+docLoc+"'");
        rt.first();
        conn.commit();
        return rt.first();
    }

    public static WikiDocument getDocument(String path) throws SQLException {
        ResultSet rt =stmt.executeQuery("SELECT `pageid`, `title`, `path`, `createdTime`, `lastedited`, `permission`, `contents` FROM documents WHERE `path` LIKE '"+path+"'");
        rt.first();
        conn.commit();
        return new WikiDocument(rt.getInt(1), rt.getString(2), rt.getString(3), rt.getString(4), rt.getString(5), rt.getInt(6), rt.getString(7));
    }

    public static WikiDocument getDocumentByPageID(int pageId) throws SQLException {
        ResultSet rt =stmt.executeQuery("SELECT `pageid`, `title`, `path`, `createdTime`, `lastedited`, `permission`, `contents` FROM documents WHERE `pageid` LIKE "+pageId+"");
        rt.first();
        conn.commit();
        return new WikiDocument(rt.getInt(1), rt.getString(2), rt.getString(3), rt.getString(4), rt.getString(5), rt.getInt(6), rt.getString(7));
    }

    public static void editDocument(int pageId, String contents) throws SQLException {
        stmt.executeQuery("UPDATE `ystwiki`.`documents` SET `contents`='"+contents+"', `lastedited`='"+System.currentTimeMillis()+"' WHERE pageid="+pageId+"");
        conn.commit();
    }

    public static void makeDocument(String title, String contents) throws SQLException {
        ResultSet rt = stmt.executeQuery("SELECT `pageid` FROM documents ORDER BY pageid DESC LIMIT 1;");
        rt.first();
        stmt.executeQuery("INSERT INTO `ystwiki`.`documents` (`pageid`, `title`, `path`, `createdTime`, `lastedited`, `permission`, `contents`) VALUES ('"+(rt.getInt(1)+1)+"', '"+title+"', '/"+title+"', '"+System.currentTimeMillis()+"', '"+System.currentTimeMillis()+"', '0', '"+contents+"');");
        conn.commit();
    }

    public static List<WikiDocument> search(String q) throws SQLException {
        ResultSet rt =stmt.executeQuery("SELECT `pageid`, `title`, `path`, `createdTime`, `lastedited`, `permission`, `contents` FROM documents WHERE `title` LIKE '%"+q+"%'");
        List<WikiDocument> wikiDocuments = new ArrayList<>();
        while (rt.next())
            wikiDocuments.add(new WikiDocument(rt.getInt(1), rt.getString(2), rt.getString(3), rt.getString(4), rt.getString(5), rt.getInt(6), rt.getString(7)));
        return wikiDocuments;
    }
}
