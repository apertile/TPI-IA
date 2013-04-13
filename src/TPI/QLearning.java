/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TPI;

/**
 *
 * @author fede
 */
public class QLearning implements Runnable {
    //las 8 acciones posibles
    static final int N=0;
    static final int NE=1;
    static final int E=2;
    static final int SE=3;
    static final int S=4;
    static final int SO=5;
    static final int O=6;
    static final int NO=7;
    int cantacciones=8;
    long iteracion=0;
    long maxIteracion=100000;
    //valor de las recompensas inmediatas
    double recBueno = 25.0;
    double recExcelente = 50.0;
    double recFinal = 100.0;
    double recMalo = -10.0;
    double recNormal = 10.0;
    // parametros
    double Tau; //temperatura para softmax
    double epsilon = 0.7; //exploration rate para egreedy
    double alpha = 0.1; //para la formula de Q(s,a), es la de aprendizaje
    double gamma = 0.9; //tambien para esa formula, es la de amortizacion
    // tabla de Qvalues en [i][j][accion]
    double Qvalues [][][];
    //grilla 
    int map[][];
    //tamaño de la columna/fila
    int tamaño=0;
    //constructores
    public QLearning(int tmño){
        initQvalues(tmño);
    }
    public QLearning (int tmño,long itmax, double exp,double apren, double temp, double amort, double recB, double recE, double recN, double recF,double recM,int [][] mapa){
        this.maxIteracion=itmax;
        this.tamaño=tmño;
        this.Tau=temp;
        this.alpha=apren;
        this.gamma=amort;
        this.recBueno=recB;
        this.recMalo=recM;
        this.recExcelente=recE;
        this.recFinal=recF;
        this.recNormal=recN;
        this.map=mapa;
        initQvalues(tamaño);
            }
    //inicializador de la tabla Qvalues
    private void initQvalues(int tamaño){
        Qvalues=new double[tamaño][tamaño][cantacciones];
        for(int i=0;i<tamaño;i++){
            for(int j=0;j<tamaño;j++){
                for(int a=0;a<cantacciones;a++){
                Qvalues[i][j][a]=0.0;
                }
            }
        }
    }
    //mejor accion para el estado i,j, la que da el Qvalue mas alto
    private int mejorAccion (int i,int j){
        int laMejorAccion=8;
        double mejorQvalue=-10000.0;
        for(int a=0;a<this.cantacciones;a++){
            if(Qvalues[i][j][a]>mejorQvalue){
                mejorQvalue=Qvalues[i][j][a];
                laMejorAccion=a;
            }            
        }
        return laMejorAccion;
    }
    //calcular el max Q de un estado i,j
    private double mejorQvalue (int i, int j){
        double mejorQ=-10000.0;
        for(int a=0;a<this.cantacciones;a++){
            if(Qvalues[i][j][a]>mejorQ){
                mejorQ=Qvalues[i][j][a];
            }
        }
        return mejorQ;
    }
    //devuelve el siguiente estado tras una accion
    private int[] siguiente(int i,int j, int accion){
        // i en la 1º posicion, j en la segunda.
        int resultado[]=new int[2];
        switch (accion){
            case N:
            if(i==0){
                resultado[0]=i; resultado [1]=j;
                //estoy fila 0 no puedo ir arriba, quedo mismo lugar
            } else {
                if (map[(i-1)][j]==5){
                    resultado[0]=i; resultado [1]=j;
                    //el siguiente estado seria pozo, no deberia poder ir
                } else {
                    resultado[0]=(i-1); resultado [1]=j;
                }
            }
            break;
            case NE:
            if(i==0 || j==(tamaño-1)){
                resultado[0]=i; resultado [1]=j;
                //primera fila o ultima columna, no puedo moverme
            } else {
                if (map[(i-1)][j+1]==5){
                    resultado[0]=i; resultado [1]=j;
                    //el siguiente estado seria pozo, no deberia poder ir
                } else {
                    resultado[0]=(i-1); resultado [1]=(j+1);
                }
            }
            break;
            case E:
            if (j==(tamaño-1)){
                resultado[0]=i; resultado [1]=j;
                //estoy en la ultima columna no puedo ir a la derecha
            } else {
                if (map[i][(j+1)]==5){
                    resultado[0]=i; resultado [1]=j;
                    //el siguiente estado es pozo, no debo poder ir
                } else {
                    resultado[0]=i; resultado [1]=j+1;
                }
            }
            break;
            case SE:
            if (i==(tamaño-1) || j==(tamaño-1)){
                resultado[0]=i; resultado [1]=j;
                //ultima columna y ultima fila, no se debe poder mover
            } else {
                if (map[i+1][(j+1)]==5){
                    resultado[0]=i; resultado [1]=j;
                    //el siguiente estado es pozo, no debo poder ir
                } else {
                    resultado[0]=(i+1); resultado [1]=(j+1);
                }
            }
            break;    
            case S:
            if (i==(tamaño-1)){
                resultado[0]=i; resultado [1]=j;
                //estoy en la ultima columna no deberia poder ir
            } else {
                if (map[i+1][j]==5){
                    resultado[0]=i; resultado [1]=j;
                    //siguiente estado es pozo, no deberia ir
                } else {
                    resultado[0]=i+1; resultado [1]=j;
                }
            }
            break;
            case SO:
            if (i==(tamaño-1) || j==0){
                resultado[0]=i; resultado [1]=j;
                //primera columna o ultima columna no se debe mover
            } else {
                if (map[i+1][j-1]==5){
                    resultado[0]=i; resultado [1]=j;
                    //siguiente estado es pozo, no deberia ir
                } else {
                    resultado[0]=i+1; resultado [1]=j-1;
                }
            }
            break;    
            case O:
            if (j==0){
                resultado[0]=i; resultado [1]=j;
                //primera columna no puedo ir a la izquierda
            } else {
                if (map[i][j-1]==5){
                    resultado[0]=i; resultado [1]=j;
                    // siguiente es un pozo, no puedo ir
                } else {
                    resultado[0]=i; resultado [1]=j-1;
                }
            }
            break;    
            case NO:
            if (i==0 || j==0){
                resultado[0]=i; resultado [1]=j;
                //primera columna o primera fila, no me puedo mover
            } else {
                if (map[i-1][j-1]==5){
                    resultado[0]=i; resultado [1]=j;
                    // siguiente es un pozo, no puedo ir
                } else {
                    resultado[0]=i-1; resultado [1]=j-1;
                }
            }
            break; 
        }
        return resultado;
    }
    //devuelve recompensa inmediata
    private double recompensa(int i, int j, int a){
        //valor por defecto
        double resultado=0.0;
        int siguiente []=this.siguiente(i, j, a);
        //calidad, bueno malo, etc..        
        int calidad = map[siguiente[0]][siguiente[1]];
        switch (calidad){
            case 0:resultado=this.recNormal;break; //es normal
            case 1:resultado=this.recMalo;break; //es malo 
            case 2:resultado=this.recBueno;break; //es bueno
            case 3:resultado=this.recExcelente;break; //es excelente
            case 4:resultado=this.recFinal;break; // es el obj final
            case 5:break; //es pozo
        }
        return resultado;
    }
    //metodo de seleccion (por ahora solo egreedy)
    private int elegirSiguiente (int i, int j){
        int accion;
        double random = java.lang.Math.random();
        if (random<this.epsilon){
            //cae dentro de la exploracion, accion es aleatoria
            accion = java.lang.Math.round((float)java.lang.Math.random()*(this.cantacciones-1));
        } else {
            //cae dentro de la parte de explotacion, accion es la mejor
            accion = mejorAccion(i,j);
        }
        return accion;
    }
    //estado de partida aleatoria
    private int[] estadoInicialAleatorio() {
        int resultado [] = new int[2];
        resultado[0] = java.lang.Math.round((float)(java.lang.Math.random()*(tamaño-1)));
        resultado[1] = java.lang.Math.round((float)(java.lang.Math.random()*(tamaño-1)));
        return resultado;
    }
    //actualizar tabla Qvalues
    private void actualizarQtable (int i, int j, int a){
        double qViejo = Qvalues[i][j][a];
        double recompensa = this.recompensa(i, j, a);
        int siguiente [] = this.siguiente(i, j, a);
        double maxQ = this.mejorQvalue(siguiente [0], siguiente [1]);
        // se aplica la formula
        Qvalues [i][j][a] = (1-this.alpha)*qViejo + this.alpha*(recompensa+(this.gamma*maxQ));
    }
    
    public void run(){
        int estadoActual [] = this.estadoInicialAleatorio();
        for (long iter=0; iter<this.maxIteracion;iter++){
            this.iteracion=iter;
            int accion=this.elegirSiguiente(estadoActual[0], estadoActual[1]);
            int estadoSiguiente [] = this.siguiente(estadoActual[0], estadoActual[1], accion);
            switch (map[estadoSiguiente[0]] [estadoSiguiente[1]]){
                case 0:estadoActual=estadoSiguiente;break;
                case 1:estadoActual=estadoSiguiente;break;
                case 2:estadoActual=estadoSiguiente;break;
                case 3:estadoActual=estadoSiguiente;break;
                case 4:this.estadoInicialAleatorio();break; // es el final, llegamos al objetivo, arranca de nuevo
                case 5:estadoActual=estadoSiguiente;break;    
            }
        }
    }
}   

