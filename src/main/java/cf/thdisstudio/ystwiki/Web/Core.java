package cf.thdisstudio.ystwiki.Web;

import cf.thdisstudio.ystwiki.Main.Main;
import cf.thdisstudio.ystwiki.Web.Handlers.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.concurrent.Executors;

public class Core {

    HttpServer server = null;

    public void init(String path, int port) throws IOException {
        Main.logger.info("Starting Web Core Init");
        server = HttpServer.create(new InetSocketAddress(port), 1);
        server.setExecutor(Executors.newCachedThreadPool());
        server.createContext("/favicon.ico", exchange -> {
            File file = null;
            file = new File(new File("./icon/favicon.ico").toURI());
            if(file == null) {
                Main.logger.warn("Favicon has been not found from image folder");
                exchange.sendResponseHeaders(404, 0);
            }else {
                exchange.getResponseHeaders().set("Content-Type", "image/x-icon");
                exchange.sendResponseHeaders(200, file.length());
                OutputStream os = exchange.getResponseBody();
                Files.copy(file.toPath(), os);
                os.close();
            }
        });
        server.createContext("/", new WikiPageHandler());
        server.createContext("/history", new HistoryHandler());
//        server.createContext("/upload/file", new FileUploadHandler());
        server.createContext("/login", new LoginHandler());
        server.createContext("/auth/login", new Auth());
        server.createContext("/upload", new UploadHandler());
        server.createContext("/search", new SearchHandler());
        server.createContext("/create", new CreateHandler());
        server.createContext("/edit", new EditHandler());
        server.start();
        Main.logger.info("Http Webserver has been successfully started");
    }


}
