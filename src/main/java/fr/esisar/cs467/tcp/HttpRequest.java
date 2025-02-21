package fr.esisar.cs467.tcp;

import java.util.HashMap;
import java.util.Map;

import io.netty.handler.codec.http.HttpMethod;
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
		
		Map<String, String> headers = new HashMap<>();
		
		String data = buffer.toString();
		String[] parts = data.split("\r\n\r\n");
		String header = parts[0];
		String bodyPart = parts[1];
		
		// Première ligne de la requête
		String[] lines = header.split("\r\n");
		if (lines.length > 0) {
			String requestLine = lines[0];
            String[] requestLineParts = requestLine.split(" ");
            request.setMethod(HttpMethod.valueOf(requestLineParts[0]));        
            request.setPath(requestLineParts[1]);  
            request.setHttpVersion(requestLineParts[2]);
		}
		// Les key,value du header
		for (int i = 1; i < lines.length; i++) {
            String headerLine = lines[i];
            int colonIndex = headerLine.indexOf(":");
            if (colonIndex != -1) {
                String headerName = headerLine.substring(0, colonIndex).trim();
                String headerValue = headerLine.substring(colonIndex + 1).trim();
                headers.put(headerName, headerValue);
            }
		}
		// Répondre au client
        String response = "HTTP/1.1 200 OK\r\nContent-Length: 0\r\n\r\n";
        socket.write(response);

        // Réinitialiser le corps et la requête après avoir répondu
        body.setLength(0);
        request = new Request();
	}

}