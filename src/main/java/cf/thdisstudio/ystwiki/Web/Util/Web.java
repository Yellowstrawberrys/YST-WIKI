package cf.thdisstudio.ystwiki.Web.Util;

import java.util.HashMap;
import java.util.Map;

public class Web {
    public static Map<String, String> queryToMap(String query) {
        if(query == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], param.replaceFirst(entry[0]+"=", ""));
            }else{
                result.put(entry[0], "");
            }
        }
        return result;
    }
}
