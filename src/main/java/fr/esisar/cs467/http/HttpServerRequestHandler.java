package fr.esisar.cs467.http;

import java.time.LocalDateTime;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

public class HttpServerRequestHandler implements Handler<HttpServerRequest> {

    // Méthode pour obtenir la date et l'heure actuelle
    public void getCurrentDateTime(HttpServerRequest request) {
        LocalDateTime now = LocalDateTime.now();
        HttpServerResponse response = request.response();
        response.putHeader("content-type", "text/plain")
                .setStatusCode(200)
                .send(now.toString());
    }

    // Méthode pour répondre avec "pong" si le corps de la requête contient "ping"
    public void getPong(HttpServerRequest request) {
        // Utilise bodyHandler pour récupérer le corps de la requête en cas de message trop long
        request.bodyHandler(buffer -> {
            if (buffer.toString().equals("ping")) {
                request.response()
                        .putHeader("content-type", "text/plain")
                        .setStatusCode(200)
                        .send("pong");  // Utilisation de send()
            } else {
                request.response()
                        .setStatusCode(400)
                        .send("Bad request: body doesn't contain ping");
            }
        });
    }

    
    @Override
    public void handle(HttpServerRequest request) {
        // Si la méthode est GET et le chemin est "/"
        if (request.method() == HttpMethod.GET && request.path().equals("/")) {
            getCurrentDateTime(request);
        } 
        // Si la méthode est POST et le chemin est "/ping"
        else if (request.method() == HttpMethod.POST && request.path().equals("/ping")) {
            getPong(request);
        } else {
            // Pour toute autre requête, réponse 400 Bad Request
            request.response()
                    .setStatusCode(400)
                    .send("Bad Request");
        }
    }
}
