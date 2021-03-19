package edu.escuelaing.arep.securespark.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import edu.escuelaing.arep.securespark.model.User;
import spark.Spark;

import javax.swing.text.Document;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

import static spark.Spark.*;
import static spark.Spark.before;
import static edu.escuelaing.arep.securespark.connection.SecureURLReader.readURL;

public class HelloSecureService {

    private static boolean authenticated = false;

    public static void main(String[] args) {
        //API: secure(keystoreFilePath, keystorePassword, truststoreFilePath, truststorePassword);
        secure("keystores/ecikeystore.p12", "123456",null,null);
        System.out.println(getPort());
	    port(getPort());
        staticFiles.location("/public");

        before("/actualtime",((request, response) ->{
            String res = null;
            if(!authenticated){
                halt(401, "<h1>There isn't a session opened</h1>");
            }
        }));

        get("/helloservice", (req, res) -> {
            return "Hello from secure service";
        });
        get("/actualtime", (req, res) -> {
            System.out.println("Entré aquí");
            LocalDate localdate = new Gson().fromJson(readURL("https://ec2-34-207-211-53.compute-1.amazonaws.com:34000/actualtime"), LocalDate.class);
            System.out.println(localdate.getYear());
            //return new Gson().toJson(localdate);

            return  "<!DOCTYPE html>\n"
                    + "<html>\n"
                    + "<head>\n"
                    + "<meta charset=\"UTF-8\">\n"
                    + "<title>Title of the document</title>\n"
                    + "</head>\n"
                    + "<body>\n"
                    + "<h1>Año: "+localdate.getYear()+"</h1>\n"
                    + "<h1>Mes: "+localdate.getMonthValue()+"</h1>\n"
                    + "<h1>Dia: "+localdate.getDayOfMonth()+"</h1>\n"
                    + "</body>\n"
                    + "</html>\n";

        });

        post("/adduser", (req, res) -> {
            User user = new Gson().fromJson(req.body(), User.class);
            String passwordEncoded = getMD5Hash(user.getPassword());
            user.setPassword(passwordEncoded);
            String json = new ObjectMapper().writeValueAsString(user);
            MongoClient mongo = new MongoClient("ec2-35-175-129-50.compute-1.amazonaws.com", 27017);
            DB db = mongo.getDB("credentials");
            DBCollection collection = db.getCollection("user");
            DBObject dbObject = (DBObject) JSON.parse(json);
            collection.insert(dbObject);
            res.status(201);
            System.out.println(user.getName());
            System.out.println(user.getPassword());
            return true;
        });
        post("/loginuser", (req, res) -> {
            boolean respuesta = false;
            User user = new Gson().fromJson(req.body(), User.class);
            MongoClient mongo = new MongoClient("ec2-35-175-129-50.compute-1.amazonaws.com", 27017);
            DB db = mongo.getDB("credentials");
            DBCollection collection = db.getCollection("user");
            for(DBObject cur: collection.find()){
                boolean cond = cur.get("name").equals(user.getName()) && cur.get("password").equals(getMD5Hash(user.getPassword()));
                if(cond){
                    respuesta = true;
                }
            }
            authenticated = true;
            res.status(200);
            return respuesta;
        });
    }

    static String getMD5Hash(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String hash = DatatypeConverter.printHexBinary(digest).toUpperCase();
        return hash;
    }

    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 5000; //returns default port if heroku-port isn't set (i.e. on localhost)
    }
}
