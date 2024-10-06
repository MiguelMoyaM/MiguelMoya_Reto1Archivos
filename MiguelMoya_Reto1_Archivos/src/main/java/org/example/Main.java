package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String archivoCSV;
        String plantillaHTML;
        String carpetaSalida;

        try {
            // Archivo de rutas
            File rutas = new File("rutas.txt");

            if (rutas.exists()) {
                // Leer las rutas directamente desde el archivo
                BufferedReader reader = new BufferedReader(new FileReader(rutas));
                archivoCSV = reader.readLine();      // Primera línea: CSV
                plantillaHTML = reader.readLine();   // Segunda línea: Plantilla HTML
                carpetaSalida = reader.readLine();   // Tercera línea: Carpeta de salida
                reader.close();

                // Validamos las rutas
                if (!new File(archivoCSV).exists() || !new File(plantillaHTML).exists() || !new File(carpetaSalida).exists()) {
                    System.out.println("Hay alguna ruta que no esta correctamente escrita");
                    Scanner scanner = new Scanner(System.in);

                    System.out.println("Introduce la ruta del archivo CSV:");
                    archivoCSV = scanner.nextLine();

                    System.out.println("Introduce la ruta de la plantilla HTML:");
                    plantillaHTML = scanner.nextLine();

                    System.out.println("Introduce la ruta de la carpeta de salida:");
                    carpetaSalida = scanner.nextLine();

                    // Guardamos las rutas al nuevo archivo creado
                    BufferedWriter writer = new BufferedWriter(new FileWriter(rutas));
                    writer.write(archivoCSV + "\n");
                    writer.write(plantillaHTML + "\n");
                    writer.write(carpetaSalida + "\n");
                    writer.close();

                    scanner.close();
                }
            } else {
                // Si el archivo no existe, pedir las rutas al usuario
                System.out.println("El archivo de rutas no existe. Introduce las rutas:");
                Scanner sC = new Scanner(System.in);

                System.out.println("Introduce la ruta del archivo CSV:");
                archivoCSV = sC.nextLine();

                System.out.println("Introduce la ruta de la plantilla HTML:");
                plantillaHTML = sC.nextLine();

                System.out.println("Introduce la ruta de la carpeta de salida:");
                carpetaSalida = sC.nextLine();
                sC.close();

                // Guardar las nuevas rutas en el archivo
                BufferedWriter writer = new BufferedWriter(new FileWriter(rutas));
                writer.write(archivoCSV + "\n");
                writer.write(plantillaHTML + "\n");
                writer.write(carpetaSalida + "\n");
                writer.close();
            }

            // Leer la plantilla HTML
            String template = new String(Files.readAllBytes(Paths.get(plantillaHTML)));

            // Leer el archivo CSV
            List<String[]> peliculas = leerArchivo(archivoCSV);

            // Verificar que las rutas sean válidas
            File plantillaFile = new File(plantillaHTML);
            File directorioSalida = new File(carpetaSalida);
            File archivoCSVFile = new File(archivoCSV);

            

            if (!plantillaFile.exists()) {
                System.err.println("No se ha encontrado la plantilla HTML, escribela correctamente");
                return;
            }

            if (!archivoCSVFile.exists()) {
                System.err.println("No se ha encontrado el archivo CSV, escribela correctamente");
                return;
            }

            if (!directorioSalida.exists()) {
                directorioSalida.mkdir();
                System.out.println("No se ha encontrado la carpeta de salida, escribela correctamnete");
            }

            // Crear archivos HTML
            for (String[] pelicula : peliculas) {
                crearHTML(pelicula, template, carpetaSalida);
            }

        } catch (IOException e) {
            System.err.println("Ha ocurrido un error con los archivos: " + e.getMessage());
        }
    }

    /**
     * Leemos un archivo CSV y devolvemos una lista de arrays String donde cada array representa una linea del archivo
     * @param ruta La ruta del archivo CSV
     * @return Una lista de arrays de String, donde cada array representa una línea del archivo
     * @throws IOException Si ocurre un error al leer el archivo
     */
    public static List<String[]> leerArchivo(String ruta) throws IOException {
        List<String[]> peliculas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                peliculas.add(datos);
            }
        }
        return peliculas;
    }

    /**
     * Generamos un archivo HTML a partir de una plantilla HTML y un array de datos de una película
     * @param pelicula Contiene los datos de la película
     * @param plantilla El contenido de la plantilla HTML que se usará como base para generar el archivo final
     * @param carpetaSalida La ruta de la carpeta donde se guardara el archivo HTML generado
     * @throws IOException Si ocurre un error al leer o escribir archivos
     */
    public static void crearHTML(String[] pelicula, String plantilla, String carpetaSalida) throws IOException {
        String contenidoHTML = plantilla.replace("%%1%%", pelicula[0])
                                        .replace("%%2%%", pelicula[1])
                                        .replace("%%3%%", pelicula[2])
                                        .replace("%%4%%", pelicula[3])
                                        .replace("%%5%%", pelicula[4]);
        String nombreArchivo = pelicula[1].replace(" ", "_") + " - " + pelicula[0] + ".html";

        File archivoSalida = new File(carpetaSalida, nombreArchivo);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoSalida))) {
            writer.write(contenidoHTML);
        }
    }
}
