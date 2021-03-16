package edu.escuelaing.arep.securespark.service;
import static spark.Spark.*;

public class HelloService {

    public static void main(String[] args){
        port(5000);
        get("/helloservice", (req,res)->{
            return "Hello from insecure service";
        });
    }
}
