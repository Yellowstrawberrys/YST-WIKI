package cf.thdisstudio.ystwiki.Web.Wiki;

import cf.thdisstudio.ystwiki.Main.Main;
import cf.thdisstudio.ystwiki.Web.Data;
import cf.ystapi.Logging.Logger;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YSTGrammar {

    public static void main(String[] args) throws SQLException {
        Main.logger = new Logger();
        Data.init();
        System.out.println(new YSTGrammar().YSTGrammarToHTML("[[IMG:wow]]"));
    }

    public String YSTGrammarToHTML(String contents) throws SQLException {
        String finalContents = contents;
        finalContents = finalContents.replaceAll("&&left_arrow", "←").replaceAll("&&right_arrow", "→").replaceAll("&&up_arrow", "↑")
                .replaceAll("&&down_arrow", "↓");
        Matcher m = Pattern.compile("\\[\\[(.*?)\\]\\]").matcher(finalContents);
        while (m.find()) {
            String st = m.group(1);
            if(st.startsWith("IMG:")){
                String[] sts = st.replaceFirst("IMG:", "").split("\\|");
                if(Data.isFileExits(sts[0])){
                    finalContents = finalContents.replaceAll("\\[\\["+st+"\\]\\]", "<img src=\"%s\" size=\"%s\"/>".formatted(Data.getFile(sts[0]), (sts.length > 1 ? sts[1] : "auto")));
                }
            }
        }
        return finalContents;
    }
}
