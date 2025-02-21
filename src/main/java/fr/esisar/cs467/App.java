package fr.esisar.cs467;

import fr.esisar.cs467.http.HttpServerVerticle;
import io.vertx.core.Vertx;


public class App{

    public static void main( String[] args )
    {

        System.out.println("Server is starting !");

        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new HttpServerVerticle());
        
    }

}