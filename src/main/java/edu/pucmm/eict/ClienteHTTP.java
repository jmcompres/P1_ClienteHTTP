package edu.pucmm.eict;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

public class ClienteHTTP {
    private final HttpClient cliente;

    public ClienteHTTP()
    {
        cliente = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)  //Para que siga persiguiendo en caso de redirección
                .build();
    }

    public HttpResponse<String> obtenerResponse(URI url)
    {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)                                     //Se supone que ya viene verificada desde el main
                .timeout(Duration.of(10,SECONDS))      //Timeout por si acaso
                .GET()
                .build();
        try
        {
            return cliente.send(request, HttpResponse.BodyHandlers.ofString());  //Se envía como texto por el bodyHandler
        } catch (InterruptedException | IOException e) {
            return null;                                      //En el main esto se toma como error de conexión
        }
    }

    public HttpResponse<String> enviarPractica(String urlDestino, String matricula) throws IOException, InterruptedException, URISyntaxException
    {
        String formData = "asignatura=practica1";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlDestino))
                .header("Content-Type", "application/x-www-form-urlencoded") // Necesario para que el servidor entienda el form data
                .header("matricula-id", matricula) // Header requerido
                .POST(HttpRequest.BodyPublishers.ofString(formData))
                .build();

        System.out.println("    >> Enviando POST a: " + urlDestino);


        System.out.println("    >> --- REPORTE DE ENVIO POST ---");

        System.out.println("    >> [HEADERS]");
        request.headers().map().forEach((k, v) -> {
            System.out.println("    >>   " + k + ": " + v);
        });

        System.out.println("    >> [PARAMETROS]");
        System.out.println("    >>   " + formData);
        System.out.println("    >> -----------------------------");


        return cliente.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
