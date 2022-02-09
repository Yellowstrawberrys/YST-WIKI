package cf.thdisstudio.ystwiki.Web.Handlers;

import cf.thdisstudio.ystwiki.Main.Main;
import cf.thdisstudio.ystwiki.Web.Data;
import cf.thdisstudio.ystwiki.Web.Wiki.WikiDocument;
import cf.thdisstudio.ystwiki.Web.Wiki.YSTGrammar;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static cf.thdisstudio.ystwiki.Web.Util.Web.queryToMap;

public class WikiPageHandler implements HttpHandler {

    public static String ystWiki = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <title>%s</title>
                <link rel="stylesheet" href="/UI.css">
            </head>
                <body>
                    <div class="top">
                        <div class="right">
                            <a class="noDeco" href="/login">
                                Login
                            </a>
                            <a class="noDeco" href="/register">
                                Sign in
                            </a>
                        </div>
                        <a href="/" class="noDeco"><img src="/logo.png" width="140px"/> <text style="font-size: 66px; color: black">%s</text></a>
                        <div class="right">
                            <form action="/search" method="GET">
                            <input type="search" name="q" placeholder="검색" autocapitalize="sentences" title="검색" id="searchInput" autocomplete="off">
                            </form>
                        </div>
                    </div>
                    <br/>
                    <nav class="left">
                        <div class="line"></div><br/>
                        <a href="/" class="noDeco">대문</a><br/>
                        <a href="/changes" class="noDeco">최근 변경</a><br/>
                        <a href="/help" class="noDeco">도움말</a><br/><br/>
                        <div class="line"></div><br/>
                        <a href="/upload" class="noDeco">파일 업로드</a><br/>
                        <a href="/특수" class="noDeco">특수 페이지</a>
                    </nav>
                    <nav class="right">
                        <div class="line"></div><br/>
                        <a href="/" class="noDeco">즐겨찾기</a><br/>
                        <a href="/changes" class="noDeco">변경사항 보기</a><br/><br/>
                        <div class="line"></div><br/>
                        <a href="?edit=visual" class="noDeco">편집</a><br/>
                        <a href="?edit=text" class="noDeco">원본 편집</a><br/><br/>
                        <div class="line"></div>
                    </nav>
                    <wikibody>
                        <!--            자바에서 여기에 위키를 채워넣음-->
                        %s
                    </wikibody>
                </body>
            </html>""";

    String css = """
            body{
                font-family: sans-serif;
                background-color: #f6f6f6;
            }
                        
            .top{
                top: 0;
            }
                        
            .right{
                right: 0;
                margin-right: 0;
                position: absolute;
            }
                        
            .noDeco{
                text-decoration: none;
            }
                        
            .left{
                left: 0;
                margin-left:0;
                position: absolute;
            }
                        
            .line{
                width: 100px;
                border-bottom: 1px solid black;
                position: absolute;
            }
                        
            wikibody{
                /*display: flex;*/
                /*align-items: center;*/
                /*justify-content: center;*/
                width: calc(100% - 201px);
                min-height: 40%;
                border: 1px black solid;
                height: auto;
                position: absolute;
                left: 100px;
                background-color: white;
            }
            wikiContents{
                margin-left: 1%;
            }
            docTitle{
                font-size: 40px;
                margin-left: 1%;
            }
            .topLine {
                width: 98%;
            }
            searchTitle{
                font-size: 20px;
                color: blue;
                font-weight: bold;
            }
            searchDescription{
                font-size: 15px;
                color: gray;
            }
            
            .editBox {
                -webkit-box-sizing: border-box;
                   -moz-box-sizing: border-box;
                        box-sizing: border-box;
                        width: 100%;
                        max-height: 100vh;
                        min-height: 5em;
                        white-space: pre-wrap;
            }
            
            .save {
                color: white;
                font-size: 15px;
                font-weight: bold;
                background-color: #4043ff;
                width: 50px;
                height: 30px;
                border: 1px black solid;
            }
            
            .LinkToWebsite{
                text-decoration: none;
            }
            
            .LinkToWebsite:hover{
                text-decoration: blue;
            }
            
            h1{
                font-size: 1.8em;
            }
            
            h2{
                font-size: 1.5em;
            }
            
            h3{
                font-size: 1.2em;
            }
            
            h4{
                font-size: 0.9em;
            }
            
            h5{
                font-size: 0.6em;
            }
            
            h1,h2,h3,h4,h5{
                margin: 0px;
                margin-left: 1%;
            }
            """;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Main.logger.debug(exchange.getRequestURI().getPath());
        if(exchange.getRequestURI().getPath().equals("/UI.css")){
            Headers h = exchange.getResponseHeaders();
            h.set("Content-Type", "text/css");
            exchange.sendResponseHeaders(200, css.getBytes(StandardCharsets.UTF_8).length);
            OutputStream os = exchange.getResponseBody();
            os.write(css.getBytes(StandardCharsets.UTF_8));
            os.close();
        }else if(exchange.getRequestURI().getPath().equals("/logo.png")){
            Headers h = exchange.getResponseHeaders();
            h.set("Content-Type", "image/png");
            exchange.sendResponseHeaders(200, Files.readAllBytes(new File("./webRoot/logo.png").toPath()).length);
            returnFile(exchange, new FileInputStream(new File("./webRoot/logo.png").getAbsolutePath()));
        }else {
            try {
                if(Data.isDocExits(exchange.getRequestURI().getPath())) {
                    WikiDocument wikiDoc = Data.getDocument(exchange.getRequestURI().getPath());
                    String doc = null;
                    int code = 200;
                    if(exchange.getRequestURI().getQuery() != null) {
                        Map<String, String> q = queryToMap(exchange.getRequestURI().getQuery());
                        if(q.containsKey("edit"))
                            doc = ystWiki.formatted(wikiDoc.getTitle() + " 수정 | " + Data.title, Data.title, "<docTitle>'" + wikiDoc.getTitle() + "' 수정하기</docTitle><hr class=\"topLine\"/> <form action=\"/edit\" Action=\"GET\">" +
                                    "<input type=\"hidden\" name=\"pageid\" value=\""+wikiDoc.getPageId()+"\" />" +
                                    "<textarea id=\"contents\" name=\"contents\" class=\"editBox\" rows=\"25\">" +
                                            wikiDoc.getContents() +
                                    "</textarea><br/><br/><br/><input type=\"submit\" value=\"저장\" class=\"save\"></form>");
                        else
                            code = 301;
                    }else {
                        doc = ystWiki.formatted(wikiDoc.getTitle() + " | " + Data.title, Data.title, "<docTitle>" + wikiDoc.getTitle() + "</docTitle><hr class=\"topLine\"/><wikiContents>" + new YSTGrammar().YSTGrammarToHTML(wikiDoc.getContents()) + "</wikiContents>");
                    }
                    Headers h = exchange.getResponseHeaders();
                    if(code != 301)
                        h.set("Content-Type", "text/html");
                    else
                        h.set("Location", exchange.getRequestURI().getPath());
                    exchange.sendResponseHeaders(code, (code == 301 ? -1 : doc.getBytes(StandardCharsets.UTF_8).length));
                    if(code != 301) {
                        OutputStream os = exchange.getResponseBody();
                        os.write(doc.getBytes(StandardCharsets.UTF_8));
                        os.close();
                    }
                }else
                    Main.logger.info("Not Found");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void returnFile(HttpExchange exchange, InputStream ipt) throws IOException {
        byte[] buffer = new byte[8 * 1460]; // Maximum TCP packet size
        int bytesRead;
        while ((bytesRead = ipt.read(buffer)) != -1) {
            exchange.getResponseBody().write(buffer, 0, bytesRead);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        ipt.close();
    }

    private String readFileFromResource(String loc) throws IOException {
        String finalSt = "";
        String line;
        BufferedReader ipt = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream(loc))));
        while ((line = ipt.readLine()) != null) {
            finalSt += line;
        }
        ipt.close();
        return finalSt;
    }
}
