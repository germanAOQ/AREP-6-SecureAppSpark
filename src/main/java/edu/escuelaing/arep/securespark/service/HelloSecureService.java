package edu.escuelaing.arep.securespark.service;

import com.google.gson.Gson;

import java.time.LocalDate;

import static spark.Spark.*;
import static edu.escuelaing.arep.securespark.connection.SecureURLReader.readURL;

public class HelloSecureService {

    public static void main(String[] args) {
        //API: secure(keystoreFilePath, keystorePassword, truststoreFilePath, truststorePassword);
        secure("keystores/ecikeystore.p12", "123456", "keystores/myTrustStore", "654321");
        port(getPort());
        staticFiles.location("/public");
        get("/helloservice", (req, res) -> {
            return "Hello from secure service";
        });
        get("/actualtime", (req, res) -> {
            LocalDate localdate = new Gson().fromJson(readURL("https://localhost:4567/actualtime"), LocalDate.class);
            return new Gson().toJson(localdate);
        });
    }

    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 5000; //returns default port if heroku-port isn't set (i.e. on localhost)
    }
}
