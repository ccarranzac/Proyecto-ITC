import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class AFD {
    ArrayList alfabeto;
    ArrayList estados;
    String estado_inicial;
    ArrayList estados_aceptacion;
    ArrayList transiciones;
    ArrayList items;


    public AFD(ArrayList alfabeto, ArrayList estados, String estado_inicial, ArrayList estados_aceptacion, ArrayList transiciones) {
        this.alfabeto = alfabeto;
        this.estados = estados;
        this.estado_inicial = estado_inicial;
        this.estados_aceptacion = estados_aceptacion;
        this.transiciones = transiciones;
    }

    public AFD(String NombreArchivo) {
        File fichero = new File(NombreArchivo);
        Scanner s = null;

        ArrayList alfabeto_f = new ArrayList();
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
        int pos_estados = 0;
        int pos_estado_inicial = 0;
        int pos_aceptacion = 0;
        int pos_transiciones = 0;

        for (int i = 0; i < items_f.size(); i++) {
            String elemento = (String) items_f.get(i);
            if (elemento.equals("#alphabet")) {
                pos_alfabeto = i;
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

        //alfabeto
        for (int i = pos_alfabeto + 1; i < pos_estados; i++) {
            alfabeto_f.add(items_f.get(i));
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
        //new AFD(alfabeto_f,estados_f,estado_inicial_f,estados_aceptacion_f,transiciones_f);
        //this(alfabeto_f,estados_f,estado_inicial_f,estados_aceptacion_f,transiciones_f);
        this.alfabeto = alfabeto_f;
        this.estados = estados_f;
        this.estado_inicial = estado_inicial_f;
        this.estados_aceptacion = estados_aceptacion_f;
        this.transiciones = transiciones_f;
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

    public boolean esAceptada(String cadena){
        char[] aCaracteres = cadena.toCharArray();
        
        return false;
    }
}

