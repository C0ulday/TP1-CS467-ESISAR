package fr.esisar.cs467.tcp;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

public class HttpRequest implements Handler<Buffer> {
	

	private StringBuilder body = new StringBuilder();
	private Request request = new Request();
	private NetSocket socket;

	public HttpRequest(NetSocket socket) {
		this.socket = socket;
	}

	public StringBuilder getBody() {
		return body;
	}

	public void setBody(StringBuilder body) {
		this.body = body;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public NetSocket getSocket() {
		return socket;
	}

	public void setSocket(NetSocket socket) {
		this.socket = socket;
	}
	

	@Override
	public void handle(Buffer buffer) {
		
		request.parseRequestHeaders(buffer.toString());
		new HttpRequestHandler(socket, request).handle(); 

	}
}