package com.luisnuxx.frotcom.utils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.Properties;
/**
 * Created by luisnuxx on 11/10/2016.
 */
public class Vault {

    private static final Logger LOGGER = LogManager.getLogger();
    private String user = "";
    private String passwd = "";
    private String provider = "";
    private Map<String,String> entities = new HashMap<>();
    private String base_url = "";
    private static String api_token = "";
    private static String api_token_param = "";

    public Vault() {
        this.setApiBaseUrl();
        this.rebuildEntities();
    }

    /** SETTERS AND GETTERS */

    public String getUser() {
        return user;
    }
    public void setUser(String u) {
        this.user = u;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;

    }
    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getApiToken() {
        return this.api_token.toString();
    }

    public void setApiToken(String api_key) {
        this.api_token = api_key.toString();
        this.api_token_param = "?api_key=" + api_key.toString();
        this.rebuildEntities();
    }



    public String getApiTokenParam() {
        return this.api_token_param.toString();
    }
    private void rebuildEntities(){
        this.entities.clear();
        this.entities.put("authorize", "{\"provider\":\""+this.provider+"\",\"username\":\""+this.user+"\",\"password\":\""+this.passwd+"\"}");
        this.entities.put("validate_token","{\"provider\":\""+this.provider+"\",\"token\":\""+this.api_token+"\"}");
        //map.forEach( (k,v) -> System.out.println("Key: " + k + ": Value: " + v));
    }
    public String getEntity(String key) {
        this.rebuildEntities();
        return this.entities.get(key).toString();
    }


    private void setApiBaseUrl() {
        InputStream inputStream = null;
        try {
            Properties prop = new Properties();
            String propFileName = "frotcom.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            this.base_url = prop.getProperty("base_url");
            LOGGER.debug("Setting base api url :: {}" ,this.base_url);

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("Exception: ", e);
        } catch (Exception e) {
            LOGGER.error("Exception: ", e);
        }
    }

    public String getBaseUrl() {

        return base_url;
    }

}

