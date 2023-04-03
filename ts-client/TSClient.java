import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.security.*;

public class TSClient {
    public static void main(String[] args) throws Exception {
        String httpsURL = "https://www.google.com";
        String truststorePath = "truststore/truststore.jks";
        String truststorePassword = "abc123";
        
        // Load the truststore
        KeyStore truststore = KeyStore.getInstance("JKS");
        truststore.load(new FileInputStream(truststorePath), truststorePassword.toCharArray());
        
        // Create an SSL context with the loaded truststore
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(truststore);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
        
        // Open an SSL connection to the server
        URL url = new URL(httpsURL);
        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
        conn.setSSLSocketFactory(sslContext.getSocketFactory());
        
        // Read the response from the server
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            System.out.println(inputLine);
        }
        in.close();
    }
}

