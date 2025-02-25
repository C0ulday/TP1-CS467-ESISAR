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

		try {

			switch (request.getPath()) {
			case "/":
				if (request.getMethod() == HttpMethod.GET) {
					getCurrentDateTime();
					
				} else if(request.getMethod() == HttpMethod.POST) {
					postEcho();
				}
				break;
				
			case "/ping":
				if (request.getMethod() == HttpMethod.POST) {
					postPingPong();
				}
				break;
			default:
				String body = "404 Not Found";
				Map<String, String> headers = new HashMap<>();
				headers.put("content-length", String.valueOf(body.length()));
				Response response = new Response(ResponseHttpCode.NOT_FOUND, headers, body);
				socket.write(response.toString())
						.onFailure(err -> System.err.println("Impossible d'écrire sur le socket: " + err.getMessage()));
				;
				break;
			}
		} catch (Exception e) {
			sendError(ResponseHttpCode.INTERNAL_SERVER_ERROR, "Serveur interne du serveur (il fait du café) ...");
		}

	}


	private void getCurrentDateTime() {

		// Création de la réponse
		LocalDateTime now = LocalDateTime.now();
		String body = now.toString();

		Map<String, String> headers = new HashMap<>();
		headers.put("content-type", "text/plain");
		// Obtention de la taille du body
		headers.put("content-length", String.valueOf(body.getBytes().length));
		Response response = new Response(ResponseHttpCode.OK, headers, body);
		writeResponse(response);

	}

	private void postEcho() {
		  // Récupération du corps envoyé par le client dans la requête
	    String echoBody = request.getBody();

	    // Préparation des en-têtes de réponse
	    Map<String, String> headers = new HashMap<>();
	    headers.put("Content-Type", "text/plain");
	    // Calcul précis de la longueur en octets du corps avec l'encodage UTF-8
	    headers.put("Content-Length", String.valueOf(echoBody.getBytes().length));

	    // Création de la réponse avec le code HTTP 200 OK
	    Response response = new Response(ResponseHttpCode.OK, headers, echoBody);

	    // Envoi de la réponse au client via le socket
	    writeResponse(response);
	}

	private void postPingPong() {

		Map<String, String> headers = new HashMap<>();
		String body;
		ResponseHttpCode code;

		if ("ping".equals(request.getBody())) {
			body = "pong";
			headers.put("content-type", "text/plain");
			code = ResponseHttpCode.OK;

		} else {
			body = "Bad request: body doesn't contain ping";
			code = ResponseHttpCode.BAD_REQUEST;
		}

		headers.put("content-length", String.valueOf(body.length()));

		Response response = new Response(code, headers, body);
		writeResponse(response);
	}
	
	private void sendError(ResponseHttpCode code, String message) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/plain");
        headers.put("Content-Length", String.valueOf(message.getBytes().length));
        
        Response response = new Response(code, headers, message);
        writeResponse(response);
    }
	
	private void writeResponse(Response response) {
	    socket.write(response.toString())
	          .onComplete(res -> socket.end());
	}
}