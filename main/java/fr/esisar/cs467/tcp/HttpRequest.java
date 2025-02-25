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
		
	    // On choppe les trames dans le body
	    body.append(buffer.toString());

	    // On attend la fin de l'obtention des entêtes (fini par "\r\n\r\n")
	    int headerEndIndex = body.indexOf("\r\n\r\n");
	    if (headerEndIndex == -1) {
	        return;
	    }

	    // Parsing
	    request.parseRequestHeaders(body.toString());

	    // Recherche de la taille de données obtenues
	    String contentLengthHeader = request.getHeaders().get("content-length");
	    if (contentLengthHeader != null) {
	        try {
	            int expectedLength = Integer.parseInt(contentLengthHeader);
	            if (request.getBody().length() < expectedLength) {
	                // Le corps n'est pas encore complet, on attend plus de données
	                return;
	            }
	        } catch (NumberFormatException e) {
	            return;
	        }
	    }

	    // Tous les fragments ont été reçus, on traite la requête
	    new HttpRequestHandler(socket, request).handle();

	    // Réinitialiser du StringBuilder
	    body.setLength(0);
	}

}