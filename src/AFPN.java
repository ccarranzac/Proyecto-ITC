import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class AFPN {
    ArrayList alfabeto_cinta;
    ArrayList alfabeto_pila;
    ArrayList estados;
    String estado_inicial;
    ArrayList estados_aceptacion;
    ArrayList transiciones;
    ArrayList items;

    public AFPN(ArrayList alfabeto_cinta, ArrayList alfabeto_pila, ArrayList estados, String estado_inicial, ArrayList estados_aceptacion, ArrayList transiciones) {
        this.alfabeto_cinta = alfabeto_cinta;
        this.alfabeto_pila = alfabeto_pila;
        this.estados = estados;
        this.estado_inicial = estado_inicial;
        this.estados_aceptacion = estados_aceptacion;
        this.transiciones = transiciones;
    }

    public AFPN(String NombreArchivo) {
        File fichero = new File(NombreArchivo);
        Scanner s = null;

        ArrayList alfabeto_cinta_f = new ArrayList();
        ArrayList alfabeto_pila_f = new ArrayList();
        ArrayList estados_f = new ArrayList();
        String estado_inicial_f = null;
        ArrayList estados_aceptacion_f = new ArrayList();
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

        int pos_alfabeto_cinta = 0;
        int pos_alfabeto_pila = 0;
        int pos_estados = 0;
        int pos_estado_inicial = 0;
        int pos_aceptacion = 0;
        int pos_transiciones = 0;

        for (int i = 0; i < items_f.size(); i++) {
            String elemento = (String) items_f.get(i);
            if (elemento.equals("#states")) {
                pos_estados = i;
            } else if (elemento.equals("#initial")) {
                pos_estado_inicial = i;
            } else if (elemento.equals("#accepting")) {
                pos_aceptacion = i;
            } else if (elemento.equals("#tapeAlphabet")) {
                pos_alfabeto_cinta = i;
            } else if (elemento.equals("#stackAlphabet")) {
                pos_alfabeto_pila = i;
            } else if(elemento.equals("#transitions")){
                pos_transiciones=i;
            }
        }
        //estados
        for (int i = pos_estados + 1; i < pos_estado_inicial; i++) {
            estados_f.add(items_f.get(i));
        }
        //estado inicial
        estado_inicial_f = (String) items_f.get(pos_estado_inicial + 1);
        //aceptacion
        for (int i = pos_aceptacion + 1; i < pos_alfabeto_cinta; i++) {
            estados_aceptacion_f.add(items_f.get(i));
        }
        //alfabeto Cinta
        for (int i = pos_alfabeto_cinta + 1; i < pos_alfabeto_pila; i++) {
            alfabeto_cinta_f.add(items_f.get(i));
        }
        //alfabeto Pila
        for (int i = pos_alfabeto_pila + 1; i < pos_transiciones; i++) {
            alfabeto_pila_f.add(items_f.get(i));
        }
        //transiciones
        for (int i = pos_transiciones + 1; i < items_f.size(); i++) {
            transiciones_f.add(items_f.get(i));
        }

        this.alfabeto_cinta = alfabeto_cinta_f;
        this.alfabeto_pila = alfabeto_pila_f;
        this.estados = estados_f;
        this.estado_inicial = estado_inicial_f;
        this.estados_aceptacion = estados_aceptacion_f;
        this.transiciones = transiciones_f;

    }

    public boolean alfabetoCorrecto(String cadena) {
        char[] aCaracteres = cadena.toCharArray();
        boolean alfabetoC = true;
        ArrayList ocur=new ArrayList();

        for (int i = 0; i < aCaracteres.length; i++) {
            int ocurrencias=0;
            for (int j = 0; j < alfabeto_cinta.size(); j++) {
                String item = (String) alfabeto_cinta.get(j);
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

    public void esAceptada(String cadena,int caminos){
        int tamaño=transiciones.size();
        char[] aCaracteres = cadena.toCharArray();
        String estado_actual=estado_inicial;
        boolean aceptacion=false;

        String[] estados=new String[tamaño];
        char[] cadena_estado=new char[tamaño];
        char[] pila_antes=new char[tamaño];
        char[] pila_despues=new char[tamaño];
        String[] sig_estado=new String[tamaño];
        int posicion1=0,posicion2=0,posicion3=0,posicion4=0;

        for(int i=0;i<transiciones.size();i++){
            int iterador_dp=0;
            String item= (String) transiciones.get(i);
            char[] item_=item.toCharArray();
            for(int j=0;j<item.length();j++){
                if(item_[j]==':'){
                    if(iterador_dp==0){
                        posicion1=j;
                        iterador_dp++;
                    }
                    else if(iterador_dp==1){
                        posicion2=j;
                        iterador_dp++;
                    }
                    else if(iterador_dp==2){
                        posicion4=j;
                    }
                }else if(item_[j]=='>'){
                    posicion3=j;
                }
            }
            char[] aux=new char[posicion1];
            char[] aux2=new char[posicion4-(posicion3+1)];
            int k=0;
            for(int j=0;j<posicion1;j++){
                aux[j]=item_[j];
            }
            estados[i]=new String(aux);
            cadena_estado[i]=item_[posicion1+1];
            pila_antes[i]=item_[posicion2+1];
            for(int j=posicion3+1;j<posicion4;j++){
                aux2[k]=item_[j];
                k++;
            }
            sig_estado[i]=new String(aux2);
            pila_despues[i]=item_[posicion4+1];
        }

        for(int m=0;m<caminos;m++){
            System.out.println("Procesamiento "+(m+1)+":");
            Stack<Character> pila= new Stack<>();
            int numero_estado=0;
            for(int i=0;i<aCaracteres.length;i++){
                ArrayList numeros =new ArrayList();
                char[] auxchar=new char[aCaracteres.length-i];
                int l=i;
                for(int k=0;k<auxchar.length;k++){
                    auxchar[k]=aCaracteres[l];
                    l++;
                }

                if(pila.isEmpty()){
                    System.out.println("("+estado_actual+","+""+new String(auxchar)+","+""+"$"+")"+"->");
                }else{
                    System.out.println("("+estado_actual+","+""+new String(auxchar)+","+""+pila+")"+"->");
                }
                for(int j=0;j<estados.length;j++){
                    if(estado_actual.equals(estados[j])){
                        if(aCaracteres[i]==cadena_estado[j]){
                            numeros.add(j);
                        }
                    }
                }
                for(int j=0;j<numeros.size();j++){
                    int numero= (int) numeros.get(j);
                    if(pila_antes[numero]!='$'){
                        if(pila.isEmpty()){
                            numeros.remove(j);
                        }
                        else if(pila_antes[numero]!=pila.peek()){
                            numeros.remove(j);
                        }
                    }
                }

                if(numeros.size()==0){
                    //System.out.println("no hay caminos disponibles");
                }
                else if(numeros.size()==1){
                    int numero= (int) numeros.get(0);
                    if(pila_antes[numero]=='$'){
                        if(pila_despues[numero]!='$'){
                            pila.push(pila_despues[numero]);
                        }
                    }else{
                        pila.pop();
                        if(pila_despues[numero]!='$'){
                            pila.push(pila_despues[numero]);
                        }
                    }
                    numero_estado=numero;

                }
                else{
                    Random r = new Random();
                    int Low = 1;
                    int High = numeros.size()+1;
                    int result = r.nextInt(High-Low) + Low;
                    int numero=(int) numeros.get(result-1);
                    if(pila_antes[numero]=='$'){
                        if(pila_despues[numero]!='$'){
                            pila.push(pila_despues[numero]);
                        }
                    }else{
                        pila.pop();
                        if(pila_despues[numero]!='$'){
                            pila.push(pila_despues[numero]);
                        }
                    }
                    numero_estado=numero;

                }


                estado_actual=sig_estado[numero_estado];

            }
            System.out.println("("+estado_actual+","+" $"+","+pila+")>>");
            for(int i=0;i<estados_aceptacion.size();i++){
                String estado_aceptacion= (String) estados_aceptacion.get(i);
                if(estado_actual.equals(estado_aceptacion)){
                    aceptacion=true;
                }
            }
            if(aceptacion && pila.isEmpty()){
                System.out.println("accepted");
            }else{
                System.out.println("rejected");
            }


        }

    }

    public void procesarListaCadenas(String []cadenas, String nombreArchivo,boolean imprimirPantalla,int[] caminos) throws IOException {
        int tamaño=transiciones.size();
        //char[] aCaracteres = cadena.toCharArray();
        String estado_actual=estado_inicial;
        boolean aceptacion=false;

        String[] estados=new String[tamaño];
        char[] cadena_estado=new char[tamaño];
        char[] pila_antes=new char[tamaño];
        char[] pila_despues=new char[tamaño];
        String[] sig_estado=new String[tamaño];
        int posicion1=0,posicion2=0,posicion3=0,posicion4=0;

        for(int i=0;i<transiciones.size();i++){
            int iterador_dp=0;
            String item= (String) transiciones.get(i);
            char[] item_=item.toCharArray();
            for(int j=0;j<item.length();j++){
                if(item_[j]==':'){
                    if(iterador_dp==0){
                        posicion1=j;
                        iterador_dp++;
                    }
                    else if(iterador_dp==1){
                        posicion2=j;
                        iterador_dp++;
                    }
                    else if(iterador_dp==2){
                        posicion4=j;
                    }
                }else if(item_[j]=='>'){
                    posicion3=j;
                }
            }
            char[] aux=new char[posicion1];
            char[] aux2=new char[posicion4-(posicion3+1)];
            int k=0;
            for(int j=0;j<posicion1;j++){
                aux[j]=item_[j];
            }
            estados[i]=new String(aux);
            cadena_estado[i]=item_[posicion1+1];
            pila_antes[i]=item_[posicion2+1];
            for(int j=posicion3+1;j<posicion4;j++){
                aux2[k]=item_[j];
                k++;
            }
            sig_estado[i]=new String(aux2);
            pila_despues[i]=item_[posicion4+1];
        }
        String ruta="E:\\IntellijProjects\\repositorios git\\PROYECTO-ITC\\salida\\"+nombreArchivo+".txt";
        if(imprimirPantalla){
            File archivo = new File(ruta);
            BufferedWriter bw;
            bw = new BufferedWriter(new FileWriter(archivo));
            for(int h=0;h<cadenas.length;h++){
                char[] aCaracteres = cadenas[h].toCharArray();
                System.out.println(cadenas[h]);
                bw.write(cadenas[h]);
                bw.newLine();
                for(int m=0;m<caminos[h];m++){
                    System.out.println("Procesamiento "+(m+1)+":");
                    bw.write("Procesamiento "+(m+1)+":");
                    bw.newLine();
                    Stack<Character> pila= new Stack<>();
                    int numero_estado=0;
                    for(int i=0;i<aCaracteres.length;i++){
                        ArrayList numeros =new ArrayList();
                        char[] auxchar=new char[aCaracteres.length-i];
                        int l=i;
                        for(int k=0;k<auxchar.length;k++){
                            auxchar[k]=aCaracteres[l];
                            l++;
                        }

                        if(pila.isEmpty()){
                            System.out.println("("+estado_actual+","+""+new String(auxchar)+","+""+"$"+")"+"->");
                            bw.write("("+estado_actual+","+""+new String(auxchar)+","+""+"$"+")"+"->");
                            bw.newLine();
                        }else{
                            System.out.println("("+estado_actual+","+""+new String(auxchar)+","+""+pila+")"+"->");
                            bw.write("("+estado_actual+","+""+new String(auxchar)+","+""+pila+")"+"->");
                            bw.newLine();
                        }
                        for(int j=0;j<estados.length;j++){
                            if(estado_actual.equals(estados[j])){
                                if(aCaracteres[i]==cadena_estado[j]){
                                    numeros.add(j);
                                }
                            }
                        }
                        for(int j=0;j<numeros.size();j++){
                            int numero= (int) numeros.get(j);
                            if(pila_antes[numero]!='$'){
                                if(pila.isEmpty()){
                                    numeros.remove(j);
                                }
                                else if(pila_antes[numero]!=pila.peek()){
                                    numeros.remove(j);
                                }
                            }
                        }

                        if(numeros.size()==0){
                            //System.out.println("no hay caminos disponibles");
                        }
                        else if(numeros.size()==1){
                            int numero= (int) numeros.get(0);
                            if(pila_antes[numero]=='$'){
                                if(pila_despues[numero]!='$'){
                                    pila.push(pila_despues[numero]);
                                }
                            }else{
                                pila.pop();
                                if(pila_despues[numero]!='$'){
                                    pila.push(pila_despues[numero]);
                                }
                            }
                            numero_estado=numero;

                        }
                        else{
                            Random r = new Random();
                            int Low = 1;
                            int High = numeros.size()+1;
                            int result = r.nextInt(High-Low) + Low;
                            int numero=(int) numeros.get(result-1);
                            if(pila_antes[numero]=='$'){
                                if(pila_despues[numero]!='$'){
                                    pila.push(pila_despues[numero]);
                                }
                            }else{
                                pila.pop();
                                if(pila_despues[numero]!='$'){
                                    pila.push(pila_despues[numero]);
                                }
                            }
                            numero_estado=numero;

                        }


                        estado_actual=sig_estado[numero_estado];

                    }
                    System.out.println("("+estado_actual+","+" $"+","+pila+")>>");
                    bw.write("("+estado_actual+","+" $"+","+pila+")>>");
                    bw.newLine();
                    for(int i=0;i<estados_aceptacion.size();i++){
                        String estado_aceptacion= (String) estados_aceptacion.get(i);
                        if(estado_actual.equals(estado_aceptacion)){
                            aceptacion=true;
                        }
                    }
                    if(aceptacion && pila.isEmpty()){
                        System.out.println("accepted");
                        bw.write("yes");
                        bw.newLine();
                    }else{
                        System.out.println("rejected");
                        bw.write("no");
                        bw.newLine();
                    }


                }
            }
            bw.close();
            System.out.println("\u001B[35m"+"ARCHIVO CREADO"+"\u001B[0m");
        }else{
            File archivo = new File(ruta);
            BufferedWriter bw;
            bw = new BufferedWriter(new FileWriter(archivo));
            for(int h=0;h<cadenas.length;h++){
                char[] aCaracteres = cadenas[h].toCharArray();
                bw.write(cadenas[h]);
                bw.newLine();
                for(int m=0;m<caminos[h];m++){
                    bw.write("Procesamiento "+(m+1)+":");
                    bw.newLine();
                    Stack<Character> pila= new Stack<>();
                    int numero_estado=0;
                    for(int i=0;i<aCaracteres.length;i++){
                        ArrayList numeros =new ArrayList();
                        char[] auxchar=new char[aCaracteres.length-i];
                        int l=i;
                        for(int k=0;k<auxchar.length;k++){
                            auxchar[k]=aCaracteres[l];
                            l++;
                        }

                        if(pila.isEmpty()){
                            bw.write("("+estado_actual+","+""+new String(auxchar)+","+""+"$"+")"+"->");
                            bw.newLine();
                        }else{
                            bw.write("("+estado_actual+","+""+new String(auxchar)+","+""+pila+")"+"->");
                            bw.newLine();
                        }
                        for(int j=0;j<estados.length;j++){
                            if(estado_actual.equals(estados[j])){
                                if(aCaracteres[i]==cadena_estado[j]){
                                    numeros.add(j);
                                }
                            }
                        }
                        for(int j=0;j<numeros.size();j++){
                            int numero= (int) numeros.get(j);
                            if(pila_antes[numero]!='$'){
                                if(pila.isEmpty()){
                                    numeros.remove(j);
                                }
                                else if(pila_antes[numero]!=pila.peek()){
                                    numeros.remove(j);
                                }
                            }
                        }

                        if(numeros.size()==0){
                            //System.out.println("no hay caminos disponibles");
                        }
                        else if(numeros.size()==1){
                            int numero= (int) numeros.get(0);
                            if(pila_antes[numero]=='$'){
                                if(pila_despues[numero]!='$'){
                                    pila.push(pila_despues[numero]);
                                }
                            }else{
                                pila.pop();
                                if(pila_despues[numero]!='$'){
                                    pila.push(pila_despues[numero]);
                                }
                            }
                            numero_estado=numero;

                        }
                        else{
                            Random r = new Random();
                            int Low = 1;
                            int High = numeros.size()+1;
                            int result = r.nextInt(High-Low) + Low;
                            int numero=(int) numeros.get(result-1);
                            if(pila_antes[numero]=='$'){
                                if(pila_despues[numero]!='$'){
                                    pila.push(pila_despues[numero]);
                                }
                            }else{
                                pila.pop();
                                if(pila_despues[numero]!='$'){
                                    pila.push(pila_despues[numero]);
                                }
                            }
                            numero_estado=numero;

                        }


                        estado_actual=sig_estado[numero_estado];

                    }
                    bw.write("("+estado_actual+","+" $"+","+pila+")>>");
                    bw.newLine();
                    for(int i=0;i<estados_aceptacion.size();i++){
                        String estado_aceptacion= (String) estados_aceptacion.get(i);
                        if(estado_actual.equals(estado_aceptacion)){
                            aceptacion=true;
                        }
                    }
                    if(aceptacion && pila.isEmpty()){
                        bw.write("yes");
                        bw.newLine();
                    }else{
                        bw.write("no");
                        bw.newLine();
                    }


                }
            }
            bw.close();
            System.out.println("\u001B[35m"+"ARCHIVO CREADO"+"\u001B[0m");
        }
    }
}
