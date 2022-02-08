package cf.thdisstudio.ystwiki.Web;

import cf.thdisstudio.ystwiki.Main.Main;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;

public class WikiPageHandler implements HttpHandler {

    String ystWiki = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <title>%s</title>
                <link rel="stylesheet" href="UI.css">
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
                        <img src="logo.png" width="140px"/> <text style="font-size: 66px;">%s</text>
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
                        <a href="/upload" class="noDeco">편집</a><br/>
                        <a href="/특수" class="noDeco">원본 편집</a><br/><br/>
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
                border: 1px black solid;
                height: auto;
                position: absolute;
                left: 100px;
            }
            """;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Main.logger.debug(exchange.getRequestURI().getPath());
        String st = ystWiki.formatted(Data.title, Data.title, "대문:대문<hr/>");
        if(exchange.getRequestURI().getPath().equals("/")) {
            Headers h = exchange.getResponseHeaders();
            h.set("Content-Type", "text/html");
//            String page = readFileFromResource("WikiPage.html");
            exchange.sendResponseHeaders(200, st.getBytes(StandardCharsets.UTF_8).length);
            OutputStream os = exchange.getResponseBody();
            os.write(st.getBytes(StandardCharsets.UTF_8));
            os.close();
            Main.logger.debug("Wow");
        }else if(exchange.getRequestURI().getPath().equals("/UI.css")){
            Headers h = exchange.getResponseHeaders();
            h.set("Content-Type", "text/css");
//            String css = readFileFromResource("UI.css");
            exchange.sendResponseHeaders(200, css.getBytes(StandardCharsets.UTF_8).length);
            OutputStream os = exchange.getResponseBody();
            os.write(css.getBytes(StandardCharsets.UTF_8));
            os.close();
            Main.logger.debug("Wow");
        }
        else if(exchange.getRequestURI().getPath().equals("/logo.png")){
            Headers h = exchange.getResponseHeaders();
            h.set("Content-Type", "image/png");
            exchange.sendResponseHeaders(200, Files.readAllBytes(new File("./webRoot/logo.png").toPath()).length);
            returnFile(exchange, new FileInputStream(new File("./webRoot/logo.png").getAbsolutePath()));
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
