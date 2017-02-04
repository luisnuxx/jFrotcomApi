package jFrotcomApi;

import com.luisnuxx.frotcom.FrotcomApi;
import com.luisnuxx.frotcom.utils.FileSystem;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.HashMap;

public class Main  {
    private static final Logger LOGGER = LogManager.getLogger();
    public static void main(String[] args) {

        HashMap<String, String> hmap =  FileSystem.getConfigSettings("/Users/luisnuxx/.luisnuxx/jFrotcomApi.ini");

        LOGGER.debug("Username:: {} Provider:: {}", hmap.get("username"),hmap.get("provider"));
        FrotcomApi api = new FrotcomApi(hmap.get("username"),hmap.get("password"),hmap.get("provider"));

        try {
            LOGGER.debug("Authenticating");
            if(api.Authenticate()) {
                LOGGER.debug("Authenticated:: Current token :: {}", api.getApiToken());
            }

        } catch (Exception e) {
            LOGGER.debug("Error authenticating");
        }
        LOGGER.debug("List vehicles - token valid");
        api.demoListV(api.getApiToken());


        if(api.isTokenValid()) {
            LOGGER.debug("Token validated:: Current token :: {} " , api.getApiToken());
        } else {
            LOGGER.debug("Token invalid:: Current token :: {} " , api.getApiToken());
        }

        api.Logout();

        LOGGER.debug("List vehicles - token invalid");
        api.demoListV(api.getApiToken());

    }





}
