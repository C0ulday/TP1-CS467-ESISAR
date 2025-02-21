package fr.esisar.cs467.tcp;

import java.util.HashMap;
import java.util.Map;

import io.vertx.core.http.HttpMethod;

public class Request {

	private String httpVersion;
	private HttpMethod method;
	private String path;
	private Map<String, String> headers = new HashMap<>();
	private String body;

	public Request(String httpVersion, HttpMethod method, String path, Map<String, String> headers, String body) {
		this.httpVersion = httpVersion;
		this.method = method;
		this.path = path;
		this.headers = headers;
		this.body = body;
	}
	
	public Request() {
		this.httpVersion = "HTTP/1.1";
		this.method = HttpMethod.GET;
		this.path = "";
		this.headers = new HashMap<>();
		this.body = "";
	}
	
	public String getHttpVersion() {
		return httpVersion;
	}

	public void setHttpVersion(String httpVersion) {
		this.httpVersion = httpVersion;
	}

	public HttpMethod getMethod() {
		return method;
	}

	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}


	public void parseRequestHeaders(String request) {
		
		String[] list = request.split("\r\n");
		// Cette prmeière ligne contient dans l'ordre la méthode, le path et la version HTTP
		String[] requete = list[0].split(" ");
		// Après avoir rétirer les espaces et mis en miniscules : on convertit la méthode en objet HttpMethod
		method = HttpMethod.valueOf(requete[0].trim().toLowerCase());
		path = requete[1].trim().toLowerCase();
		httpVersion = requete[2].trim().toLowerCase();
		for (int i = 1; i < list.length; i++) {
			if (list[i].contains(": ")) {
				String[] str = list[i].split(": ");
				// On ajoute la paire key,value
				headers.put(str[0].trim().toLowerCase(), str[1].trim().toLowerCase());
			} else {
				// Si on n'a pas de séparateur on s'arrête
				break;
			}
		}
	}

}