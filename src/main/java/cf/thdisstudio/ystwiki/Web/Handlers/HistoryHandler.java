package cf.thdisstudio.ystwiki.Web.Handlers;

import cf.thdisstudio.ystwiki.Web.Data;
import cf.thdisstudio.ystwiki.Web.Wiki.History;
import cf.thdisstudio.ystwiki.Web.Wiki.WikiDocument;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

import static cf.thdisstudio.ystwiki.Web.Handlers.WikiPageHandler.login;
import static cf.thdisstudio.ystwiki.Web.Handlers.WikiPageHandler.ystWiki;

public class HistoryHandler implements HttpHandler {

    String historyDiv = """
            <div>
                <searchTitle>%s</searchTitle><br/><searchDescription>%s</searchDescription>
            </div>
            <br/>""";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String document = exchange.getRequestURI().getPath().replaceFirst("/history", "");
        WikiDocument wikiDocument = null;
        try {
            wikiDocument = Data.getDocument(document);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String result = "";
        try {
            List<History> hisList = Data.getHistory(wikiDocument.getPath());
            for(History his : hisList)
                result += historyDiv.formatted(his.userName+" - "+his.action.getValue(), his.date+" | "+his.count);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(result.length() < 1)
            result = "<h2>변경 사항이 없음</h2>".formatted(wikiDocument.getTitle());
        result = ystWiki.formatted(wikiDocument.getTitle() + " 변경사항 | " + Data.title, login(exchange), Data.title, "<h1>"+wikiDocument.getTitle()+"에 대한 변경사항</h1><hr/><br/>"+result);
        exchange.sendResponseHeaders(200, result.getBytes(StandardCharsets.UTF_8).length);
        OutputStream os = exchange.getResponseBody();
        os.write(result.getBytes(StandardCharsets.UTF_8));
        os.close();
    }
}
