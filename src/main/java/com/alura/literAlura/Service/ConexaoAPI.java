package com.alura.literAlura.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConexaoAPI {
    private static final String BASE_URL = "https://gutendex.com";

    public String obterDados(String endereco) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endereco))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Se a resposta é um redirecionamento, seguir a nova URL
            if (response.statusCode() == 301 || response.statusCode() == 302) {
                String novaUrl = response.headers().firstValue("location").orElse("");
                if (!novaUrl.isEmpty()) {
                    if (!novaUrl.startsWith("http")) {
                        // Completar a URL de redirecionamento com o esquema e base URL
                        novaUrl = BASE_URL + novaUrl;
                    }
                    System.out.println("Redirecionando para: " + novaUrl);
                    return obterDados(novaUrl); // Chamada recursiva com a nova URL
                }
            }

            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Response Headers: " + response.headers());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
