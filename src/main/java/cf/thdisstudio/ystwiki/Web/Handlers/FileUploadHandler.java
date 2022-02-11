package cf.thdisstudio.ystwiki.Web.Handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUploadHandler implements HttpHandler {

    boolean isStarted = false;
    String fileName = "webRoot/Uploading.yst";
    int BUFFER_SIZE = 4096;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException, IOException {
        String requestMethod = httpExchange.getRequestMethod();

        if (requestMethod.equalsIgnoreCase("POST"))
        {
            Headers responseHeaders = httpExchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "text/plain");
            responseHeaders.add("charset", "utf-8");
            httpExchange.sendResponseHeaders(200, 0);

            InputStream ipt = httpExchange.getRequestBody();

            FileOutputStream fileOutputStream = new FileOutputStream(fileName);

            byte[] buffer = new byte[BUFFER_SIZE];

            int bytesRead;

            while ((bytesRead = ipt.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
//            bufferedReader.lines().forEachOrdered(s -> {
//                try {
////                    System.out.println(s);
//                    if(isStarted){
//                        if(s.contains("IEND")) {
//                            isStarted = false;
//                            fileOutputStream.write((s+"\n").getBytes(StandardCharsets.UTF_8));
//                        }else
//                            fileOutputStream.write((s+"\n").getBytes(StandardCharsets.UTF_8));
//                    }else if(s.contains("Content-Type:")) {
//                        isStarted = true;
//                    }else if(s.contains("filename="))
//                        fileName = s.split("filename=")[1].replaceAll("\"", "");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
            fileName = "yst.png";
            fileOutputStream.close();

            System.out.println(fileName);

            String line = null;

            List<String> lines = new ArrayList<>();

            File f1 = new File("webRoot/Uploading.yst");

            FileReader fr = new FileReader(f1);
            BufferedReader br = new BufferedReader(fr);

            DataOutputStream opt = new DataOutputStream(new FileOutputStream(new File("webRoot/", fileName)));

            int stack = 0;
            int linenum = 0;

            while ((line = br.readLine()) != null) {
                if (line.contains("filename="))
                    fileName = line.split("filename=")[1].replaceAll("\"", "");

                if(linenum > 3) {
                    if (line.matches("-----------------------------([1-9]+)")) {
                        break;
                    }
                    opt.write((line+"\r\n").getBytes("Cp1252"));
                }
                linenum++;
            }
            fr.close();
            br.close();

            System.out.println("File uploaded - bytes total.");

            httpExchange.getResponseBody().close();
        }
    }
}
