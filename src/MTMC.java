import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MTMC {
    ArrayList estados;
    String estado_inicial;
    String estado_aceptacion;
    ArrayList alfabeto_entrada;
    ArrayList alfabeto_cinta;
    ArrayList transiciones;
    ArrayList items;

    public MTMC(ArrayList estados, String estado_inicial, String estado_aceptacion, ArrayList alfabeto_entrada, ArrayList alfabeto_cinta, ArrayList transiciones) {
        this.estados = estados;
        this.estado_inicial = estado_inicial;
        this.estado_aceptacion = estado_aceptacion;
        this.alfabeto_entrada = alfabeto_entrada;
        this.alfabeto_cinta = alfabeto_cinta;
        this.transiciones = transiciones;
    }

    public MTMC(String NombreArchivo){
        File fichero = new File(NombreArchivo);
        Scanner s = null;
        ArrayList estados_f = new ArrayList();
        String estado_inicial_f = null;
        String estado_aceptacion_f = null;
        ArrayList alfabeto_entrada_f = new ArrayList();
        ArrayList alfabeto_cinta_f = new ArrayList();
        ArrayList transiciones_f = new ArrayList();
        ArrayList items_f = new ArrayList();

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
        String[] GammaS = new String[alfabeto_cinta.size()];//{"a","b","c","X"};
        String[] Gamma = Sigma;
        String[] Q = new String[estados.size()];//{"q0","q1","q2","q3","q4"};
        String q_0 = estado_inicial;
        String q_a = estado_aceptacion;

        String[] delta = new String[transiciones.size()];/*{"q0:a:!?q0:a:>:X:>","q0:b:!?q1:b:-:!:<",
                "q1:b:X?q1:b:>:X:<","q1:b:!?q2:b:-:!:>",
                "q2:b:X?q2:b:>:X:>","q2:c:!?q3:c:-:!:<",
                "q3:c:X?q3:c:>:X:<","q3:!:!?q4:!:-:!:-"};*/

        int N_Cintas = 0;

        delta= (String[]) transiciones.toArray(delta);

        for (int j = 2; j < delta[0].length(); j++){
            if (":".equals(delta[0].substring(j,j+1))){
                N_Cintas = N_Cintas +1 ;
            }
            if("?".equals(delta[0].substring(j,j+1))){
                break;
            }
        }
        String estado = q_0;

        int supervisor = 0; // lo usare para verificar si una letra sa sale del alfabeto
        String status = "Accepted";
        String Procesamiento = "";

        String[] Cinta = new String[N_Cintas];
        int[] Posicion = new int[N_Cintas];

        Cinta[0] = "!" + cadena + "!";
        Posicion[0] = 1;

        for (int j = 1; j < N_Cintas; j++){
            Cinta[j] = "!";
            Posicion[j] = 0;
        }

        String Configuracion = "";
        String Componente = "";

        for(int i = 0; i < N_Cintas; i++){
            Componente = Cinta[i].substring(0,Posicion[i])+"*"+Cinta[i].substring(Posicion[i]);
            Configuracion = Configuracion + "," + Componente;
        }

        Configuracion = "(" + estado + Configuracion + ")";
        Procesamiento = Configuracion ;


        int Coincidencias = 0;

        while (!estado.equals(q_a)){
            for (String delta1 : delta) {
                for (int j = 2; j < delta1.length(); j++) {
                    if ("?".equals(delta1.substring(j, j+1))) {
                        for (int i = 0; i < N_Cintas; i++){
                            if((Cinta[N_Cintas-i-1].substring(Posicion[N_Cintas-i-1],Posicion[N_Cintas-i-1]+1)).equals(delta1.substring(j-(2*i)-1,j-(2*i)))){
                                Coincidencias = Coincidencias + 1;
                            }
                        }
                        if((Coincidencias == N_Cintas)&&(estado.equals(delta1.substring(0,j-(2*N_Cintas))))){
                            estado = delta1.substring(j+1,delta1.length()-(4*N_Cintas));
                            for (int i = 0; i < N_Cintas; i++){
                                Cinta[N_Cintas-i-1] = Cinta[N_Cintas-i-1].substring(0,Posicion[N_Cintas-i-1])+ delta1.substring(delta1.length()-(4*i)-3,delta1.length()-(4*i)-2) + Cinta[N_Cintas-i-1].substring(Posicion[N_Cintas-i-1]+1);
                                if(">".equals(delta1.substring(delta1.length()-(4*i)-1,delta1.length()-(4*i)))){
                                    Posicion[N_Cintas-i-1] = Posicion[N_Cintas-i-1]+1;
                                }
                                if("<".equals(delta1.substring(delta1.length()-(4*i)-1,delta1.length()-(4*i)))){
                                    Posicion[N_Cintas-i-1] = Posicion[N_Cintas-i-1] -1;
                                }
                                if(Posicion[N_Cintas-i-1] == -1){
                                    Cinta[N_Cintas-i-1]= "!" + Cinta[N_Cintas-i-1];
                                    Posicion[N_Cintas-i-1] = 0;
                                }
                                if(Posicion[N_Cintas-i-1] == Cinta[N_Cintas-i-1].length()){
                                    Cinta[N_Cintas-i-1]= Cinta[N_Cintas-i-1] + "!";
                                    Posicion[N_Cintas-i-1] = Cinta[N_Cintas-i-1].length()-1;
                                }
                            }

                            supervisor = 0;

                            Configuracion = "";
                            Componente = "";

                            for(int i = 0; i < N_Cintas; i++){
                                Componente = Cinta[i].substring(0,Posicion[i])+"*"+Cinta[i].substring(Posicion[i]);
                                Configuracion = Configuracion + "," + Componente;
                            }

                            Configuracion = "(" + estado + Configuracion + ")";

                            Procesamiento = Procesamiento + "->" + Configuracion;

                        }
                        Coincidencias = 0;
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
        String[] GammaS = new String[alfabeto_cinta.size()];//{"a","b","c","X"};
        String[] Gamma = Sigma;
        String[] Q = new String[estados.size()];//{"q0","q1","q2","q3","q4"};
        String q_0 = estado_inicial;
        String q_a = estado_aceptacion;

        String[] delta = new String[transiciones.size()];/*{"q0:a:!?q0:a:>:X:>","q0:b:!?q1:b:-:!:<",
                "q1:b:X?q1:b:>:X:<","q1:b:!?q2:b:-:!:>",
                "q2:b:X?q2:b:>:X:>","q2:c:!?q3:c:-:!:<",
                "q3:c:X?q3:c:>:X:<","q3:!:!?q4:!:-:!:-"};*/

        int N_Cintas = 0;

        delta= (String[]) transiciones.toArray(delta);
        String ruta="E:\\IntellijProjects\\repositorios git\\PROYECTO-ITC\\salida\\"+nombreArchivo+".txt";
        if(imprimirPantalla){
            File archivo = new File(ruta);
            BufferedWriter bw;
            bw = new BufferedWriter(new FileWriter(archivo));
            for(int h=0;h<cadenas.length;h++){
                for (int j = 2; j < delta[0].length(); j++){
                    if (":".equals(delta[0].substring(j,j+1))){
                        N_Cintas = N_Cintas +1 ;
                    }
                    if("?".equals(delta[0].substring(j,j+1))){
                        break;
                    }
                }
                String estado = q_0;

                int supervisor = 0; // lo usare para verificar si una letra sa sale del alfabeto
                String status = "Accepted";
                String Procesamiento = "";

                String[] Cinta = new String[N_Cintas];
                int[] Posicion = new int[N_Cintas];

                Cinta[0] = "!" + cadenas[h] + "!";
                System.out.println(cadenas[h]);
                bw.write(cadenas[h]);
                bw.newLine();
                Posicion[0] = 1;

                for (int j = 1; j < N_Cintas; j++){
                    Cinta[j] = "!";
                    Posicion[j] = 0;
                }

                String Configuracion = "";
                String Componente = "";

                for(int i = 0; i < N_Cintas; i++){
                    Componente = Cinta[i].substring(0,Posicion[i])+"*"+Cinta[i].substring(Posicion[i]);
                    Configuracion = Configuracion + "," + Componente;
                }

                Configuracion = "(" + estado + Configuracion + ")";
                Procesamiento = Configuracion ;


                int Coincidencias = 0;

                while (!estado.equals(q_a)){
                    for (String delta1 : delta) {
                        for (int j = 2; j < delta1.length(); j++) {
                            if ("?".equals(delta1.substring(j, j+1))) {
                                for (int i = 0; i < N_Cintas; i++){
                                    if((Cinta[N_Cintas-i-1].substring(Posicion[N_Cintas-i-1],Posicion[N_Cintas-i-1]+1)).equals(delta1.substring(j-(2*i)-1,j-(2*i)))){
                                        Coincidencias = Coincidencias + 1;
                                    }
                                }
                                if((Coincidencias == N_Cintas)&&(estado.equals(delta1.substring(0,j-(2*N_Cintas))))){
                                    estado = delta1.substring(j+1,delta1.length()-(4*N_Cintas));
                                    for (int i = 0; i < N_Cintas; i++){
                                        Cinta[N_Cintas-i-1] = Cinta[N_Cintas-i-1].substring(0,Posicion[N_Cintas-i-1])+ delta1.substring(delta1.length()-(4*i)-3,delta1.length()-(4*i)-2) + Cinta[N_Cintas-i-1].substring(Posicion[N_Cintas-i-1]+1);
                                        if(">".equals(delta1.substring(delta1.length()-(4*i)-1,delta1.length()-(4*i)))){
                                            Posicion[N_Cintas-i-1] = Posicion[N_Cintas-i-1]+1;
                                        }
                                        if("<".equals(delta1.substring(delta1.length()-(4*i)-1,delta1.length()-(4*i)))){
                                            Posicion[N_Cintas-i-1] = Posicion[N_Cintas-i-1] -1;
                                        }
                                        if(Posicion[N_Cintas-i-1] == -1){
                                            Cinta[N_Cintas-i-1]= "!" + Cinta[N_Cintas-i-1];
                                            Posicion[N_Cintas-i-1] = 0;
                                        }
                                        if(Posicion[N_Cintas-i-1] == Cinta[N_Cintas-i-1].length()){
                                            Cinta[N_Cintas-i-1]= Cinta[N_Cintas-i-1] + "!";
                                            Posicion[N_Cintas-i-1] = Cinta[N_Cintas-i-1].length()-1;
                                        }
                                    }

                                    supervisor = 0;

                                    Configuracion = "";
                                    Componente = "";

                                    for(int i = 0; i < N_Cintas; i++){
                                        Componente = Cinta[i].substring(0,Posicion[i])+"*"+Cinta[i].substring(Posicion[i]);
                                        Configuracion = Configuracion + "," + Componente;
                                    }

                                    Configuracion = "(" + estado + Configuracion + ")";

                                    Procesamiento = Procesamiento + "->" + Configuracion;

                                }
                                Coincidencias = 0;
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
                bw.write("Procesamiento:");
                bw.newLine();
                System.out.println(Procesamiento + "(" + status + ")");
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
                for (int j = 2; j < delta[0].length(); j++){
                    if (":".equals(delta[0].substring(j,j+1))){
                        N_Cintas = N_Cintas +1 ;
                    }
                    if("?".equals(delta[0].substring(j,j+1))){
                        break;
                    }
                }
                String estado = q_0;

                int supervisor = 0; // lo usare para verificar si una letra sa sale del alfabeto
                String status = "Accepted";
                String Procesamiento = "";

                String[] Cinta = new String[N_Cintas];
                int[] Posicion = new int[N_Cintas];

                Cinta[0] = "!" + cadenas[h] + "!";
                bw.write(cadenas[h]);
                bw.newLine();
                Posicion[0] = 1;

                for (int j = 1; j < N_Cintas; j++){
                    Cinta[j] = "!";
                    Posicion[j] = 0;
                }

                String Configuracion = "";
                String Componente = "";

                for(int i = 0; i < N_Cintas; i++){
                    Componente = Cinta[i].substring(0,Posicion[i])+"*"+Cinta[i].substring(Posicion[i]);
                    Configuracion = Configuracion + "," + Componente;
                }

                Configuracion = "(" + estado + Configuracion + ")";
                Procesamiento = Configuracion ;


                int Coincidencias = 0;

                while (!estado.equals(q_a)){
                    for (String delta1 : delta) {
                        for (int j = 2; j < delta1.length(); j++) {
                            if ("?".equals(delta1.substring(j, j+1))) {
                                for (int i = 0; i < N_Cintas; i++){
                                    if((Cinta[N_Cintas-i-1].substring(Posicion[N_Cintas-i-1],Posicion[N_Cintas-i-1]+1)).equals(delta1.substring(j-(2*i)-1,j-(2*i)))){
                                        Coincidencias = Coincidencias + 1;
                                    }
                                }
                                if((Coincidencias == N_Cintas)&&(estado.equals(delta1.substring(0,j-(2*N_Cintas))))){
                                    estado = delta1.substring(j+1,delta1.length()-(4*N_Cintas));
                                    for (int i = 0; i < N_Cintas; i++){
                                        Cinta[N_Cintas-i-1] = Cinta[N_Cintas-i-1].substring(0,Posicion[N_Cintas-i-1])+ delta1.substring(delta1.length()-(4*i)-3,delta1.length()-(4*i)-2) + Cinta[N_Cintas-i-1].substring(Posicion[N_Cintas-i-1]+1);
                                        if(">".equals(delta1.substring(delta1.length()-(4*i)-1,delta1.length()-(4*i)))){
                                            Posicion[N_Cintas-i-1] = Posicion[N_Cintas-i-1]+1;
                                        }
                                        if("<".equals(delta1.substring(delta1.length()-(4*i)-1,delta1.length()-(4*i)))){
                                            Posicion[N_Cintas-i-1] = Posicion[N_Cintas-i-1] -1;
                                        }
                                        if(Posicion[N_Cintas-i-1] == -1){
                                            Cinta[N_Cintas-i-1]= "!" + Cinta[N_Cintas-i-1];
                                            Posicion[N_Cintas-i-1] = 0;
                                        }
                                        if(Posicion[N_Cintas-i-1] == Cinta[N_Cintas-i-1].length()){
                                            Cinta[N_Cintas-i-1]= Cinta[N_Cintas-i-1] + "!";
                                            Posicion[N_Cintas-i-1] = Cinta[N_Cintas-i-1].length()-1;
                                        }
                                    }

                                    supervisor = 0;

                                    Configuracion = "";
                                    Componente = "";

                                    for(int i = 0; i < N_Cintas; i++){
                                        Componente = Cinta[i].substring(0,Posicion[i])+"*"+Cinta[i].substring(Posicion[i]);
                                        Configuracion = Configuracion + "," + Componente;
                                    }

                                    Configuracion = "(" + estado + Configuracion + ")";

                                    Procesamiento = Procesamiento + "->" + Configuracion;

                                }
                                Coincidencias = 0;
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
