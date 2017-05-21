import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;

/*
*   2º Trabalho da disciplina de Sistema Operacionais - UFPB
*   Autor: Diego Filipe Souza de Lima
*   Data: 17/05/2017
*
*   Foi solicitado a implementação de 3 algoritmos de Substituição de paginas.
*     1. FIFO
*     2. OTM
*     3. LRU
*
*   Foi solicitado como metrica do trabalho a apresentação do número de erros de
*   faltas de paginas de cada algoritmo.
*/

public class Algoritmos{

  private static ArrayList<Integer> sequenciaPag = new ArrayList<Integer>();
  static final int INIT = -1;

  public static void main(String[] args) {

    try {
       String filePath = "../inputs/entrada.txt";
       String line = "";
       BufferedReader readFile = new BufferedReader(new FileReader(filePath));
       int qtdQuadros = Integer.parseInt(readFile.readLine());

       while ((line = readFile.readLine()) != null) {
         sequenciaPag.add(Integer.parseInt(line));
       }

      /* while(!sequenciaPag.isEmpty()){
            System.out.println(sequenciaPag.remove(0));
        }*/

      fifo(qtdQuadros);
      otm(qtdQuadros);
      lru(qtdQuadros);

     }
     catch (Exception e) {
       e.printStackTrace();
     }
  }
  /*
	*	Implementação do algoritmo FIFO "First In First Out"
	*	@param qtdQuadros - Quantidade de quadros disponiveis na memoria
	*	@return - Exibi o resultado da metrica solicitada
	*/
  public static void fifo(int qtdQuadros) {

      int faltaPagina = 0;
      Queue<Integer> quadrosDisp = new LinkedList<Integer>(); // quadros disponiveis na memoria

      for (int i = 0; i < qtdQuadros; i++)
        quadrosDisp.add(INIT);                 // inicializa os quadros com valor -1

      for (Integer pagAtual : sequenciaPag) {
        if (!quadrosDisp.contains(pagAtual)) { // verifica se ocorre falta de pagina
          quadrosDisp.poll();                  // remove a pagina mais dentre as que estão alocadas
          quadrosDisp.add(pagAtual);           // insere a pagina deseja na fila
          faltaPagina++;
        }
      }

      System.out.println("FIFO " + faltaPagina);
    }
    /*
  	*	Implementação do algoritmo OTM "Algoritmo Otimo"
  	*	@param qtdQuadros - Quantidade de quadros disponiveis na memoria
  	*	@return - Exibi o resultado da metrica solicitada
  	*/
    public static void otm(int qtdQuadros) {

      int faltaPagina = 0;
  		int maiorTempo = 0;   // salva a pagina que não será acessada pelo maior período de maior
  		int quadroVit = 0;    // salva qual o quadro vitima
      int falhaInicial = qtdQuadros;  // controla os erros de paginas iniciais
  		ArrayList<Integer> quadrosDisp = new ArrayList<Integer>(); // lista de quadros disponiveis
      ArrayList<Integer> seqOtm = new ArrayList<Integer>(sequenciaPag); // copia auxiliar da sequência de refência
      int tamanho = seqOtm.size();

      for (int i = 0; i < qtdQuadros; i++)
        quadrosDisp.add(INIT);                      // inicializa os quadros com valor -1

      for(Integer pagAtual : sequenciaPag){

        if(!quadrosDisp.contains(pagAtual)){        // verifica se ocorre falta de pagina

    		  if(falhaInicial != 0){                    // falta de paginas iniciais
    				quadrosDisp.set(faltaPagina,pagAtual);  // seta a pagina no quadro disponivel
            seqOtm.remove(0);                       // remove pagina da sequência inicial
            faltaPagina++;
            falhaInicial--;
    				continue;
    			}

    			maiorTempo = -1;

    			for(int j = 0; j < quadrosDisp.size(); j++){ // seleciona o quadro vitíma

    				if(seqOtm.indexOf(quadrosDisp.get(j)) == -1){ // verifica se a pagina que está em um quadro, ainda vai precisar ser alocada
    					quadroVit = j;
    					break;
    				}

    				if(seqOtm.indexOf(quadrosDisp.get(j)) > maiorTempo){ // verifica quando a pagina que está na memoria, vai ser alocada
    					quadroVit = j;
    					maiorTempo = seqOtm.indexOf(quadrosDisp.get(j));
    				}
    			}
    			quadrosDisp.set(quadroVit, pagAtual);    // insere a pagina no quadro correto
    			faltaPagina++;
        }
        seqOtm.remove(0);
    	}
    	System.out.println("OTM " + faltaPagina);
    }
    /*
  	*	Implementação do algoritmo LRU "Least Recently Used"
  	*	@param qtdQuadros - Quantidade de quadros disponiveis na memoria
  	*	@return - Exibi o resultado da metrica solicitada
  	*/
    public static void lru(int qtdQuadros){

        int faltaPagina = 0;
        boolean found = false;          // permite a substituição de pagina
        final int BASE = qtdQuadros-1;  // base da pilha
        final int TOPO = 0;             // topo da pilha
        ArrayList<Integer> pilhaQuad = new ArrayList<Integer>(); // pilha de quadros disponiveis

        for (Integer entry : sequenciaPag) {
			       found = false;

    			for (Integer frame : pilhaQuad) {
    				if (frame == entry){            // verifica se a pagina ja está alocada a um quadro
    					pilhaQuad.remove(frame);
    					pilhaQuad.add(TOPO,entry);
    					found = true;
    					break;
    				}
    			}

    			if(!found){
    				if (pilhaQuad.size() != qtdQuadros){  // falta de paginas iniciais
    					faltaPagina++;
    					pilhaQuad.add(TOPO,entry);      // aloca as paginas iniciais
    				} else {                          // demais faltas de paginas
    					faltaPagina++;
    					pilhaQuad.remove(BASE);         // remove a base da pilha
    					pilhaQuad.add(TOPO,entry);      // insere no topo da pilha
    				}
    			}
    		}
        System.out.println("LRU "+faltaPagina);
    }
}
