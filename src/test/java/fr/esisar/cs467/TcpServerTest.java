package fr.esisar.cs467;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;

@ExtendWith(VertxExtension.class)
public class TcpServerTest{

    @Test
    public void testGetCurrentDateTime(Vertx vertx, VertxTestContext testContext){

        WebClient client = WebClient.create(vertx);

        client
            .get(8080, "localhost", "/")
            .send()
            .onSuccess(response -> {
                assertEquals(200, response.statusCode());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                String body = response.bodyAsString().split("\\.")[0];
                LocalDateTime dateTime = LocalDateTime.parse(body, formatter);
                if(LocalDateTime.now().getYear() == dateTime.getYear() && LocalDateTime.now().getMonthValue() == dateTime.getMonthValue() && LocalDateTime.now().getDayOfMonth() == dateTime.getDayOfMonth()){
                    testContext.completeNow();
                }else{  
                    testContext.failNow("Data not equal");
                }
            })
            .onFailure(testContext::failNow);

    }

    @Test
    public void testPingPongSuccess(Vertx vertx, VertxTestContext testContext){

        WebClient client = WebClient.create(vertx);

        client
            .post(8080, "localhost", "/ping")
            .putHeader("Content-Type", "text/plain")
            .sendBuffer(Buffer.buffer("ping"))
            .onSuccess(response -> {
                var resp = response.bodyAsString();
                if(resp.equals("pong")){
                    testContext.completeNow();
                }else{
                    testContext.failNow("Data not equal");;
                }
            })
            .onFailure(testContext::failNow);
            
    }

    @Test
    public void testPingPongFailure(Vertx vertx, VertxTestContext testContext){

        WebClient client = WebClient.create(vertx);

        client
            .post(8080, "localhost", "/ping")
            .putHeader("Content-Type", "text/plain")
            .sendBuffer(Buffer.buffer("pong"))
            .onSuccess(response -> {
                if(response.statusCode() == 400 && !response.bodyAsString().equals("ping")){
                    testContext.completeNow();
                }else{
                    testContext.failNow("");
                }
            });
            
    }

    @Test
    public void testEcho(Vertx vertx, VertxTestContext testContext){

        WebClient client = WebClient.create(vertx);

        File file = new File("./lots_of_data.txt");
        

        try (FileInputStream fis = new FileInputStream(file)) {

            String data = new String(fis.readAllBytes());

            client
                .post(8080, "localhost", "/")
                .putHeader("Content-Type", "text/plain")
                .sendBuffer(Buffer.buffer(data))
                .onSuccess(response -> {
                    var resp = response.bodyAsString();
                    if(resp.equals(data)){
                        testContext.completeNow();
                    }else{
                        testContext.failNow("Data not equal");;
                    }
                })
                .onFailure(testContext::failNow);
        } catch (Exception e) {
            e.printStackTrace();
        }

        
            
    }
}
