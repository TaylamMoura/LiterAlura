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

            if (response.statusCode() == 301 || response.statusCode() == 302) {
                String novaUrl = response.headers().firstValue("location").orElse("");
                if (!novaUrl.isEmpty()) {
                    if (!novaUrl.startsWith("http")) {
                        novaUrl = BASE_URL + novaUrl;
                    }
                    return obterDados(novaUrl);
                }
            }
            return response.body();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
