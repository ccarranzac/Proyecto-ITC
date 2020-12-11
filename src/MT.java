import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MT {
    ArrayList estados;
    String estado_inicial;
    String estado_aceptacion;
    ArrayList alfabeto_entrada;
    ArrayList alfabeto_cinta;
    ArrayList transiciones;
    ArrayList items;

    public MT(ArrayList estados, String estado_inicial, String estado_aceptacion, ArrayList alfabeto_entrada, ArrayList alfabeto_cinta, ArrayList transiciones) {
        this.estados = estados;
        this.estado_inicial = estado_inicial;
        this.estado_aceptacion = estado_aceptacion;
        this.alfabeto_entrada = alfabeto_entrada;
        this.alfabeto_cinta = alfabeto_cinta;
        this.transiciones = transiciones;
    }

    public MT(String NombreArchivo){
        File fichero = new File(NombreArchivo);
        Scanner s = null;

        ArrayList estados_f = new ArrayList();
        String estado_inicial_f = null;
        String estado_aceptacion_f = null;
        ArrayList alfabeto_entrada_f = new ArrayList();
        ArrayList alfabeto_cinta_f = new ArrayList();
        ArrayList transiciones_f = new ArrayList();
        ArrayList items_f = new ArrayList(); //para pruebas

        try {
            s = new Scanner(fichero);

            while (s.hasNextLine()) {
                String linea = s.nextLine();    // Guardamos la linea en un String
                items_f.add(linea);
            }

        } catch (Exception ex) {
            System.out.println("Mensaje: " + ex.getMessage());
        } finally {
            // Cerramos el fichero tanto si la lectura ha sido correcta o no
            try {
                if (s != null)
                    s.close();
            } catch (Exception ex2) {
                System.out.println("Mensaje 2: " + ex2.getMessage());
            }
        }

        this.items = items_f;

        int pos_estados = 0;
        int pos_estado_inicial = 0;
        int pos_aceptacion = 0;
        int pos_alfabeto_entrada = 0;
        int pos_alfabeto_cinta = 0;
        int pos_transiciones = 0;

        for(int i = 0; i < items_f.size(); i++){
            String elemento = (String) items_f.get(i);
            if(elemento.equals("#states")){
                pos_estados=i;
            }else if(elemento.equals("#initial")){
                pos_estado_inicial=i;
            }else if(elemento.equals("#accepting")){
                pos_aceptacion=i;
            }else if(elemento.equals("#inputAlphabet")){
                pos_alfabeto_entrada=i;
            }else if(elemento.equals("#tapeAlphabet")){
                pos_alfabeto_cinta=i;
            }else if(elemento.equals("#transitions")){
                pos_transiciones=i;
            }
        }

        //estados
        for(int i=pos_estados+1;i<pos_estado_inicial;i++){
            estados_f.add(items_f.get(i));
        }
        //estado inicial
        estado_inicial_f= (String) items_f.get(pos_estado_inicial+1);
        //estado aceptacion
        estado_aceptacion_f=(String) items_f.get(pos_aceptacion+1);
        //alfabeto de entrada
        for(int i=pos_alfabeto_entrada+1;i<pos_alfabeto_cinta;i++){
            alfabeto_entrada_f.add(items_f.get(i));
        }
        //alfabeto de cinta
        for(int i=pos_alfabeto_cinta+1;i<pos_transiciones;i++){
            alfabeto_cinta_f.add(items_f.get(i));
        }
        //transiciones
        for (int i = pos_transiciones + 1; i < items_f.size(); i++) {
            transiciones_f.add(items_f.get(i));
        }

        this.estados = estados_f;
        this.estado_inicial = estado_inicial_f;
        this.estado_aceptacion = estado_aceptacion_f;
        this.alfabeto_entrada = alfabeto_entrada_f;
        this.alfabeto_cinta = alfabeto_cinta_f;
        this.transiciones = transiciones_f;

    }

    public void esAceptada(String cadena){
        String[] Sigma = new String[alfabeto_entrada.size()];//{"a","b","c"};
        String[] GammaS = new String[alfabeto_entrada.size()];//{"X","Y","Z"};
        String[] Gamma = Sigma;
        String[] Q = new String[estados.size()];//{"q0","q1","q2","q3","q4","q5"};
        String q_0 = estado_inicial;
        String q_a = estado_aceptacion;
        //La matriz de transiciones tiene en sus columnas las transiciones para
        //las letras de Sigma, luego de GammaS (es decir Gamma), en la ultima
        //columna la transicion de la caja y en columnas los estados.
        String[] delta = new String[transiciones.size()];/*{"q0:a?q1:X:>","q0:Y?q4:Y:>","q0:!?q5:!:-",
                "q1:a?q1:a:>","q1:b?q2:Y:>","q1:Y?q1:Y:>",
                "q2:b?q2:b:>","q2:c?q3:Z:<","q2:Z?q2:Z:>",
                "q3:a?q3:a:<","q3:b?q3:b:<","q3:X?q0:X:>","q3:Y?q3:Y:<","q3:Z?q3:Z:<",
                "q4:Y?q4:Y:>","q4:Z?q4:Z:>","q4:!?q5:!:-"};*/
        String Cinta = "";

        delta= (String[]) transiciones.toArray(delta);

        String estado = q_0;
        int posicion = 1;
        int supervisor = 0; // lo usare para verificar si una letra sa sale del alfabeto
        String status = "Accepted";
        String Procesamiento = "";

        Cinta = "!" + cadena + "!";

        String Configuracion = Cinta.substring(0,posicion)+estado+Cinta.substring(posicion);
        Procesamiento = Configuracion;

        while (!estado.equals(q_a)){
            for (String delta1 : delta) {
                for (int j = 2; j < delta1.length(); j++) {
                    if ("?".equals(delta1.substring(j, j+1))) {
                        if ((estado.equals(delta1.substring(0, j-2)))&&((Cinta.substring(posicion,posicion+1)).equals(delta1.substring(j-1,j)))){
                            estado = delta1.substring(j+1,delta1.length()-4);
                            Cinta = Cinta.substring(0,posicion)+delta1.substring(delta1.length()-3,delta1.length()-2)+Cinta.substring(posicion+1);
                            if(">".equals(delta1.substring(delta1.length()-1))){
                                posicion = posicion+1;
                            }
                            if("<".equals(delta1.substring(delta1.length()-1))){
                                posicion = posicion -1;
                            }
                            supervisor = 0;
                            Configuracion = Cinta.substring(0,posicion)+estado+Cinta.substring(posicion);
                            Procesamiento = Procesamiento + "->" + Configuracion;
                        }
                    }
                }
                supervisor = supervisor + 1;
                if(supervisor == delta.length+1){
                    status = "Rejected";
                    System.out.println(Procesamiento + "(" + status + ")");
                        return;
                }
            }
        }
        System.out.println("Procesamiento:");
        System.out.println(Procesamiento + "(" + status + ")");
    }

    public void procesarListaCadenas(String []cadenas, String nombreArchivo,boolean imprimirPantalla) throws IOException {
        String[] Sigma = new String[alfabeto_entrada.size()];//{"a","b","c"};
        String[] GammaS = new String[alfabeto_entrada.size()];//{"X","Y","Z"};
        String[] Gamma = Sigma;
        String[] Q = new String[estados.size()];//{"q0","q1","q2","q3","q4","q5"};
        String q_0 = estado_inicial;
        String q_a = estado_aceptacion;
        //La matriz de transiciones tiene en sus columnas las transiciones para
        //las letras de Sigma, luego de GammaS (es decir Gamma), en la ultima
        //columna la transicion de la caja y en columnas los estados.
        String[] delta = new String[transiciones.size()];/*{"q0:a?q1:X:>","q0:Y?q4:Y:>","q0:!?q5:!:-",
                "q1:a?q1:a:>","q1:b?q2:Y:>","q1:Y?q1:Y:>",
                "q2:b?q2:b:>","q2:c?q3:Z:<","q2:Z?q2:Z:>",
                "q3:a?q3:a:<","q3:b?q3:b:<","q3:X?q0:X:>","q3:Y?q3:Y:<","q3:Z?q3:Z:<",
                "q4:Y?q4:Y:>","q4:Z?q4:Z:>","q4:!?q5:!:-"};*/
        String Cinta = "";

        delta= (String[]) transiciones.toArray(delta);


        String ruta="E:\\IntellijProjects\\repositorios git\\PROYECTO-ITC\\salida\\"+nombreArchivo+".txt";
        if(imprimirPantalla){
            File archivo = new File(ruta);
            BufferedWriter bw;
            bw = new BufferedWriter(new FileWriter(archivo));
            for(int h=0;h<cadenas.length;h++){
                String estado = q_0;
                int posicion = 1;
                int supervisor = 0; // lo usare para verificar si una letra sa sale del alfabeto
                String status = "Accepted";
                String Procesamiento = "";
                Cinta = "!" + cadenas[h] + "!";
                System.out.println(cadenas[h]);
                bw.write(cadenas[h]);
                bw.newLine();
                String Configuracion = Cinta.substring(0,posicion)+estado+Cinta.substring(posicion);
                Procesamiento = Configuracion;

                while (!estado.equals(q_a)){
                    for (String delta1 : delta) {
                        for (int j = 2; j < delta1.length(); j++) {
                            if ("?".equals(delta1.substring(j, j+1))) {
                                if ((estado.equals(delta1.substring(0, j-2)))&&((Cinta.substring(posicion,posicion+1)).equals(delta1.substring(j-1,j)))){
                                    estado = delta1.substring(j+1,delta1.length()-4);
                                    Cinta = Cinta.substring(0,posicion)+delta1.substring(delta1.length()-3,delta1.length()-2)+Cinta.substring(posicion+1);
                                    if(">".equals(delta1.substring(delta1.length()-1))){
                                        posicion = posicion+1;
                                    }
                                    if("<".equals(delta1.substring(delta1.length()-1))){
                                        posicion = posicion -1;
                                    }
                                    supervisor = 0;
                                    Configuracion = Cinta.substring(0,posicion)+estado+Cinta.substring(posicion);
                                    Procesamiento = Procesamiento + "->" + Configuracion;
                                }
                            }
                        }
                        supervisor = supervisor + 1;
                        if(supervisor == delta.length+1){
                            status = "Rejected";
                            System.out.println(Procesamiento + "(" + status + ")");
                            bw.write(Procesamiento + "(" + status + ")");
                            bw.newLine();
                            bw.close();
                            System.out.println("\u001B[35m"+"ARCHIVO CREADO"+"\u001B[0m");
                            return;
                        }
                    }
                }
                System.out.println("Procesamiento:");
                System.out.println(Procesamiento + "(" + status + ")");
                bw.write("Procesamiento:");
                bw.newLine();
                bw.write(Procesamiento + "(" + status + ")");
                bw.newLine();
            }

            bw.close();
            System.out.println("\u001B[35m"+"ARCHIVO CREADO"+"\u001B[0m");
        }else{
            File archivo = new File(ruta);
            BufferedWriter bw;
            bw = new BufferedWriter(new FileWriter(archivo));
            for(int h=0;h<cadenas.length;h++){
                String estado = q_0;
                int posicion = 1;
                int supervisor = 0; // lo usare para verificar si una letra sa sale del alfabeto
                String status = "Accepted";
                String Procesamiento = "";
                Cinta = "!" + cadenas[h] + "!";
                bw.write(cadenas[h]);
                bw.newLine();
                String Configuracion = Cinta.substring(0,posicion)+estado+Cinta.substring(posicion);
                Procesamiento = Configuracion;

                while (!estado.equals(q_a)){
                    for (String delta1 : delta) {
                        for (int j = 2; j < delta1.length(); j++) {
                            if ("?".equals(delta1.substring(j, j+1))) {
                                if ((estado.equals(delta1.substring(0, j-2)))&&((Cinta.substring(posicion,posicion+1)).equals(delta1.substring(j-1,j)))){
                                    estado = delta1.substring(j+1,delta1.length()-4);
                                    Cinta = Cinta.substring(0,posicion)+delta1.substring(delta1.length()-3,delta1.length()-2)+Cinta.substring(posicion+1);
                                    if(">".equals(delta1.substring(delta1.length()-1))){
                                        posicion = posicion+1;
                                    }
                                    if("<".equals(delta1.substring(delta1.length()-1))){
                                        posicion = posicion -1;
                                    }
                                    supervisor = 0;
                                    Configuracion = Cinta.substring(0,posicion)+estado+Cinta.substring(posicion);
                                    Procesamiento = Procesamiento + "->" + Configuracion;
                                }
                            }
                        }
                        supervisor = supervisor + 1;
                        if(supervisor == delta.length+1){
                            status = "Rejected";
                            bw.write(Procesamiento + "(" + status + ")");
                            bw.newLine();
                            bw.close();
                            System.out.println("\u001B[35m"+"ARCHIVO CREADO"+"\u001B[0m");
                            return;
                        }
                    }
                }
                bw.write("Procesamiento:");
                bw.newLine();
                bw.write(Procesamiento + "(" + status + ")");
                bw.newLine();
            }

            bw.close();
            System.out.println("\u001B[35m"+"ARCHIVO CREADO"+"\u001B[0m");
        }
    }

}
