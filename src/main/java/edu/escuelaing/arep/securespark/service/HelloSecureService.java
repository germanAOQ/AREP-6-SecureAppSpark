package edu.escuelaing.arep.securespark.service;
import static spark.Spark.*;

public class HelloSecureService {

    public static void main(String[] args){
        //API: secure(keystoreFilePath, keystorePassword, truststoreFilePath, truststorePassword);
        secure("keystores/ecikeystore.p12", "123456", null, null);
        port(getPort());
        get("/helloservice", (req,res)->{
            return "Hello from secure service";
        });
    }

    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 5000; //returns default port if heroku-port isn't set (i.e. on localhost)
    }
}
