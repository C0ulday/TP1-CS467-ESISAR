package fr.esisar.cs467.tcp;

import fr.esisar.cs467.http.HttpServerRequestHandler;
import io.vertx.core.AbstractVerticle;


public class TcpHttpServerVerticle extends AbstractVerticle {
	@Override
	public void start() {
		
		vertx.createNetServer()
        .connectHandler(new HttpServerRequestHandler())
        .listen(8080)
        .onComplete(
            server -> System.out.println("Server listening on port : " + server.actualPort()), 
            serverFail -> System.out.println("Unable to start server : " + serverFail.getMessage())
        );

	}
}