package cf.thdisstudio.ystwiki.Web.Sessions;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class SessionManager {
    protected static HashMap<String, Session> Sessions = new HashMap<>();

    public static Session getSessionBySessionID(String sessionID){
        return Sessions.get(sessionID);
    }

    public static String createNewSession(String ip, String userID){
        String sessionID = UUID.randomUUID().toString();
        Session session = new Session();
        session.IP = ip;
        session.userID = userID;
        session.endDate = System.currentTimeMillis()+432000000;
        session.sessionID = sessionID;
        Sessions.put(sessionID, session);

        return sessionID;
    }
}
