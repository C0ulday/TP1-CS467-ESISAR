package fr.esisar.cs467.tcp;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.net.NetSocket;

public class HttpRequestHandler {

	private NetSocket socket;
	private Request request;

	public HttpRequestHandler(NetSocket socket, Request request) {
		this.socket = socket;
		this.request = request;
	}
	
	public NetSocket getSocket() {
		return socket;
	}

	public void setSocket(NetSocket socket) {
		this.socket = socket;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public void handle() {
		
		switch (request.getPath()) {
			case "/":
				getCurrentDateTime();
				break;
			default :
		    	String body = "404 Not Found";
				Map<String , String> headers = new HashMap<>() ;
				headers.put("content-length", String.valueOf(body.length()) );
				Response response = new Response(ResponseHttpCode.NOT_FOUND,headers,body);
				socket.write(response.toString());
				break;
		}
	}

	private void getCurrentDateTime() {
		
		// Création de la réponse
		LocalDateTime now =  LocalDateTime.now();
    	String body = now.toString();
    	
		Map<String , String> headers = new HashMap<>() ;
		headers.put("content-type", "text/plain");
		// Obtention de la taille du body
		headers.put("content-length", String.valueOf(body.length()) );
		Response response = new Response(ResponseHttpCode.OK,headers,body);
		socket.write(response.toString());
		
	}

	private void postEcho() {
	}

	private void postPingPong() {
	}
}