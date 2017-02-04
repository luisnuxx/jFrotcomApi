package com.luisnuxx.frotcom.net;

/**
 * Created by luisnuxx on 11/10/2016.
 */

import com.luisnuxx.frotcom.datatypes.ApiResult;
import com.luisnuxx.frotcom.utils.Vault;
import com.luisnuxx.frotcom.datatypes.RequestTypes.REQUEST_TYPE;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
// DOCS
//https://hc.apache.org/httpcomponents-client-4.5.x/tutorial/html/index.html

public class RestClient {

    private final String USER_AGENT = "Mozilla/5.0";
    private static final Logger LOGGER = LogManager.getLogger();
    public RestClient() {}

    public ApiResult execute(Vault vault,REQUEST_TYPE reqType, String requestUrl,String url_params,String entity_data) throws Exception {

        String url = vault.getBaseUrl() + requestUrl;
        if(vault.getApiToken() != null && vault.getApiToken().toString().length()>0) {
            url += vault.getApiTokenParam();
        }
        if(url_params != null && url_params.length() >0) {
            url += url_params;
        }
        CloseableHttpClient client = null;
        ApiResult res = null;
        try {
            client = HttpClientBuilder.create().build();

            HttpRequestBase request = null;
            switch (reqType) {
                case GET:
                    request = new HttpGet(url);
                    break;
                case POST:
                    request = new HttpPost(url);
                    break;
                case PUT:
                    request = new HttpPut(url);
                    break;
                case DELETE:
                    request = new HttpDelete(url);
                    break;
            }

            if (entity_data != null && entity_data.length() > 0) {
                StringEntity _entity = new StringEntity(entity_data, "UTF-8");

                try {
                    if (request instanceof HttpPut)
                        ((HttpPut) request).setEntity(_entity);
                    if (request instanceof HttpPost)
                        ((HttpPost) request).setEntity(_entity);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            }

            request.addHeader("User-Agent", USER_AGENT);
            request.addHeader("content-type", "application/json");
            request.addHeader("Accept", "application/json");

            CloseableHttpResponse response = client.execute(request);
            try {
                LOGGER.debug("Sending {}  request to URL : {}",reqType.toString(), url);
                LOGGER.debug("Response Code : {}",
                        response.getStatusLine().getStatusCode());

                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

                StringBuffer result = new StringBuffer();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);

                    res = new ApiResult(response.getStatusLine().getStatusCode(),result.toString());
                }
            } finally {
                response.close();
            }
        } catch (IOException e) {
            LOGGER.error("Fatal transport error: {}" , e.getMessage());
            e.printStackTrace();
        } finally {
            // Release the connection.
            client.close();
        }
        return res;
    }

}