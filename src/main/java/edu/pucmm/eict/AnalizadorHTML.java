package edu.pucmm.eict;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;

public class AnalizadorHTML {

    public AnalizadorHTML() {}

    public void analizar(String html, String baseUrl, ClienteHTTP cliente)
    {
        System.out.println("\n--- Analisis del Documento HTML ---");
        System.out.println();

        Document doc = Jsoup.parse(html,baseUrl);
        System.out.println("Cantidad de lineas: " + html.lines().count());
        System.out.println("Cantidad de parrafos (<p>): " + doc.select("p").size());
        System.out.println("Cantidad de imagenes (<img>) en parrafos (<p>): " + doc.select("p img").size());

        Elements formularios = doc.select("form");
        int postCount = 0;
        int getCount = 0;
        System.out.println();
        System.out.println("Total de formularios (<form>): " + formularios.size());

        for (Element form : formularios)
        {
            String metodo = form.attr("method").toUpperCase();
            if (metodo.equalsIgnoreCase("post")) { postCount++; }
            else if (metodo.equalsIgnoreCase("get") || metodo.isEmpty()) { getCount++; }
        }

        System.out.println("Cant. formularios GET: "+getCount);
        System.out.println("Cant. formularios POST: "+postCount);

        for (Element form : formularios)
        {
            String metodo = form.attr("method").toUpperCase();

            System.out.println("--> Analizando Formulario #" + (formularios.indexOf(form) + 1));
            System.out.println("    Metodo: " + (metodo.isEmpty() ? "GET" : metodo));

            Elements inputs = form.select("input");
            System.out.println("    Inputs encontrados:");
            for (Element input : inputs)
            {
                String nombre = input.attr("name");
                String tipo = input.attr("type");
                System.out.println("    - Input: [Name: "+nombre+" | Type: "+tipo+ "]");
            }

            if (metodo.equalsIgnoreCase("post"))
            {
                try
                {
                    String matricula = "1015-3259";

                    String actionUrl = form.absUrl("action");
                    if(actionUrl.isEmpty()) actionUrl = form.attr("action"); //si absUrl falla (porque parseamos string directo), usamos el action raw

                    System.out.println("    [!] Formulario POST detectado. Enviando peticion de práctica...");
                    HttpResponse<String> respuestaPost = cliente.enviarPractica(actionUrl, matricula);

                    System.out.println("    [!] Respuesta del servidor (POST): " + respuestaPost.statusCode());
                }
                catch (IOException | InterruptedException | URISyntaxException e)
                {
                    System.out.println("    Error enviando el POST: " + e.getMessage());
                }
            }
        }
    }
}
