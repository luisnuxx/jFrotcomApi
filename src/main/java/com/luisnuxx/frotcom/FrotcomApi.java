package com.luisnuxx.frotcom;

import com.luisnuxx.frotcom.datatypes.ApiResult;
import com.luisnuxx.frotcom.utils.Vault;
import com.luisnuxx.frotcom.datatypes.RequestTypes.REQUEST_TYPE;
import com.luisnuxx.frotcom.net.RestClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


import java.util.Iterator;

/**
 * Created by luisnuxx on 11/10/2016.
 */
public class FrotcomApi {
    RestClient apiClient;
    private static final Logger LOGGER = LogManager.getLogger();

    private String api_token;
    private Vault _vault;

    String _Entity;
    public FrotcomApi(String username, String password,String provider) {
        LOGGER.debug("Username:: {} Provider:: {}", username,provider);
        this._vault = new Vault();
        this._vault.setUser(username);
        this._vault.setPasswd(password);
        this._vault.setProvider(provider);
        this.apiClient = new RestClient();
    }

    public ApiResult doRequest(REQUEST_TYPE type,String request,String url_params,String entity_data) throws Exception {
        ApiResult res=null;
        try{
            if(this.api_token == null || this.isTokenValid() == false) {
                LOGGER.debug("Token INVALID:: Authenticating again");
                this.Authenticate();
            }
            if(this.api_token != null) {
                return this.apiClient.execute(this._vault,type,request,url_params,entity_data);
            }
            return res;
        } catch(Exception e) {
            throw e;
        }
    }


    public Boolean Authenticate() throws Exception{
        try {
            ApiResult res = this.apiClient.execute(
                    this._vault,
                    REQUEST_TYPE.POST,
                    "authorize",
                    null,
                    this._vault.getEntity("authorize"));
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(res.getHttpBody());
            JSONObject jsonObject = (JSONObject) obj;
            String api_token = (String) jsonObject.get("token");
            LOGGER.debug("Token:: {}" , api_token);
            this.api_token = api_token;
            this._vault.setApiToken(this.api_token);
            LOGGER.debug("Authenticate::Token in the vault:: {} " , this._vault.getApiToken());
            LOGGER.debug("Authenticate::API Token param in the vault:: {} " , this._vault.getApiTokenParam());
            return true;
        }catch(Exception e) {
            return false;
        }
    }

    public ApiResult Logout(){
        try {
            LOGGER.debug("Api Token::Code:: {}" , this._vault.getApiTokenParam().toString());
            ApiResult res = this.apiClient.execute(
                    this._vault,
                    REQUEST_TYPE.DELETE,
                    "authorize",
                    this._vault.getApiTokenParam(),
                    null);

            this.api_token = null;
            this._vault.setApiToken(null);

            LOGGER.debug("Logout::Code:: {}" , res.getHttpStatusCode().toString());
            LOGGER.debug("Logout::BodyText:: {}" , res.getHttpBody());
            return res;
        }catch(Exception e) {
            return null;
        }

    }

    public Boolean isTokenValid() {
        try {
            //ApiResult res = this.doRequest(REQUEST_TYPE.PUT, "authorize", this._vault.getApiTokenParam(), this._vault.getEntity("validate_token"));
            ApiResult res = this.apiClient.execute(
                    this._vault,
                    REQUEST_TYPE.PUT,
                    "authorize",
                    this._vault.getApiTokenParam(),
                    this._vault.getEntity("validate_token"));


            LOGGER.debug("ValidateToken::Code:: {}" , res.getHttpStatusCode().toString());
            LOGGER.debug("ValidateToken::BodyText:: {}" , res.getHttpBody());

            if (res.getHttpStatusCode() != HttpStatus.SC_OK) {
                this.api_token = null;
                this._vault.setApiToken(null);
                return false;
            } else {
                return true;
            }
        }catch(Exception e) {
            return false;
        }
    }

    /*

    END OF THE CORE METHODS


     */


    public ApiResult ListVehicles(String token) throws Exception{
        LOGGER.debug("List Vehicles token:: {}",token);
        try {

            //ApiResult res = this.doRequest(REQUEST_TYPE.GET, "vehicles", this._vault.getApiTokenParam(), null);
            ApiResult res = this.doRequest(
                    REQUEST_TYPE.GET,
                    "vehicles",
                    null,
                    null);

            LOGGER.debug("List Vehicles::Code:: {}" , this._vault.getApiTokenParam().toString());
            LOGGER.debug("List Vehicles::Code:: {}" , res.getHttpStatusCode().toString());

            return res;
        }catch(Exception e) {
            throw e;
        }

    }


    public String getApiToken() {
        return this._vault.getApiToken();
    }


    public void demoListV(String token) {

        String license_plate="";
        try {

            ApiResult res = this.ListVehicles(token);

            JSONParser parser = new JSONParser();

            Object obj = parser.parse(res.getHttpBody());
            JSONArray vehicles_list = (JSONArray) obj;
            Iterator i = vehicles_list.iterator();
            String driver_name = "";
            Integer count = 0;
            while (i.hasNext()) {
                JSONObject jsonObject = (JSONObject) i.next();
                license_plate = String.valueOf(jsonObject.get("licensePlate"));
                String id = String.valueOf(jsonObject.get("id"));

                try {
                    driver_name = String.valueOf(jsonObject.get("driverName"));
                } catch(Exception e) {
                    driver_name = "";
                }
                LOGGER.debug("{} -- {} -- {} ",id,driver_name ,license_plate);
                count++;
                if(count > 3) {
                    break;
                }
            }



        } catch (Exception e) {
            LOGGER.error("Error getting vehicles + ",license_plate);
            e.printStackTrace();
        }
    }


}
