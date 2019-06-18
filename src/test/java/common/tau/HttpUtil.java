package common.tau;

import org.apache.commons.codec.binary.Base64;

import javax.annotation.Nullable;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpUtil {
    /**
     * This static block ensures that invalid ssl connections are ignored.
     * This became necessary due to untrusted internally used certs
     */
    static {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
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
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }

    public static String performHttpRequestWithBody(String targetUrl, String httpMethod, String payloadJson){
        try {
            return performHttpRequestWithBody(targetUrl, httpMethod, payloadJson, false);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String performHttpRequestWithoutBody(String targetUrl, String httpMethod){
        try {
            return performHttpRequestWithoutBody(targetUrl, httpMethod, false);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String prepareUrlWithGetParameters(String url, String uri, Map<String, String> parameters) {
        String getParameterString = "";
        if (parameters != null) {
            getParameterString = parameters.entrySet().
                    stream()
                    .map(param ->
                            String.format("%s=%s", param.getKey(), URLEncoder.encode(param.getValue())))
                    .collect(Collectors.joining("&", "?", ""));
        }
        return String.format("%s%s%s", url, uri, getParameterString);

    }

    //region
    private static String performHttpRequestWithoutBody(String targetUrl, String httpMethod, boolean useBasicAuth) throws IOException {
        URL url = new URL(targetUrl);
        HttpURLConnection httpsURLConnection;
        if (targetUrl.startsWith("https://")) {
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
        } else {
           httpsURLConnection = (HttpURLConnection) url.openConnection();
        }

        httpsURLConnection.setRequestMethod(httpMethod);
        if(useBasicAuth) {
            httpsURLConnection = addBasicAuthToConnection(httpsURLConnection);
        }
        httpsURLConnection.setUseCaches(false);
        InputStreamReader resultInputStreamReader = new InputStreamReader(httpsURLConnection.getInputStream());
        return new BufferedReader(resultInputStreamReader).lines().collect(Collectors.joining(""));
    }

    private static String performHttpRequestWithBody(String targetUrl, String httpMethod, String payloadJson, boolean useBasicAuth) throws IOException {
        return performHttpRequestWithBody(targetUrl, httpMethod, payloadJson, useBasicAuth, "admin", "secret");
    }

    private static String performHttpRequestWithBody(String targetUrl, String httpMethod, String payloadJson, boolean useBasicAuth, String username, String pwd) throws IOException {
        URL url = new URL(targetUrl);

        HttpURLConnection httpsURLConnection;

        if (targetUrl.startsWith("https://")) {
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
        } else {
            httpsURLConnection = (HttpURLConnection) url.openConnection();
        }
        httpsURLConnection.setRequestMethod(httpMethod);

        if(useBasicAuth) {
            httpsURLConnection = addBasicAuthToConnection(username, pwd, httpsURLConnection);
        }

        httpsURLConnection.setDoOutput(true);
        httpsURLConnection.setUseCaches(false);
        httpsURLConnection.setRequestProperty("Content-Type", "application/json");
        OutputStreamWriter out = new OutputStreamWriter(
                httpsURLConnection.getOutputStream());
        out.write(payloadJson);
        out.close();
        InputStreamReader resultInputStreamReader = new InputStreamReader(httpsURLConnection.getInputStream());
        return new BufferedReader(resultInputStreamReader).lines().collect(Collectors.joining(""));
    }


    private static void performRequestWithoutResponse(String targetUrl, String httpMethod, @Nullable String payloadJson, boolean useBasicAuth) {
        try {
            URL url = new URL(targetUrl);
            HttpURLConnection httpsURLConnection = (HttpURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod(httpMethod);
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setUseCaches(false);
            if (useBasicAuth) {
                httpsURLConnection = addBasicAuthToConnection(httpsURLConnection);
            }
            if (payloadJson != null) {
                httpsURLConnection.setRequestProperty("Content-Type", "application/json");
                OutputStreamWriter out = new OutputStreamWriter(
                        httpsURLConnection.getOutputStream());
                out.write(payloadJson);
                out.close();
            }
            httpsURLConnection.connect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static HttpURLConnection addBasicAuthToConnection(HttpURLConnection connection) {
        String username = "admin";
        String password = "secret";
        return addBasicAuthToConnection(username, password, connection);

    }

    private static HttpURLConnection addBasicAuthToConnection(String username, String password, HttpURLConnection connection) {
        String plainClientCredentials = String.format("%s:%s", username, password);
        String base64ClientCredentials = new String(Base64.encodeBase64(plainClientCredentials.getBytes(Charset.defaultCharset())), Charset.defaultCharset());
        connection.setRequestProperty("Authorization", "Basic " + base64ClientCredentials);
        return connection;
    }


}
