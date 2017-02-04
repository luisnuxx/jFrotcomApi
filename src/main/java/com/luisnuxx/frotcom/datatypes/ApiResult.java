package com.luisnuxx.frotcom.datatypes;

/**
 * Created by luisnuxx on 11/10/2016.
 */
public class ApiResult {


    private Integer httpStatusCode;
    private String httpBody;
    public ApiResult() {
        // just pass
    }

    public ApiResult(Integer httpStatusCode,String httpBody) {
        if(httpStatusCode != null) {
            this.setHttpStatusCode(httpStatusCode);
        }
        if(httpBody != null && httpBody.toString().length()>0) {
            this.setHttpBody(httpBody);
        }
    }
    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(Integer httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getHttpBody() {
        return httpBody;
    }

    public void setHttpBody(String httpBody) {
        this.httpBody = httpBody;
    }
}
