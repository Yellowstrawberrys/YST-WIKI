package cf.thdisstudio.ystwiki.Web.Handlers;

import cf.thdisstudio.ystwiki.Web.Data;
import cf.thdisstudio.ystwiki.Web.Sessions.SessionManager;
import cf.thdisstudio.ystwiki.Web.Util.Web;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.Map;

public class Auth implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, -1);
        if(exchange.getRequestMethod().equalsIgnoreCase("POST")){
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
            try {
                System.out.println("?");
                Map<String, String> q = Web.queryToMap(URLDecoder.decode(bufferedReader.readLine()));
                String uid;
                if((uid = Data.Login(q.get("id"),q.get("pass"))) != null){
                    System.out.println("Login Success");
                    exchange.getRequestHeaders().add("Set-Cookie", "session-id="+SessionManager.createNewSession(exchange.getRemoteAddress().getHostString(), uid));
                }else
                    System.out.println("Login Failed");
            } catch (SQLException e) {
                System.out.println("Exception");
                e.printStackTrace();
            }
        }else
            System.out.println("s");
    }
}
