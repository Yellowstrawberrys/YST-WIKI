package cf.thdisstudio.ystwiki.Web.Handlers;

import cf.thdisstudio.ystwiki.Main.Main;
import cf.thdisstudio.ystwiki.Web.Data;
import cf.thdisstudio.ystwiki.Web.Wiki.WikiDocument;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cf.thdisstudio.ystwiki.Web.Util.Web.queryToMap;

public class SearchHandler implements HttpHandler {

    String results = """
            <div>
                <a href="%s" class="noDeco">
                    <searchTitle>%s</searchTitle><br/>
                    <searchDescription>%s</searchDescription>
                </a>
            </div>
            <br/>
            """;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> query = queryToMap(URLDecoder.decode(exchange.getRequestURI().getRawQuery(), StandardCharsets.UTF_8));
        if(query.containsKey("q")){
            try {
                StringBuilder sb = new StringBuilder();
                List<WikiDocument> documentList = Data.search(query.get("q"));
                if(!documentList.isEmpty())
                    for(WikiDocument wikiDocument : documentList)
                        sb.append(results.formatted(wikiDocument.getPath(), wikiDocument.getTitle(), wikiDocument.getContents()));
                else
                    sb.append("\"").append(query.get("q")).append("\"에 대한 검색결과가 없습니다. 문서를 새로 만들까요? <a href=\"/create/").append(query.get("q")).append("\" class=\"noDeco\">네!</a>");
                String finalResult = WikiPageHandler.ystWiki.replaceAll("\n", "").replaceFirst("<nav class=\"right\">            <div class=\"line\"></div><br/>            <a href=\"/\" class=\"noDeco\">즐겨찾기</a><br/>            <a href=\"/changes\" class=\"noDeco\">변경사항 보기</a><br/><br/>            <div class=\"line\"></div><br/>            <a href=\"\\?edit=visual\" class=\"noDeco\">편집</a><br/>            <a href=\"\\?edit=text\" class=\"noDeco\">원본 편집</a><br/><br/>            <div class=\"line\"></div>        </nav>", "").formatted(Data.title, Data.title, "<docTitle> "+query.get("q")+"에 대한 검색결과 </docTitle><hr class=\"topLine\"/><wikiContents>"+sb+"</wikiContents>");
                exchange.sendResponseHeaders(200, finalResult.getBytes(StandardCharsets.UTF_8).length);
                OutputStream os = exchange.getResponseBody();
                os.write(finalResult.getBytes(StandardCharsets.UTF_8));
                os.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
