package edu.pucmm.eict;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main() {
        Scanner sc = new Scanner(System.in);
        ClienteHTTP cliente = new ClienteHTTP();

        System.out.print("Ingrese una URL valida: ");
        String urlInput = sc.nextLine().trim();

        try {
            URL url = new URI(urlInput).toURL();
            HttpResponse<String> respuesta = cliente.obtenerResponse(url.toURI());
            if (respuesta == null)
            {
                System.out.println("Error de conexion");
            }
            else
            {
                String tipoRecurso = respuesta.headers().firstValue("Content-Type").orElse("Desconocido");
                System.out.println("Tipo de recurso: "+tipoRecurso);

                if (tipoRecurso.contains("text/html"))
                {
                    AnalizadorHTML analizador = new AnalizadorHTML();
                    analizador.analizar(respuesta.body(),urlInput,cliente);
                }
                else { System.out.println("El recurso no es HTML, no se realizara el análisis detallado"); }
            }
        } catch (MalformedURLException | URISyntaxException | IllegalArgumentException e) {
            System.out.println("URL invalida: "+e.getMessage());
        }
    }
}
