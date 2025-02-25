package fr.esisar.cs467.tcp;

import io.vertx.core.AbstractVerticle;


public class TcpHttpServerVerticle extends AbstractVerticle {
    @Override
    public void start(){
        vertx.createNetServer()
            .connectHandler(new Connection())
            .listen(8080)
            .onComplete(
                server -> System.out.println("Le serveur écoute sur le port... : " + server.actualPort()), 
                serverFail -> System.out.println("Impossible de démarrer le serveur : " + serverFail.getMessage())
            );
    }
}