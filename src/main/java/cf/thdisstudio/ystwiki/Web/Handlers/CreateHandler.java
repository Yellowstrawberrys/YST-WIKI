package cf.thdisstudio.ystwiki.Web.Handlers;

import cf.thdisstudio.ystwiki.Web.Data;
import cf.thdisstudio.ystwiki.Web.Sessions.SessionManager;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

import static cf.thdisstudio.ystwiki.Web.Handlers.WikiPageHandler.login;
import static cf.thdisstudio.ystwiki.Web.Util.Web.queryToMap;

public class CreateHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if(exchange.getRequestURI().getPath().startsWith("/create/new")){
            Map<String, String> q = queryToMap(URLDecoder.decode(exchange.getRequestURI().getRawQuery(), StandardCharsets.UTF_8));
            if(q.containsKey("title") && q.containsKey("contents")){
                try {
                    if(!Data.isDocExits(q.get("title")))
                        Data.makeDocument(q.get("title"), q.get("contents"), (SessionManager.getSessionByHttpExchange(exchange) != null ? Objects.requireNonNull(SessionManager.getSessionByHttpExchange(exchange)).userID : exchange.getRemoteAddress().getHostString()));
                    Headers responseHeaders = exchange.getResponseHeaders();
                    responseHeaders.add("Location", "/"+q.get("title"));
                    exchange.sendResponseHeaders(302, -1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }else
                exchange.sendResponseHeaders(404, 0);
        }else if(exchange.getRequestURI().getPath().startsWith("/create/")){
            String st = exchange.getRequestURI().getPath().replaceAll("/create/", "");
            String name = st.substring(0, 1).toUpperCase() + st.substring(1);
            String finalResult = WikiPageHandler.ystWiki.replaceAll("\n", "")
                    .replaceFirst("<nav class=\"right\">            <div class=\"line\"></div><br/>            <a href=\"/\" class=\"noDeco\">즐겨찾기</a><br/>            <a href=\"/changes\" class=\"noDeco\">변경사항 보기</a><br/><br/>            <div class=\"line\"></div><br/>            <a href=\"\\?edit=visual\" class=\"noDeco\">편집</a><br/>            <a href=\"\\?edit=text\" class=\"noDeco\">원본 편집</a><br/><br/>            <div class=\"line\"></div>        </nav>", "")
                    .formatted(Data.title, login(exchange), Data.title, "<docTitle> '" + name + "' 문서 생성 </docTitle><hr class=\"topLine\"/>" +
                    "<form action=\"/create/new/\" Action=\"GET\">" +
                            "<input type=\"hidden\" name=\"title\" value=\""+name+"\" />" +
                    "<textarea id=\"contents\" name=\"contents\" class=\"editBox\" rows=\"25\">" +
                    "</textarea><br/><br/><br/><input type=\"submit\" value=\"저장\" class=\"save\"></form>");
            exchange.sendResponseHeaders(200, finalResult.getBytes(StandardCharsets.UTF_8).length);
            OutputStream os = exchange.getResponseBody();
            os.write(finalResult.getBytes(StandardCharsets.UTF_8));
            os.close();
        }else
            exchange.sendResponseHeaders(404, 0);
    }
}
