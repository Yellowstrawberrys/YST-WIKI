package cf.thdisstudio.ystwiki.Main;

import cf.thdisstudio.ystwiki.Web.Core;
import cf.thdisstudio.ystwiki.Web.Data;
import cf.ystapi.Logging.Logger;
import cf.ystapi.Logging.LoggingBuilder;

import java.io.File;
import java.io.IOException;

public class Main {

    public static boolean isFirstRun = false;
    public static Logger logger;

    public static void main(String[] args) throws IOException {
        logger = new LoggingBuilder().build("Wiki");
        logger.info("Starting YST WIKI");
        logger.info("Checking Config File");
        if(!new File("./server.conf").exists()) {
            isFirstRun = true;
            logger.warn("Config File has been not found. Creating a new one");
            logger.info("Setting FirstRun as true");
        }else{
            logger.info("Config File has been found");
        }
        Core core = new Core();
        core.init("/", 8080);
        Data.init();
    }

}
