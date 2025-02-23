package fr.esisar.cs467.http;

import io.vertx.core.AbstractVerticle;

public class HttpServerVerticle extends AbstractVerticle{

    @Override
    public void start(){
        vertx.createHttpServer()
            .requestHandler(new HttpServerRequestHandler())
            .listen(8080)
            .onComplete(
                server -> System.out.println("Server listening on port : " + server.actualPort()), 
                serverFail -> System.out.println("Unable to start server : " + serverFail.getMessage())
            );
    }
    
}