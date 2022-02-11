package cf.thdisstudio.ystwiki.Web.Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class UploadHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if(exchange.getRequestURI().getPath().equals("/upload")) {
            String answer = "\n" +
                    "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<body>\n" +
                    "\n" +
                    "<form action=\"/upload/file\" method=\"post\" enctype=\"multipart/form-data\">\n" +
                    "  Select image to upload:\n" +
                    "  <input type=\"file\" name=\"uploadFile\" id=\"uploadFile\">\n" +
                    "  <input type=\"submit\" value=\"Upload Image\" name=\"submit\">\n" +
                    "</form>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>\n";
            exchange.sendResponseHeaders(200, answer.getBytes(StandardCharsets.UTF_8).length);
            OutputStream os = exchange.getResponseBody();
            os.write(answer.getBytes(StandardCharsets.UTF_8));
            os.close();
        }
    }
}
