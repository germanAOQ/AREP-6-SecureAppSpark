package edu.escuelaing.arep.securespark.connection;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SecureURLReader {
    public static void main(String[] args) throws KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException, CertificateException {
        // Create a file and a password representation
        File trustStoreFile = new File("keystores/myTrustStore");
        char[] trustStorePassword = "abcdef".toCharArray();

        // Load the trust store, the default type is "pkcs12", the alternative is "jks"
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(new FileInputStream(trustStoreFile), trustStorePassword);

        // Get the singleton instance of the TrustManagerFactory
        TrustManagerFactory tmf = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());

        // Itit the TrustManagerFactory using the truststore object
        tmf.init(trustStore);

        //Set the default global SSLContext so all the connections will use it
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
        SSLContext.setDefault(sslContext);

        // We can now read this URL
        //readURL("https://localhost:5000/actualtime");

        // This one can't be read because the Java default truststore has been
        // changed.
        //readURL("https://www.google.com");

        readURL("https://ec2-34-207-211-53.compute-1.amazonaws.com:4567/actualtime");
    }

    public static String readURL(String site) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, KeyManagementException {

        File trustStoreFile = new File("keystores/myTrustStore");
        char[] trustStorePassword = "abcdef".toCharArray();

        // Load the trust store, the default type is "pkcs12", the alternative is "jks"
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(new FileInputStream(trustStoreFile), trustStorePassword);

        // Get the singleton instance of the TrustManagerFactory
        TrustManagerFactory tmf = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());

        // Itit the TrustManagerFactory using the truststore object
        tmf.init(trustStore);

        //Set the default global SSLContext so all the connections will use it
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
        SSLContext.setDefault(sslContext);


        // Crea el objeto que representa una URL
        URL siteURL = new URL(site);
        // Crea el objeto que URLConnection
        URLConnection urlConnection = siteURL.openConnection();
        // Obtiene los campos del encabezado y los almacena en un estructura Map
        Map<String, List<String>> headers = urlConnection.getHeaderFields();
        // Obtiene una vista del mapa como conjunto de pares <K,V> // para poder navegarlo
        Set<Map.Entry<String, List<String>>> entrySet = headers.entrySet();
        // Recorre la lista de campos e imprime los valores
        String inputLine = null;
        String res = null;
        for (Map.Entry<String, List<String>> entry : entrySet) {
            String headerName = entry.getKey();
            //Si el nombre es nulo, significa que es la linea de estado
            if (headerName != null) {
                //System.out.print(headerName + ":");
            }
            List<String> headerValues = entry.getValue();
            for (String value : headerValues) {
                //System.out.print(value);
            }
            //System.out.println("");
        }
        try {
            BufferedReader reader
                    = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            while ((inputLine = reader.readLine()) != null) {
                //System.out.println(inputLine);
                if(inputLine != null){
                    res = inputLine;
                }
            }
        } catch (IOException x) {
            System.err.println(x);
        }
        return res;
    }
}
