package com.jgarcia;

import com.jgarcia.objetos.Manga;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.descending;

public class menuBBDD {
    static Scanner scanner = new Scanner(System.in);
    static Long id; static String titulo; static String genero; static int capitulos; static double precio;
    static MongoClient mongoClient = new MongoClient("localhost",27017);
    static MongoDatabase mongoDatabase = mongoClient.getDatabase("benimaru");
    static MongoCollection mongoCollection = mongoDatabase.getCollection("mangas");
    public static void main(String[] args) {
        String opcion; boolean continuar = true; int opt = 0;

        System.out.println("¿Qué operación desea realizar?\n" + "1. Alta.\n2. Actualización.\n3. Borrado.\n" +
                "4. Búsqueda.\n5. Listar mangas.\n6. Ejemplo de consulta con agregaciones.\n7. Ejemplo de consulta con proyecciones.\n0. Salir");
        opcion = scanner.nextLine();
        while(continuar) {
            switch (opcion) {
                case "1":
                    System.out.println("Pulse: \n1. Insertar un manga.\n2. Insertar varios mangas.");
                    opt = scanner.nextInt();
                    if(opt == 1)
                        insertManga();
                    if(opt == 2)
                        insertMangas();
                    break;
                case "2":
                    System.out.println("Pulse: \n1. Actualizar un manga.\n2. Actualizar varios mangas.");
                    opt = scanner.nextInt();
                    if(opt == 1)
                        updateManga();
                    if(opt == 2)
                        updateMangas();
                    break;
                case "3":
                    System.out.println("Pulse: \n1. Eliminar un manga.\n2. Eliminar varios mangas.");
                    opt = scanner.nextInt();
                    if(opt == 1)
                        borraManga();
                    if(opt == 2)
                        borraMangas();
                    break;
                case "4":
                    System.out.println("Pulse: \n1. Buscar una manga.\n2. Buscar varios mangas.");
                    opt = scanner.nextInt();
                    if(opt == 1)
                        buscaManga();
                    if(opt == 2)
                        buscaManga();
                    break;
                case "5": listaMangas(); break;
                case "6":
                    System.out.println("Pulse intro para insertar mangas de ejemplo y continuar: ");
                    consultaAgregaciones();
                    break;
                case "7":
                    System.out.println("Pulse intro para insertar mangas de ejemplo y continuar: ");
                    consultaProyecciones();
                    break;
                case "0":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Escoja un número entre 1 y 8 para escoger una opción. 0 para cerrar el programa");
            }
            scanner.nextLine();
            System.out.println("¿Desea realizar otra operación?\n1. Sí\n2. No");
            String sino = scanner.nextLine();
            if(!sino.equals("1")) {
                continuar = false;
            }else {
                System.out.println("¿Qué operación desea realizar?\n" + "1. Alta.\n2. Actualización.\n3. Borrado.\n" +
                        "4. Búsqueda.\n5. Listar mangas.\n6. Ejemplo mapeado. \n7. Ejemplo de consulta con agregaciones.\n8. Ejemplo de consulta con proyecciones.\n0. Salir");
                opcion = scanner.nextLine();
            }
        }
        mongoClient.close();
    }

    public static void insertManga(){
        Manga manga = null;

        System.out.println("\n\nNUEVO MANGA\n");

        System.out.print("\nID: ");
        id = scanner.nextLong();
        //LIMPIAMOS EL BUFFER
        scanner.nextLine();

        System.out.print("\nTÍTULO: ");
        titulo = scanner.nextLine();

        System.out.print("\nGÉNERO: ");
        genero = scanner.nextLine();

        System.out.print("\nNÚMERO DE CAPÍTULOS: ");
        capitulos = scanner.nextInt();

        System.out.print("\nPRECIO: ");
        precio = scanner.nextDouble();

        manga = new Manga(id, titulo, genero, capitulos, precio);

        long count = mongoCollection.countDocuments(Filters.eq("_id", id));

        //Si no existe el id
        if (count == 0) {
            Document document = new Document("_id", manga.getId())
                    .append("titulo", manga.getTitulo())
                    .append("genero", manga.getGenero())
                    .append("capitulos", manga.getCapitulos())
                    .append("precio", manga.getPrecio());

            mongoCollection.insertOne(document);
            System.out.println("¡Manga añadido con éxito!");
        } else {
            System.out.println("ERROR: El id que has introducido ya existe");
        }
    }

    public static void insertMangas() {
        Manga manga = null;
        System.out.println("¿Cuántos mangas desea añadir?");
        int num = scanner.nextInt();
        scanner.nextLine();
        List<Document> docs = new ArrayList<>();
        for(int i=0; i<num; i++){
            System.out.println("\n\nNUEVO MANGA\n");
            System.out.print("\nID: ");
            id = scanner.nextLong();
            //LIMPIAMOS EL BUFFER
            scanner.nextLine();

            System.out.print("\nTÍTULO: ");
            titulo = scanner.nextLine();

            System.out.print("\nGÉNERO: ");
            genero = scanner.nextLine();

            System.out.print("\nNÚMERO DE CAPÍTULOS: ");
            precio = scanner.nextDouble();

            System.out.print("\nPRECIO: ");
            precio = scanner.nextDouble();

            manga = new Manga(id, titulo, genero, capitulos, precio);

            Document document = new Document("_id", manga.getId())
                    .append("titulo", manga.getTitulo())
                    .append("genero", manga.getGenero())
                    .append("capitulos",manga.getCapitulos())
                    .append("precio",manga.getPrecio());
            docs.add(document);
            System.out.println("");
        }
        mongoCollection.insertMany(docs);
        System.out.println("¡Mangas añadidos con éxito!");
    }

    public static void updateManga() {
        System.out.println("Introduzca el id del manga que desea actualizar: ");
        id = scanner.nextLong();
        scanner.nextLine(); //LIMPIAMOS EL BUFFER
        Document filtro = new Document("_id",id);
        // DOCUMENTO QUE GUARDA LOS CAMBIOS
        Document actualizacion = null;
        System.out.println("Escoja campo a modificar: \n1.- Titulo.\n2.- Género.\n3.- Capítulos.\n4.- Precio.");
        String opc = scanner.nextLine();
        switch (opc){
            case "1":
                System.out.println("Introduzca el nuevo titulo: ");
                titulo = scanner.nextLine();
                actualizacion = new Document("$set", new Document("titulo", titulo));
                break;
            case "2":
                System.out.println("Introduzca el nuevo género: ");
                genero = scanner.nextLine();
                actualizacion = new Document("$set", new Document("genero", genero));
                break;
            case "3": System.out.println("Introduzca nuevo numero de capítulos: ");
                capitulos = scanner.nextInt();
                scanner.nextLine();
                actualizacion = new Document("$set", new Document("capitulos", capitulos));
                break;
            case "4": System.out.println("Introduzca nuevo precio: ");
                precio = scanner.nextDouble();
                scanner.nextLine();
                actualizacion = new Document("$set", new Document("precio", precio));
                break;
        }
        mongoCollection.updateOne(filtro,actualizacion);
        System.out.println("¡Manga actualizado con éxito!");
    }

    public static void updateMangas() {
        scanner.nextLine();
        Document filtro = null; Document actualizacion = null; String valorActualizacion;

        //NO FILTRAMOS POR ID, AL SER ÚNICO, LA APLICACIÓN SÓLO SE APLICARÍA A UN REGISTRO
        System.out.println("Escoja campo de búsqueda: \n1.- Titulo.\n2.- Genero.\n3.- Capitulos.\n4.- Precio.");
        String campoFiltrado = scanner.nextLine();
        campoFiltrado.toLowerCase();

        System.out.println("Introduzca valor del campo de búsqueda");
        String campoFiltro = scanner.nextLine();

        if(campoFiltrado.equals("1")){
            filtro = new Document("titulo", campoFiltro);
        }else if(campoFiltrado.equals("2")){
            filtro = new Document("genero", campoFiltro);
        }else if(campoFiltrado.equals("3")){
            int filter = Integer.parseInt(campoFiltro);
            filtro = new Document("capitulos", filtro);
        }else{
            double filter = Double.parseDouble(campoFiltro);
            filtro = new Document("precio", filter);
        }

        System.out.println("Introduzca el campo que desea modificar: \n1.- Titulo.\n2.- Genero.\n3.- Capitulos.\n4.- Precio.");
        String campoModificar = scanner.nextLine();

        switch (campoModificar){
            case "1":
                System.out.println("Introduzca nuevo titulo de los mangas: ");
                valorActualizacion = scanner.nextLine();
                actualizacion = new Document("$set", new Document("titulo", valorActualizacion));
                break;
            case "2":
                System.out.println("Introduzca nuevo genero de los mangas: ");
                valorActualizacion = scanner.nextLine();
                actualizacion = new Document("$set", new Document("genero", valorActualizacion));
                break;
            case "3":
                System.out.println("Introduzca nuevo numero capitulos de los mangas: ");
                capitulos = scanner.nextInt();
                actualizacion = new Document("$set", new Document("capitulos", capitulos));
                break;
            case "4":
                System.out.println("¿Desea establecer el mismo precio para ambos mangas?\n1. Sí\n2. No");
                String s = scanner.nextLine();
                if(s.equals("1")) {
                    System.out.println("Introduzca nuevo precio: ");
                    double precio = scanner.nextDouble();
                    actualizacion = new Document("$set", new Document("precio", precio));
                }else {
                    System.out.println("Escriba número positivo si quiere sumar o negativo para restar al precio existente: ");
                    double precio = scanner.nextDouble();
                    actualizacion = new Document("$inc", new Document("precio", precio));
                }
                break;
        }
        UpdateResult resultado = mongoCollection.updateMany(filtro, actualizacion);
        int modificados = (int) resultado.getModifiedCount();
        if (modificados > 0)
            System.out.println("¡" +modificados + " mangas modificados correctamente!");
        else {
            System.out.println("No se ha modificado ningun manga, compruebe que el valor del campo de filtrado coincide con algún valor de campo en la base de datos.");
        }
    }

    public static void borraManga() {
        //DATOS DE ENTRADA REQUERIDOS ÚNICAMENTE PARA ID, VALOR ÚNICO
        System.out.println("Introduzca id del manga que desea eliminar: ");
        id = scanner.nextLong();
        Document documento = new Document("_id", id);
        mongoCollection.deleteOne(documento);
        System.out.println("¡Manga eliminado correctamente!");
    }

    public static void borraMangas() {
        scanner.nextLine();
        Document eliminacion = null; String toDelete = ""; DeleteResult resultado = null;
        System.out.println("Escoja campo de búsqueda de los valores de los mangas a eliminar: \n1.- Titulo.\n2.- Genero.\n3.- Capitulos.\n4.- Precio.");
        String campoCriterio = scanner.nextLine();

        if(campoCriterio.equals("1")){
            System.out.println("Introduzca titulo de los mangas que desea eliminar");
            toDelete = scanner.nextLine();
            eliminacion = new Document("titulo", toDelete);

        }else if(campoCriterio.equals("2")){
            System.out.println("Introduzca genero de los mangas que desea eliminar");
            toDelete = scanner.nextLine();
            eliminacion = new Document("genero", toDelete);

        }else if(campoCriterio.equals("3")){
            System.out.println("Introduzca capitulos de los mangas que desea eliminar");
            int aBorrar = scanner.nextInt();
            scanner.nextLine();
            eliminacion = new Document("capitulos", aBorrar);

        }else{
            System.out.println("Introduzca precio de los mangas a eliminar");
            double aBorrar = scanner.nextDouble();
            eliminacion = new Document("precio", aBorrar);

        }
        resultado = mongoCollection.deleteMany(eliminacion);
        System.out.println("!Se han eliminado "+resultado.getDeletedCount()+" mangas correctamente!" );
    }

    public static void buscaManga() {
        System.out.println("Introduzca id del manga: ");
        Long idMasc = scanner.nextLong();
        Document doc = new Document("_id", idMasc);

        //HACEMOS USO DEL MÉTODO LIMIT PARA QUE MUESTRE EL PRIMER
        FindIterable findIterable = mongoCollection.find(doc).limit(1);

        MongoCursor cursor = findIterable.iterator();

        while(cursor.hasNext()){
            doc = (Document) cursor.next();
            System.out.println(doc);
        }
    }

    public static void buscaMangas() {
        //CREACIÓN DE ARRAY DE DOCUMENTS QUE AÑADIREMOS A LA COLECCIÓN
        System.out.println("Debido a la gran cantidad de posibilidades y operadores para realizar una búsqueda compleja, añadimos los mangas " +
                "manualmente.\nComo ejemplo, a continuación se muestran aquellas mascotas cuya genero sea 'Accion' o 'Fantasía' y cuesten más de 10€.");
        scanner.nextLine();
        List<Document> documentos = new ArrayList<>();
        Document d1 = new Document("_id", 100).append("titulo", "Solo Leveling").append("genero", "Accion").append("precio", 13.99);
        Document d2 = new Document("_id", 101).append("titulo", "The Beginning After The End").append("genero", "Fantasia").append("precio", 15.99);
        Document d3 = new Document("_id", 102).append("titulo", "Tower of God").append("genero", "Fantasia").append("precio", 8.99);
        Document d4 = new Document("_id", 103).append("titulo", "Leveling With The Gods").append("genero", "Shonen").append("precio", 10.99);
        Document d5 = new Document("_id", 104).append("titlo", "Omniscient Reader Viewpoint").append("genero", "Accion").append("peso", 5.95);
        documentos.add(d1);
        documentos.add(d2);
        documentos.add(d3);
        documentos.add(d4);
        documentos.add(d5);
        //LOS INSERTAMOS A LA COLECCIÓN PARA PODER HACER LAS BÚSQUEDAS EN CASO DE QUE NO EXISTA NINGÚN DOCUMENTO
        mongoCollection.insertMany(documentos);
        System.out.println("¡Mangas añadidos correctamente!");

        Document query = new Document();
        List<Document> orConditions = new ArrayList<>();
        orConditions.add(new Document("genero", "Accion"));
        orConditions.add(new Document("genero", "Fantasia"));
        query.append("$or", orConditions);
        query.append("precio", new Document("$gt", 10));

        FindIterable findIterable = mongoCollection.find(query);

        MongoCursor cursor = findIterable.iterator();

        while(cursor.hasNext()){
            query = (Document) cursor.next();
            System.out.println(query);
        }
    }

    public static void listaMangas() {
        List<Manga> mangas = new ArrayList<>();
        // DOCUMENTO DE FILTRADO VACÍO, POR TANTO ESCOGERÁ TODOS
        FindIterable findIterable = mongoCollection.find();
        MongoCursor mongoCursor = findIterable.iterator();
        while (mongoCursor.hasNext()){
            Document doc = (Document)mongoCursor.next();
            Manga manga = new Manga();

            manga.setId(doc.getLong("_id"));
            manga.setTitulo(doc.getString("titulo"));
            manga.setGenero(doc.getString("genero"));
            manga.setCapitulos(doc.getInteger("capitulos"));
            manga.setPrecio(doc.getDouble("precio"));
            mangas.add(manga);
        }
        //MOSTRAMOS TODOS LOS MANGAS
        mangas.stream().forEach(System.out::println);
    }

    public static void consultaAgregaciones() {
        //AÑADIMOS LOS DATOS MANUALMENTE POR SI NO EXISTIERA NINGÚN REGISTRO
        scanner.nextLine();
        List<Document> documentos = new ArrayList<>();
        Document d1 = new Document("_id", 40).append("titulo", "Doom Breaker").append("genero", "Accion").append("capitulo", 123).append("precio", 12.99);
        Document d2 = new Document("_id", 41).append("titulo", "Limit Breaker").append("genero", "Fantasia").append("capitulo", 132).append("precio", 18.99);
        Document d3 = new Document("_id", 42).append("titulo", "The Challenger").append("genero", "Shonen").append("capitulo", 213).append("precio", 21.99);
        Document d4 = new Document("_id", 43).append("titulo", "Crazy Demon").append("genero", "Accion").append("capitulo", 232).append("precio", 12.99);
        Document d5 = new Document("_id", 44).append("titulo", "Hero Killer").append("genero", "Accion").append("capitulo", 312).append("precio", 15.97);
        Document d6 = new Document("_id", 45).append("titulo", "Jungle Juice").append("genero", "Fantasia").append("capitulo", 321).append("precio", 16.86);
        Document d7 = new Document("_id", 46).append("titulo", "Tomb Raider King").append("genero", "Seinen").append("capitulo", 111).append("precio", 17.97);
        documentos.add(d1);
        documentos.add(d2);
        documentos.add(d3);
        documentos.add(d4);
        documentos.add(d5);
        documentos.add(d6);
        documentos.add(d7);

        mongoCollection.insertMany(documentos);
        System.out.println("¡Mangas añadidos correctamente!");

        System.out.println("---------------- CALCULAMOS LA MEDIA DE PRECIO DE TODOS LOS MANGAS ORDENADOS POR GENERO ----------------");

        MongoCursor<Document> cursor = mongoCollection.aggregate(Arrays.asList(group("$genero", sum("sumaprecio", "$precio"), avg("mediaprecio", "$precio")))).iterator();
        while (cursor.hasNext()){
            Document doc =(Document) cursor.next();
            System.out.println(doc);
        }
    }

    public static void consultaProyecciones() {
        //AÑADIMOS LOS DATOS MANUALMENTE POR SI NO EXISTIERA NINGÚN REGISTRO
        scanner.nextLine();
        List<Document> documentos = new ArrayList<>();
        Document d1 = new Document("_id", 50).append("titulo", "Black Clover").append("genero", "Fantasia").append("capitulos", 123).append("precio", 12.99);
        Document d2 = new Document("_id", 51).append("titulo", "Mercenary Enrollment").append("genero", "School Life").append("capitulos", 231).append("precio", 18.99);
        Document d3 = new Document("_id", 52).append("titulo", "God Game").append("genero", "VR").append("capitulos", 312).append("precio", 21.99);
        Document d4 = new Document("_id", 53).append("titulo", "Second Life Ranker").append("genero", "Seinen").append("capitulos", 213).append("precio", 12.99);
        Document d5 = new Document("_id", 54).append("titulo", "Magic Emperor").append("genero", "Antihero").append("capitulos", 132).append("precio", 15.97);
        Document d6 = new Document("_id", 55).append("titulo", "Eleceed").append("genero", "Accion").append("capitulos", 321).append("precio", 16.96);
        Document d7 = new Document("_id", 56).append("titulo", "Nano Machine").append("genero", "Murim").append("capitulos", 123).append("precio", 17.95);
        documentos.add(d1);
        documentos.add(d2);
        documentos.add(d3);
        documentos.add(d4);
        documentos.add(d5);
        documentos.add(d6);
        documentos.add(d7);

        mongoCollection.insertMany(documentos);
        System.out.println("¡Mangas añadidos correctamente!");

        System.out.println("----------------MANGAS QUE CUESTAN MÁS DE 16€, ORDENADAS POR PRECIO DESCENDENTE SIN INCLUIR EL ID----------------");

        MongoCursor<Document> cursor = mongoCollection.find(gt("precio", 16)).sort(descending("precio")).projection(fields(include("titulo", "genero", "capitulos", "precio"), exclude("_id"))).iterator();

        while (cursor.hasNext()){
            Document query = (Document) cursor.next();
            System.out.println(query);
        }
    }
}
