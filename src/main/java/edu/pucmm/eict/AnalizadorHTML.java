package edu.pucmm.eict;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class AnalizadorHTML {

    public AnalizadorHTML() {}

    public void analizar(String html, ClienteHTTP cliente)
    {
        System.out.println("\n--- Análisis del Documento HTML ---");
        System.out.println();

        Document doc = Jsoup.parse(html);
        System.out.println("Cantidad de lineas: " + html.lines().count());
        System.out.println("Cantidad de parrafos (<p>): " + doc.select("p").size());
        System.out.println("Cantidad de imágenes (<img>) en parrafos (<p>): " + doc.select("p img").size());

        Elements formularios = doc.select("form");
        int postCount = 0;
        int getCount = 0;
        System.out.println();
        System.out.println("Total de formularios (<form>): " + formularios.size());

        ArrayList<String> infos = new ArrayList<String>(); //Esto para mostrar primero la categorización de métodos (que se determina en la iteración) y luego las informaciones

        for (Element form : formularios)
        {
            String metodo = form.attr("method").toUpperCase();

            if (metodo.equalsIgnoreCase("post")) { postCount++; }
            else if (metodo.equalsIgnoreCase("get") || metodo.isEmpty()) { getCount++; }

            infos.add("--> Analizando Formulario #" + (formularios.indexOf(form) + 1));
            infos.add("    Método: " + metodo);

            Elements inputs = form.select("input");
            infos.add("    Inputs encontrados:");
            for (Element input : inputs) {
                String nombre = input.attr("name");
                String tipo = input.attr("type");
                infos.add("    - Input: [Name: "+nombre+" | Type: "+tipo+ "]");
            }

            if (metodo.equalsIgnoreCase("post"))
            {
                try
                {
                    String matricula = "1015-3259";

                    String actionUrl = form.absUrl("action");
                    if(actionUrl.isEmpty()) actionUrl = form.attr("action"); //si absUrl falla (porque parseamos string directo), usamos el action raw

                    infos.add("    [!] Formulario POST detectado. Enviando peticion de práctica...");
                    var respuestaPost = cliente.enviarPractica(actionUrl, matricula);

                    infos.add("    [!] Respuesta del servidor (POST): " + respuestaPost.statusCode());
                }
                catch (IOException | InterruptedException e)
                {
                    infos.add("Error enviando el POST: " + e.getMessage());
                }
            }
        }

        System.out.println("Cant. formularios GET: "+getCount);
        System.out.println("Cant. formularios POST: "+postCount);

        for (String s : infos) { System.out.println(s); }
    }
}
