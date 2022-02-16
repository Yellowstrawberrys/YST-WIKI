package cf.thdisstudio.ystwiki.Web.Wiki;

import cf.thdisstudio.ystwiki.Main.Main;
import cf.thdisstudio.ystwiki.Web.Data;
import cf.ystapi.Logging.Logger;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YSTGrammar {

    public static void main(String[] args) throws SQLException {
        Main.logger = new Logger();
        Data.init();
        System.out.println(new YSTGrammar().YSTGrammarToHTML("**sfasd dasfsdaf**"));
    }

    public String YSTGrammarToHTML(String contents) throws SQLException {
        String finalContents = contents;
        finalContents = finalContents.replaceAll("&&left_arrow", "←").replaceAll("&&right_arrow", "→").replaceAll("&&up_arrow", "↑")
                .replaceAll("&&down_arrow", "↓").replaceAll("\n", "<br/>");
        Matcher m = Pattern.compile("\\[\\[(.*?)\\]\\]").matcher(finalContents);
        while (m.find()) {
            String st = m.group(1);
            if(st.startsWith("IMG:")){
                String[] sts = st.replaceFirst("IMG:", "").split("\\|");
                if(Data.isFileExits(sts[0])){
                    finalContents = finalContents.replaceAll("\\[\\["+st+"\\]\\]", "<img src=\"%s\" size=\"%s\"/>".formatted(Data.getFile(sts[0]), (sts.length > 1 ? sts[1] : "auto")));
                }
            }else if(st.startsWith("WEBSITE:")){
                String[] sts = st.replaceFirst("WEBSITE:", "").split(" \\|\\| ");
                finalContents = finalContents.replaceAll("\\[\\["+st+"\\]\\]", "<a href=\"%s\" class=\"LinkToWebsite\">%s</a>".formatted(sts[0], (sts.length > 1 ? sts[1] : sts[0])));
            }
        }
        List<List<String>> strings = Arrays.asList(Arrays.asList("\\*\\*(.*?)\\*\\*", "\\*\\*", "\\*\\*", "<strong>", "</strong>"),
                Arrays.asList("\\_\\_(.*?)\\_\\_", "\\_\\_", "\\_\\_", "<u>", "</u>"),
                Arrays.asList("\\_(.*?)\\_", "\\_", "\\_", "<em>", "</em>"),
                Arrays.asList("===== (.*?) =====", "===== ", " =====", "<h5>", "</h5><hr class=\"topLine\"/>"),
                Arrays.asList("==== (.*?) ====", "==== ", " ====", "<h4>", "</h4><hr class=\"topLine\"/>"),
                Arrays.asList("=== (.*?) ===", "=== ", " ===", "<h3>", "</h3><hr class=\"topLine\"/>"),
                Arrays.asList("== (.*?) ==", "== ", " ==", "<h2>", "</h2><hr class=\"topLine\"/>"),
                Arrays.asList("= (.*?) =", "= ", " =", "<h1>", "</h1><hr class=\"topLine\"/>"));
        for(List<String> sts : strings) {
            Matcher ma = Pattern.compile(sts.get(0)).matcher(finalContents);
            while (ma.find()) {
                String st = ma.group(1);
                finalContents = finalContents.replaceAll(sts.get(1) + st + sts.get(2), sts.get(3) + st + sts.get(4));
            }
        }
        return finalContents;
    }
}
