

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class Main {
    static boolean ArchivoCorrecto(String nombreArchivo, int opcion) {
        int longitud = nombreArchivo.length();
        if (opcion == 1) {
            if (nombreArchivo.charAt(longitud - 1) == 'a' && nombreArchivo.charAt(longitud - 2) == 'f' &&
                    nombreArchivo.charAt(longitud - 3) == 'd' && nombreArchivo.charAt(longitud - 4) == '.') {
                return true;
            }
            return false;
        } else if (opcion == 2) {
            if (nombreArchivo.charAt(longitud - 1) == 'a' && nombreArchivo.charAt(longitud - 2) == 'd' &&
                    nombreArchivo.charAt(longitud - 3) == 'p' && nombreArchivo.charAt(longitud - 4) == 'd' && nombreArchivo.charAt(longitud - 5) == '.') {
                return true;
            }
            return false;
        } else if (opcion == 3) {
            if (nombreArchivo.charAt(longitud - 1) == 'a' && nombreArchivo.charAt(longitud - 2) == 'd' &&
                    nombreArchivo.charAt(longitud - 3) == 'p' && nombreArchivo.charAt(longitud - 4) == '.') {
                return true;
            }
            return false;
        } else if (opcion == 4) {
            if (nombreArchivo.charAt(longitud - 1) == 'm' && nombreArchivo.charAt(longitud - 2) == 's' &&
                    nombreArchivo.charAt(longitud - 3) == 'm' && nombreArchivo.charAt(longitud - 4) == '.') {
                return true;
            }
            return false;
        } else if (opcion == 5) {
            if (nombreArchivo.charAt(longitud - 1) == 'm' && nombreArchivo.charAt(longitud - 2) == 't' &&
                    nombreArchivo.charAt(longitud - 3) == '.') {
                return true;
            }
            return false;
        } else if (opcion == 6) {
            if (nombreArchivo.charAt(longitud - 1) == 'm' && nombreArchivo.charAt(longitud - 2) == 't' &&
                    nombreArchivo.charAt(longitud - 3) == 't' && nombreArchivo.charAt(longitud - 4) == '.') {
                return true;
            }
            return false;
        } else if (opcion == 7) {
            if (nombreArchivo.charAt(longitud - 1) == 'm' && nombreArchivo.charAt(longitud - 2) == 't' &&
                    nombreArchivo.charAt(longitud - 3) == 't' && nombreArchivo.charAt(longitud - 4) == 'm' && nombreArchivo.charAt(longitud - 5) == '.') {
                return true;
            }
            return false;
        } else {//no se a implementado
            return false;
        }

    }



        public static void muestraContenido(String archivo) throws FileNotFoundException, IOException {
            String cadena;
            FileReader f = new FileReader(archivo);
            BufferedReader b = new BufferedReader(f);
            while((cadena = b.readLine())!=null) {
                System.out.println(cadena);
            }
            b.close();
        }

        public static void clearScreen() {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;
        int opcion;
        String nombreArchivo;
        String ruta;
        String cadena;
        boolean formato;
        boolean alfabeto_correcto;
        System.out.println("/---------------------------------------------------------------\\");
        System.out.println("|"+"\u001B[32m"+"PROYECTO ITC"+"\u001B[0m"+"                                                   |");
        while (!salir) {
            System.out.println("|---------------------------------------------------------------|");
            System.out.println("|1. AFD                                                         |");
            System.out.println("|2. AFPD                                                        |");
            System.out.println("|3. AFPN                                                        |");
            System.out.println("|4. AF2P                                                        |");
            System.out.println("|5. Máquina de Turing – Modelo Estándar                         |");
            System.out.println("|6. Máquina de Turing Modelo con una Cinta dividida en Pistas   |");
            System.out.println("|7. Máquina de Turing Modelo con Múltiples Cintas               |");
            System.out.println("|8. Máquina de Turing No Determinista                           |");
            System.out.println("|9. Salir                                                       |");
            System.out.println("\\---------------------------------------------------------------/");
            System.out.print("Escribe una de las opciones: ");
            opcion = Integer.parseInt(scanner.next());


            switch (opcion) {
                case 1:
                    clearScreen();
                    System.out.println("*********************************************");
                    System.out.println("Has seleccionado la opcion AFD");
                    System.out.println("*********************************************");
                    System.out.print("Ingrese el nombre del archivo: ");
                    nombreArchivo = scanner.next();
                    formato = ArchivoCorrecto(nombreArchivo,opcion);
                    if(formato==false){
                        System.out.println("Extensión de archivo incorrecta");
                        break;
                    }
                    System.out.println("Extensión de archivo correcto");
                    ruta="E:\\IntellijProjects\\PROYECTO-ITC\\archivos\\"+nombreArchivo;
                    System.out.println("*********************************************");
                    System.out.println("EL ARCHIVO CARGADO ES:");
                    muestraContenido(ruta);
                    System.out.println("*********************************************");
                    System.out.println("Presione ENTER para continuar...");
                    System.in.read();
                    AFD afd=new AFD(ruta);
                    System.out.print("\u001B[33m"+"desea procesar las cadenas en un archivo adicional(1) o en pantalla(ingrese cualquier otro número): "+"\u001B[0m");
                    int numero_decision=Integer.parseInt(scanner.next());
                    if(numero_decision==1){
                        System.out.print("Cuantas cadenas desea evaluar: ");
                        int cant_cadenas=Integer.parseInt(scanner.next());
                        String [] cadenas=new String[cant_cadenas];
                        for(int i=0;i<cadenas.length;i++){
                            System.out.print("Ingrese la cadena "+(i+1)+": ");
                            String cadena_item=scanner.next();
                            cadenas[i]=cadena_item;
                        }
                        System.out.print("Ingrese el nombre del archivo que donde imprimir el procesamiento de las cadenas: ");
                        String nombreArchivoSalida=scanner.next();
                        System.out.print("Desea que adicionalmente se imprima el procesamiento en consola (S/N): ");
                        String auxSalida=scanner.next();
                        boolean imprimirpantalla;
                        if(auxSalida.equals("S")){
                            imprimirpantalla=true;
                        }else{
                            imprimirpantalla=false;
                        }
                        afd.procesarListaCadenas(cadenas,nombreArchivoSalida,imprimirpantalla);
                        System.out.println("Presione ENTER para salir...");
                        System.in.read();
                        break;
                    }else{
                        System.out.print("Ingrese la cadena a evaluar: ");
                        cadena=scanner.next();
                        alfabeto_correcto=afd.alfabetoCorrecto(cadena);
                        System.out.println("/------------------------------------------\\");
                        System.out.println("|"+"\u001B[31m"+"RESULTADO"+"\u001B[0m"+"                                 |");
                        System.out.println("\\------------------------------------------/");
                        if(alfabeto_correcto){
                            System.out.println("esta cadena si pertenece al alfabeto del AFD");
                            //Aqui va el metodo para saber si la cadena es aceptada
                            boolean aceptacion=afd.esAceptada(cadena);

                        }else{
                            System.out.println("esta cadena no pertenece al alfabeto del AFD");
                        }
                        System.out.println("");
                        System.out.println("Presione ENTER para salir...");
                        System.in.read();
                    /*System.out.println(afd.items);
                    System.out.println(afd.alfabeto);
                    System.out.println(afd.estados);
                    System.out.println(afd.estado_inicial);
                    System.out.println(afd.estados_aceptacion);
                    System.out.println(afd.transiciones);*/
                        break;
                    }

                case 2:
                    clearScreen();
                    System.out.println("*********************************************");
                    System.out.println("Has seleccionado la opcion AFD");
                    System.out.println("*********************************************");
                    System.out.print("Ingrese el nombre del archivo: ");
                    nombreArchivo = scanner.next();
                    formato = ArchivoCorrecto(nombreArchivo,opcion);
                    if(formato==false){
                        System.out.println("Extensión de archivo incorrecta");
                        break;
                    }
                    System.out.println("Extensión de archivo correcto");
                    ruta="/home/tech/IdeaProjects/Proyecto-ITC/archivos/"+nombreArchivo;
                    System.out.println("*********************************************");
                    System.out.println("EL ARCHIVO CARGADO ES:");
                    muestraContenido(ruta);
                    System.out.println("*********************************************");
                    System.out.println("Presione ENTER para continuar...");
                    System.in.read();
                    AFPD afpd=new AFPD(ruta);
                    System.out.print("\u001B[33m"+"desea procesar las cadenas en un archivo adicional(1) o en pantalla(ingrese cualquier otro número): "+"\u001B[0m");
                    int numero_decisionpd=Integer.parseInt(scanner.next());
                    if(numero_decisionpd==1){
                        System.out.print("Cuantas cadenas desea evaluar: ");
                        int cant_cadenas=Integer.parseInt(scanner.next());
                        String [] cadenas=new String[cant_cadenas];
                        for(int i=0;i<cadenas.length;i++){
                            System.out.print("Ingrese la cadena "+(i+1)+": ");
                            String cadena_item=scanner.next();
                            cadenas[i]=cadena_item;
                        }
                        System.out.print("Ingrese el nombre del archivo que donde imprimir el procesamiento de las cadenas: ");
                        String nombreArchivoSalida=scanner.next();
                        System.out.print("Desea que adicionalmente se imprima el procesamiento en consola (S/N): ");
                        String auxSalida=scanner.next();
                        boolean imprimirpantalla;
                        if(auxSalida.equals("S")){
                            imprimirpantalla=true;
                        }else{
                            imprimirpantalla=false;
                        }
                        afpd.procesarListaCadenas(cadenas,nombreArchivoSalida,imprimirpantalla);
                        System.out.println("Presione ENTER para salir...");
                        System.in.read();
                        break;
                    }else{
                        System.out.print("Ingrese la cadena a evaluar: ");
                        cadena=scanner.next();
                        alfabeto_correcto=afpd.alfabetoCorrecto(cadena);
                        System.out.println("/------------------------------------------\\");
                        System.out.println("|"+"\u001B[31m"+"RESULTADO"+"\u001B[0m"+"                                 |");
                        System.out.println("\\------------------------------------------/");
                        if(alfabeto_correcto){
                            System.out.println("esta cadena si pertenece al alfabeto del AFD");
                            //Aqui va el metodo para saber si la cadena es aceptada
                            afpd.esAceptada(cadena);

                        }else{
                            System.out.println("esta cadena no pertenece al alfabeto del AFD");
                        }
                        System.out.println("");
                        System.out.println("Presione ENTER para salir...");
                        System.in.read();
                    /*System.out.println(afd.items);
                    System.out.println(afd.alfabeto);
                    System.out.println(afd.estados);
                    System.out.println(afd.estado_inicial);
                    System.out.println(afd.estados_aceptacion);
                    System.out.println(afd.transiciones);*/
                        break;
                    }
                case 3:
                    clearScreen();
                    System.out.println("*********************************************");
                    System.out.println("Has seleccionado la opcion AFPN");
                    System.out.println("*********************************************");
                    System.out.println("Ingrese el nombre del archivo: ");
                    nombreArchivo = scanner.next();
                    formato = ArchivoCorrecto(nombreArchivo,opcion);
                    if(formato==false){
                        System.out.println("Extensión de archivo incorrecta");
                        break;
                    }
                    System.out.println("Extensión de archivo correcto");
                    ruta="E:\\IntellijProjects\\PROYECTO-ITC\\archivos\\"+nombreArchivo;
                    System.out.println("*********************************************");
                    System.out.println("EL ARCHIVO CARGADO ES:");
                    muestraContenido(ruta);
                    System.out.println("*********************************************");
                    System.out.println("Presione ENTER para continuar...");
                    System.in.read();
                    break;
                case 4:
                    clearScreen();
                    System.out.println("*********************************************");
                    System.out.println("Has seleccionado la opcion AF2P");
                    System.out.println("*********************************************");
                    System.out.println("Ingrese el nombre del archivo: ");
                    nombreArchivo = scanner.next();
                    formato = ArchivoCorrecto(nombreArchivo,opcion);
                    if(formato==false){
                        System.out.println("Extensión de archivo incorrecta");
                        break;
                    }
                    System.out.println("Extensión de archivo correcto");
                    ruta="E:\\IntellijProjects\\PROYECTO-ITC\\archivos\\"+nombreArchivo;
                    System.out.println("*********************************************");
                    System.out.println("EL ARCHIVO CARGADO ES:");
                    muestraContenido(ruta);
                    System.out.println("*********************************************");
                    System.out.println("Presione ENTER para continuar...");
                    System.in.read();
                    break;
                case 5:
                    clearScreen();
                    System.out.println("*********************************************");
                    System.out.println("Has seleccionado la opcion Máquina de Turing – Modelo Estándar");
                    System.out.println("*********************************************");
                    System.out.println("Ingrese el nombre del archivo: ");
                    nombreArchivo = scanner.next();
                    formato = ArchivoCorrecto(nombreArchivo,opcion);
                    if(formato==false){
                        System.out.println("Extensión de archivo incorrecta");
                        break;
                    }
                    System.out.println("Extensión de archivo correcto");
                    ruta="E:\\IntellijProjects\\PROYECTO-ITC\\archivos\\"+nombreArchivo;
                    System.out.println("*********************************************");
                    System.out.println("EL ARCHIVO CARGADO ES:");
                    muestraContenido(ruta);
                    System.out.println("*********************************************");
                    System.out.println("Presione ENTER para continuar...");
                    System.in.read();
                    break;
                case 6:
                    clearScreen();
                    System.out.println("*********************************************");
                    System.out.println("Has seleccionado la opcion Máquina de Turing Modelo con una Cinta dividida en Pistas");
                    System.out.println("*********************************************");
                    System.out.println("Ingrese el nombre del archivo: ");
                    nombreArchivo = scanner.next();
                    formato = ArchivoCorrecto(nombreArchivo,opcion);
                    if(formato==false){
                        System.out.println("Extensión de archivo incorrecta");
                        break;
                    }
                    System.out.println("Extensión de archivo correcto");
                    ruta="E:\\IntellijProjects\\PROYECTO-ITC\\archivos\\"+nombreArchivo;
                    System.out.println("*********************************************");
                    System.out.println("EL ARCHIVO CARGADO ES:");
                    muestraContenido(ruta);
                    System.out.println("*********************************************");
                    System.out.println("Presione ENTER para continuar...");
                    System.in.read();
                    break;
                case 7:
                    clearScreen();
                    System.out.println("*********************************************");
                    System.out.println("Has seleccionado la opcion Máquina de Turing Modelo con Múltiples Cintas");
                    System.out.println("*********************************************");
                    System.out.println("Ingrese el nombre del archivo: ");
                    nombreArchivo = scanner.next();
                    formato = ArchivoCorrecto(nombreArchivo,opcion);
                    if(formato==false){
                        System.out.println("Extensión de archivo incorrecta");
                        break;
                    }
                    System.out.println("Extensión de archivo correcto");
                    ruta="E:\\IntellijProjects\\PROYECTO-ITC\\archivos\\"+nombreArchivo;
                    System.out.println("*********************************************");
                    System.out.println("EL ARCHIVO CARGADO ES:");
                    muestraContenido(ruta);
                    System.out.println("*********************************************");
                    System.out.println("Presione ENTER para continuar...");
                    System.in.read();
                    break;
                case 8:
                    clearScreen();
                    System.out.println("*********************************************");
                    System.out.println("Has seleccionado la opcion Máquina de Turing No Determinista");
                    System.out.println("*********************************************");
                    System.out.println("Presione ENTER para continuar...");
                    System.in.read();
                    break;
                case 9:
                    salir = true;
                    break;
                default:
                    System.out.println("Solo números entre 1 y 9");
            }

        }
    }

}
