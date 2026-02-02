package edu.pucmm.eict;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ClienteHTTP cliente = new ClienteHTTP();

        System.out.println("Práctica 1: Cliente HTTP");

        while (true)
        {
            System.out.println("");
            System.out.print("Ingrese una URL válida: ");
            //String urlInput = sc.nextLine().trim();

            /*Verificar la URL antes*/
            try {
                URI url = new URI(args[0]);
                HttpResponse<String> respuesta = cliente.obtenerResponse(url);
                System.out.println("Status: "+respuesta.statusCode());
                System.out.println("Tipo de recurso: "+respuesta.headers().firstValue("Content-Type").orElse("Desconocido"));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
