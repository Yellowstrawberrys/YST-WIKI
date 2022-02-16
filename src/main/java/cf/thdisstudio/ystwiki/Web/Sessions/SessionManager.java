package cf.thdisstudio.ystwiki.Web.Sessions;

import com.sun.net.httpserver.HttpExchange;

import java.util.HashMap;
import java.util.UUID;

public class SessionManager {
    protected static HashMap<String, Session> Sessions = new HashMap<>();

    public static Session getSessionBySessionID(String sessionID){
        return Sessions.get(sessionID);
    }

    public static String createNewSession(String ip, String userID, String userName){
        String sessionID = UUID.randomUUID().toString();
        Session session = new Session();
        session.IP = ip;
        session.userID = userID;
        session.userName = userName;
        session.endDate = System.currentTimeMillis()+432000000;
        session.sessionID = sessionID;
        Sessions.put(sessionID, session);

        return sessionID;
    }

    public static Session getSessionByHttpExchange(HttpExchange httpExchange){
        return (httpExchange.getRequestHeaders() == null || httpExchange.getRequestHeaders().isEmpty() || !httpExchange.getRequestHeaders().containsKey("Cookie") ? null : getSessionBySessionID(httpExchange.getRequestHeaders().get("Cookie").get(0)));
    }
}
