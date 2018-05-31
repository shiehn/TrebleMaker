package com.treblemaker.utils.Http;

import com.treblemaker.Application;
import org.apache.commons.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class HttpUtils {

    private final int MIN_IN_MILS = 60 * 1000;
    private final int DEFAULT_TIMEOUT = MIN_IN_MILS * 10;

    public String sendGet(String url, int timeout) {
        return sendGet(url, null, null, timeout);
    }

    public String sendGet(String url) {
        return sendGet(url, null, null, DEFAULT_TIMEOUT);
    }

    public String sendGet(String url, String username, String password) {
        return sendGet(url, username, password, DEFAULT_TIMEOUT);
    }

    public String sendGet(String url, String username, String password, int timeout) {

        StringBuffer response = new StringBuffer();

        HttpURLConnection con = null;
        try {
            URL obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();

            Application.logger.debug("LOG: Sending 'GET' request to URL : " + url);

            if(username != null && password != null){
                String auth = username + ":" + password;
                byte[] encodedAuth = Base64.encodeBase64(
                        auth.getBytes(Charset.forName("US-ASCII")) );
                String authHeader = "Basic " + new String( encodedAuth );
                con.setRequestProperty("Authorization", authHeader);
            }

            // optional default is GET
            con.setRequestMethod("GET");
            con.setConnectTimeout(timeout);
            con.setReadTimeout(timeout);

            int responseCode = con.getResponseCode();
            Application.logger.debug("LOG: Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();

        }catch (Exception e){

            Application.logger.debug("LOG:", e);
            return "error";
        }finally {
            if(con != null){
                con.disconnect();
            }
        }
    }
}
