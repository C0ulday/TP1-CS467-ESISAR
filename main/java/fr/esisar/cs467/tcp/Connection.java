package fr.esisar.cs467.tcp;

import io.vertx.core.Handler;
import io.vertx.core.net.NetSocket;

public class Connection implements Handler<NetSocket> {
	@Override
	public void handle(NetSocket socket) {
		socket.handler(new HttpRequest(socket));
	}
}