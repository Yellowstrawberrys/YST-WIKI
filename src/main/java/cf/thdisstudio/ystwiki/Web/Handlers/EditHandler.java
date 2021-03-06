package cf.thdisstudio.ystwiki.Web.Handlers;

import cf.thdisstudio.ystwiki.Web.Data;
import cf.thdisstudio.ystwiki.Web.Sessions.SessionManager;
import cf.thdisstudio.ystwiki.Web.Util.Web;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

public class EditHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> q = Web.queryToMap(URLDecoder.decode(exchange.getRequestURI().getRawQuery(), StandardCharsets.UTF_8));
        if(q.containsKey("pageid") && q.containsKey("contents")){
            try {
                Data.editDocument(Integer.parseInt(q.get("pageid")), q.get("contents"), (SessionManager.getSessionByHttpExchange(exchange) != null ? Objects.requireNonNull(SessionManager.getSessionByHttpExchange(exchange)).userID : exchange.getRemoteAddress().getHostString()));
                Headers responseHeaders = exchange.getResponseHeaders();
                String path = Data.getDocumentByPageID(Integer.parseInt(q.get("pageid"))).getTitle();
                if(path.equals("대문"))
                    path = "";
                responseHeaders.add("Location", "/"+ URLEncoder.encode(path, StandardCharsets.UTF_8));
                exchange.sendResponseHeaders(302, -1);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
