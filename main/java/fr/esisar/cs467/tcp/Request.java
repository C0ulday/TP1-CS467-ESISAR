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

		String[] lines = request.split("\r\n");
		if (lines.length == 0) {
			throw new IllegalArgumentException("Requête vide !");
		}

		// Cette prmeière ligne contient dans l'ordre la méthode, le path et la version
		// HTTP
		String[] requete = lines[0].split(" ");
		if (requete.length < 3) {
			throw new IllegalArgumentException("Requête mal formée : " + lines[0]);
		}
		// Après avoir rétirer les espaces et mis en miniscules/maj (pas de casses) : on
		// convertit la méthode en objet HttpMethod
		method = HttpMethod.valueOf(requete[0].trim().toUpperCase());
		path = requete[1].trim().toLowerCase();
		httpVersion = requete[2].trim().toLowerCase();
		int i = 1;
		// Recherche des entêtes
		for (; i < lines.length; i++) {

			// Au cas où on a une ligne vide entre les entêtes
			if (lines[i].isEmpty())
				i++;

			if (lines[i].contains(": ")) {
				String[] str = lines[i].split(": ", 2);
				// On ajoute la paire key,value
				headers.put(str[0].trim().toLowerCase(), str[1].trim().toLowerCase());
			} else {
				// Si on n'a pas de séparateur on s'arrête
				break;
			}
		}
		// Gestion du body
		if (i < lines.length) {
			StringBuilder string = new StringBuilder();
			for (int j = 0; j < lines.length; j++) {
				string.append(lines[j]);
			}
			string.append("\r\n");
			body = string.toString();
		} else {
			body = "";
		}
	}

}