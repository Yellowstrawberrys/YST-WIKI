package cf.thdisstudio.ystwiki.Web.Handlers;

import cf.thdisstudio.ystwiki.Web.Data;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static cf.thdisstudio.ystwiki.Web.Handlers.WikiPageHandler.login;

public class LoginHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String finalResult = WikiPageHandler.ystWiki.replaceAll("\n", "")
                .replaceFirst("<nav class=\"right\">            <div class=\"line\"></div><br/>            <a href=\"/\" class=\"noDeco\">즐겨찾기</a><br/>            <a href=\"/changes\" class=\"noDeco\">변경사항 보기</a><br/><br/>            <div class=\"line\"></div><br/>            <a href=\"\\?edit=visual\" class=\"noDeco\">편집</a><br/>            <a href=\"\\?edit=text\" class=\"noDeco\">원본 편집</a><br/><br/>            <div class=\"line\"></div>        </nav>", "")
                .formatted(Data.title, login(exchange), Data.title, "<docTitle> 로그인 </docTitle><hr class=\"topLine\"/>" +
                        "<form action=\"/auth/login\" method=\"POST\">" +
                        "<label>ID를 입력하시오</label><br>" +
                        "<input type=\"text\" name=\"id\"/>" +
                        "<label>비밀번호를 입력하시오</label><br>" +
                        "<input type=\"password\" name=\"pass\"/>" +
                        "</textarea><br/><br/><br/><input type=\"submit\" value=\"로그인\" class=\"save\"></form>");
        exchange.sendResponseHeaders(200, finalResult.getBytes(StandardCharsets.UTF_8).length);
        OutputStream os = exchange.getResponseBody();
        os.write(finalResult.getBytes(StandardCharsets.UTF_8));
        os.close();
    }
}
