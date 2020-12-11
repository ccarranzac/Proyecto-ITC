import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class AF2P {
    ArrayList alfabeto;
    ArrayList alfabeto_pila;
    ArrayList estados;
    String estado_inicial;
    ArrayList estados_aceptacion;
    ArrayList transiciones;
    ArrayList items;
    String cad;

    public AF2P(ArrayList alfabeto,ArrayList alfabeto_pila, ArrayList estados, String estado_inicial, ArrayList estados_aceptacion, ArrayList transiciones) {
        this.alfabeto = alfabeto;
        this.alfabeto_pila = alfabeto_pila;
        this.estados = estados;
        this.estado_inicial = estado_inicial;
        this.estados_aceptacion = estados_aceptacion;
        this.transiciones = transiciones;
    }

    public AF2P(String NombreArchivo) {
        File fichero = new File(NombreArchivo);
        Scanner s = null;

        ArrayList alfabeto_f = new ArrayList();
        ArrayList alfabeto_pila_f = new ArrayList();
        ArrayList estados_f = new ArrayList();
        String estado_inicial_f = null;
        ArrayList estados_aceptacion_f = new ArrayList();
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

        int pos_alfabeto = 0;
        int pos_alfabeto_p = 0;
        int pos_estados = 0;
        int pos_estado_inicial = 0;
        int pos_aceptacion = 0;
        int pos_transiciones = 0;

        for (int i = 0; i < items_f.size(); i++) {
            String elemento = (String) items_f.get(i);
            if (elemento.equals("#alphabet")) {
                pos_alfabeto = i;
            }else if (elemento.equals("#alphabetP")) {
                pos_alfabeto_p = i;
            } else if (elemento.equals("#states")) {
                pos_estados = i;
            } else if (elemento.equals("#initial")) {
                pos_estado_inicial = i;
            } else if (elemento.equals("#accepting")) {
                pos_aceptacion = i;
            } else if (elemento.equals("#transitions")) {
                pos_transiciones = i;
            }
        }
        //lectura de los items de la configuración
        //alfabeto
        for (int i = pos_alfabeto + 1; i < pos_alfabeto_p; i++) {
            alfabeto_f.add(items_f.get(i));
        }
        //alfabeto pila
        for (int i = pos_alfabeto_p + 1; i < pos_estados; i++) {
            alfabeto_pila_f.add(items_f.get(i));
        }
        //estados
        for (int i = pos_estados + 1; i < pos_estado_inicial; i++) {
            estados_f.add(items_f.get(i));
        }
        //estado inicial
        estado_inicial_f = (String) items_f.get(pos_estado_inicial + 1);
        //estados aceptacion
        for (int i = pos_aceptacion + 1; i < pos_transiciones; i++) {
            estados_aceptacion_f.add(items_f.get(i));
        }
        //transiciones
        for (int i = pos_transiciones + 1; i < items_f.size(); i++) {
            transiciones_f.add(items_f.get(i));
        }

        this.alfabeto = alfabeto_f;
        this.alfabeto_pila = alfabeto_pila_f;
        this.estados = estados_f;
        this.estado_inicial = estado_inicial_f;
        this.estados_aceptacion = estados_aceptacion_f;
        this.transiciones = transiciones_f;
    }

    //verificar que la cadena ingresada pertenezca al alfabeto
    public boolean alfabetoCorrecto(String cadena) {

        char[] aCaracteres = cadena.toCharArray();
        boolean alfabetoC = true;
        ArrayList ocur=new ArrayList();

        for (int i = 0; i < aCaracteres.length; i++) {
            int ocurrencias=0;
            for (int j = 0; j < alfabeto.size(); j++) {
                String item = (String) alfabeto.get(j);
                char[] item_=item.toCharArray();
                if(aCaracteres[i]!=item_[0]){
                    ocurrencias+=1;
                }
            }
            ocur.add(ocurrencias);
        }
        //System.out.println(ocur);
        for(int i=0;i<ocur.size();i++){
            int num= (int) ocur.get(i);
            if(num >1){
                return false;
            }
        }
        return alfabetoC;
    }
    //Analiza la cadena según la configuración del automata
    public String esAceptada(String cadena){
        Stack<String> pila1 = new Stack();
        Stack<String> pila2 = new Stack();
        int tamaño=transiciones.size();
        char[] aCaracteres = cadena.toCharArray();
        String estado_actual=estado_inicial;
        boolean aceptacion=false;
        StringBuilder stack1 = new StringBuilder();
        StringBuilder stack2 = new StringBuilder();
        StringBuilder procesamiento = new StringBuilder();
        char[] stackin1=new char[tamaño];
        char[] stackin2=new char[tamaño];
        char[] stackout1=new char[tamaño];
        char[] stackout2=new char[tamaño];
        String[] estado=new String[tamaño];
        char[] cadena_estado=new char[tamaño];
        String[] sig_estado=new String[tamaño];
        int posicion1 = 0;
        int posicion2 = 0;
        Boolean computando = true;
        String resultado;

        //sepraramos los estados, y e
        for(int i=0;i<transiciones.size();i++){

            String item= (String) transiciones.get(i);
            stackin1[i] =item.charAt(5);
            stackin2[i] =item.charAt(7);
            stackout1[i] =item.charAt(12);
            stackout2[i] =item.charAt(14);

            char[] item_=item.toCharArray();
            for(int j=0;j<item_.length;j++){
                if(item_[j] == ':'){
                    posicion1=j;
                }
                else if(item_[j]=='>'){
                    posicion2=j;
                }
            }
            char[] aux=new char[posicion1];
            char[] aux2=new char[item_.length-(posicion2+5)];
            int k=0;
            for(int j=0;j<posicion1;j++){
                aux[j]=item_[j];
            }
            estado[i]=new String(aux);
            cadena_estado[i]=item_[posicion1+1];
            for(int j=posicion2+1;j<item_.length-4;j++){
                aux2[k]=item_[j];
                k++;
            }
            sig_estado[i]=new String(aux2);

        }



        pila1.push("Z");
        pila2.push("Z");
        stack1.append("$");
        stack2.append("$");
        cad= ("("+estado_actual+","+cadena+","+stack1+","+stack2+")->");
        procesamiento.append(cad);
        for(int i=0; i< aCaracteres.length;i++){
            int num=0;
            for(int j=0;j<estado.length;j++){
                if(estado_actual.equals(estado[j])) {
                    if (aCaracteres[i] == cadena_estado[j] ) {
                        num = j;

                        //insercion o eliminacion de la pila
                        for(int k=0;k<alfabeto_pila.size();k++) {
                            if ((String.valueOf(stackin1[j]).equals("$") && !(String.valueOf(stackout1[j]).equals("$"))) &&
                                    (String.valueOf(stackin2[j]).equals("$") && !(String.valueOf(stackout2[j]).equals("$")))) {
                                pila1.push(String.valueOf(stackout1[j]));
                                pila2.push(String.valueOf(stackout2[j]));
                                stack1.append(stackout1[j]);
                                stack2.append(stackout2[j]);
                                System.out.println("1");
                                break;
                            } else if (((String.valueOf(stackin1[j]).equals(alfabeto_pila.get(k))) && (pila1.peek().equals(String.valueOf(stackin1[j]))) && !(String.valueOf(stackout1[j]).equals("$")))&&
                                    ((String.valueOf(stackin2[j]).equals(alfabeto_pila.get(k))) && (pila2.peek().equals(String.valueOf(stackin2[j]))) && !(String.valueOf(stackout2[j]).equals("$")))) {
                                pila1.pop();
                                pila2.pop();
                                stack1.deleteCharAt(stack1.length()-1);
                                stack2.deleteCharAt(stack2.length()-1);
                                pila1.push(String.valueOf(stackout1[j]));
                                pila2.push(String.valueOf(stackout2[j]));
                                stack1.append(stackout1[j]);
                                stack2.append(stackout2[j]);
                                break;
                            } else if ((String.valueOf(stackin1[j]).equals("$") && (String.valueOf(stackout1[j]).equals("$"))) &&
                                    ((String.valueOf(stackin2[j]).equals(alfabeto_pila.get(k))) && (pila2.peek().equals(String.valueOf(stackin2[j]))) && !(String.valueOf(stackout2[j]).equals("$")))) {

                                pila2.pop();
                                stack2.deleteCharAt(stack2.length()-1);
                                pila2.push(String.valueOf(stackout2[j]));
                                stack2.append(stackout2[j]);
                                break;
                            } else if ((String.valueOf(stackin2[j]).equals("$") && (String.valueOf(stackout2[j]).equals("$"))) &&
                                    ((String.valueOf(stackin1[j]).equals(alfabeto_pila.get(k))) && (pila2.peek().equals(String.valueOf(stackin1[j]))) && !(String.valueOf(stackout1[j]).equals("$")))) {

                                pila1.pop();
                                stack1.deleteCharAt(stack1.length()-1);
                                pila1.push(String.valueOf(stackout1[j]));
                                stack1.append(stackout1[j]);
                                break;
                            } else if ((String.valueOf(stackin1[j]).equals("$") && (String.valueOf(stackout1[j]).equals("$"))) &&
                                    (String.valueOf(stackin2[j]).equals("$") && !(String.valueOf(stackout2[j]).equals("$")))) {

                                pila2.push(String.valueOf(stackout2[j]));
                                stack2.append(stackout2[j]);
                                break;
                            } else if ((String.valueOf(stackin2[j]).equals("$") && (String.valueOf(stackout2[j]).equals("$"))) &&
                                    (String.valueOf(stackin1[j]).equals("$") && !(String.valueOf(stackout1[j]).equals("$")))) {

                                pila1.push(String.valueOf(stackout1[j]));
                                stack1.append(stackout1[j]);
                                break;


                            } else if ((String.valueOf(stackin1[j]).equals("$") && (String.valueOf(stackout1[j]).equals("$"))) &&
                                    (String.valueOf(stackin2[j]).equals("$") && (String.valueOf(stackout2[j]).equals("$")))){
                                break;
                            }else if (((pila1.peek().equals(String.valueOf(stackin1[j]))) && (String.valueOf(stackout1[j]).equals("$"))) &&
                                    (String.valueOf(stackin2[j]).equals("$") && (String.valueOf(stackout2[j]).equals("$")))){
                                if(!pila1.empty()) {
                                    pila1.pop();
                                    stack1.deleteCharAt(stack1.length()-1);
                                    break;
                                }
                            }else if (((pila2.peek().equals(String.valueOf(stackin2[j]))) && (String.valueOf(stackout2[j]).equals("$"))) &&
                                    (String.valueOf(stackin1[j]).equals("$") && (String.valueOf(stackout1[j]).equals("$")))){
                                if(!pila2.empty()) {
                                    pila2.pop();
                                    stack2.deleteCharAt(stack2.length()-1);
                                    break;
                                }
                            }else if ((String.valueOf(stackin2[j]).equals(alfabeto_pila.get(k))  && (pila2.peek().equals(String.valueOf(stackin2[j]))) && (String.valueOf(stackout2[j]).equals("$"))) &&
                                    (String.valueOf(stackin1[j]).equals(alfabeto_pila.get(k))  && (pila1.peek().equals(String.valueOf(stackin1[j]))) && (String.valueOf(stackout1[j]).equals("$")))){
                                if(!(pila2.empty() && pila1.empty())) {
                                    pila1.pop();
                                    pila2.pop();
                                    stack1.deleteCharAt(stack1.length()-1);
                                    stack2.deleteCharAt(stack2.length()-1);
                                    break;
                                }
                            }else {
                                if (k == alfabeto.size() - 1) {
                                    computando = false;
                                }
                            }
                        }
                    }
                }
            }


            if (computando) {

                int l ;
                if (i==0) {
                    char[] auxchar=new char[aCaracteres.length-i-1];
                    l = 1;

                    for(int k=0;k<auxchar.length;k++){
                        auxchar[k]=aCaracteres[l];
                        l++;
                    }
                    if(auxchar.length!=0) {

                        estado_actual = sig_estado[num];
                        cad = ("(" + estado_actual + "," + new String(auxchar) + "," + stack1 +"," + stack2 + ")->");
                        procesamiento.append(cad);
                    }else{
                        estado_actual = sig_estado[num];
                        cad = ("(" + estado_actual + ",$" + new String(auxchar) + "," + stack1+ "," + stack2 +  ")>>");
                        procesamiento.append(cad);
                    }

                }else{

                    char[] auxchar=new char[aCaracteres.length-i-1];

                    l = i+1;
                    for(int k=0;k<auxchar.length;k++){
                        auxchar[k]=aCaracteres[l];
                        l++;
                    }
                    if(auxchar.length!=0) {

                        estado_actual = sig_estado[num];
                        cad = ("(" + estado_actual + "," + new String(auxchar) + "," + stack1  +"," + stack2 + ")->");
                        procesamiento.append(cad);
                    }else{
                        estado_actual = sig_estado[num];
                        cad = ("(" + estado_actual + ",$" + new String(auxchar) + "," + stack1 +"," + stack2 + ")>>");
                        procesamiento.append(cad);
                    }
                }

            }else{
                char[] auxchar=new char[aCaracteres.length-i];
                int l=i;
                for(int k=0;k<auxchar.length;k++){
                    auxchar[k]=aCaracteres[l];
                    l++;
                }
                cad = ("(" + estado_actual + "," + new String(auxchar) + "," + stack1 +"," + stack2 + ")>>no");
                procesamiento.append(cad);
                resultado = procesamiento.toString();
                System.out.println(procesamiento);
                return resultado;
            }
        }
        if(pila1.peek().equals("Z") && pila2.peek().equals("Z") ) {
            pila1.pop();
            pila2.pop();
        }

        for(int i=0;i<estados_aceptacion.size();i++){
            String estado_aceptacion= (String) estados_aceptacion.get(i);
            if((estado_actual.equals(estado_aceptacion))  && (pila1.empty()) && (pila2.empty())){
                aceptacion=true;
            }
        }
        if(aceptacion){
            cad =("yes");
            procesamiento.append(cad);
        }
        else{
            cad = ("no");
            procesamiento.append(cad);
        }
        System.out.println(procesamiento);
        return procesamiento.toString();
    }

    //procesamiento de varias cadenas y puedan ser guardadas en un archivo de texto
    public void procesarListaCadenas(String []cadenas, String nombreArchivo,boolean imprimirPantalla) throws IOException {

        String ruta="E:\\IntellijProjects\\repositorios git\\PROYECTO-ITC\\salida\\"+nombreArchivo+".txt";

        if(imprimirPantalla){
            File archivo = new File(ruta);
            BufferedWriter bw;
            bw = new BufferedWriter(new FileWriter(archivo));

            for(int i=0;i<cadenas.length;i++){
                bw.write(cadenas[i]);
                bw.newLine();
                System.out.println(esAceptada(cadenas[i]));
                bw.write(esAceptada(cadenas[i]));
                bw.newLine();
            }
            bw.close();
            System.out.println("\u001B[35m"+"ARCHIVO CREADO"+"\u001B[0m");
        }else{
            File archivo = new File(ruta);
            BufferedWriter bw;
            bw = new BufferedWriter(new FileWriter(archivo));
            for(int i=0;i<cadenas.length;i++){
                bw.write(cadenas[i]);
                bw.newLine();
                bw.write(esAceptada(cadenas[i]));
                bw.newLine();
            }
            bw.close();
            System.out.println("\u001B[35m"+"ARCHIVO CREADO"+"\u001B[0m");
        }

    }
}
