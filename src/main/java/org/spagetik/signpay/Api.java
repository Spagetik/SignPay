package org.spagetik.signpay;

import com.google.gson.Gson;
import org.json.JSONObject;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;

public class Api {

    private static final String payUrl = "https://spworlds.ru/api/public/transactions";

    // send a POST request to the server with the following data:
    // amount
    // receiver
    // comment

    // receive a response from the server with the following data:
    // status
    // message

    public static String sendPayment(String amount, String receiver, String comment) {
        HashMap<String, String> data = new HashMap<>();
        data.put("amount", amount);
        data.put("receiver", receiver);
        data.put("comment", comment);
        HashMap<String, String> out = new HashMap<>();
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            URL url = new URL(payUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Authorization", "Bearer " + Signpay.apiToken());
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            String json = new JSONObject(data).toString();;
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getResponseCode() >= 400 ? con.getErrorStream() : con.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                String outJson = response.toString();
                if (outJson.length() > 0) {
                    out = new Gson().fromJson(outJson, HashMap.class);
                    if (con.getResponseCode() >= 400) {
                        System.out.println("Error: " + out.get("message"));
                        return "Возникла ошибка: " + out.get("message");
                    }
                }
                else if (con.getResponseCode() == 200) {
                    System.out.println("Success");
                    return "Оплата прошла успешно!";
                }
            }
        } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }
}
