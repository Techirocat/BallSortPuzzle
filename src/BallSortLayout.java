import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BallSortLayout implements Ilayout {

    private final int altura;
    private final int nTubos;
    private List<Stack<Character>> board;

    private static final char vazio = 'E';


    /**
     * Construtor de uma nova instância de BallSortLayout
     * @param  configuration representação textual do estado inicial do tabuleiro
     */
    public BallSortLayout(int altura, int nTubos, String configuration){
        if (configuration == null) throw new IllegalArgumentException();

        this.altura = altura;
        this.nTubos = nTubos;

        String [] lines = configuration.split("\\R");

        board = new ArrayList<>(nTubos);
        for (int i = 0; i < nTubos; i++) {
            board.add(new Stack<>());
        }

        for (int i = altura - 1; i >= 0; i--) {
            String [] tubes = lines[i].split(" ");

            for (int j = 0; j < nTubos; j++) {
                char c = tubes[j].charAt(0);

                if(c != vazio){
                    board.get(j).push(c);
                }
            }
        }
    }


    /**
     * Contrutor de deep copy
     * @param other o layout de referência a ser copiado
     */
    public BallSortLayout(BallSortLayout other){
        altura = other.altura;
        nTubos = other.nTubos;

        this.board = new ArrayList<>(other.nTubos);
        for (Stack<Character> stack : other.board) {
            Stack<Character> st = new Stack<>();
            st.addAll(stack);
            this.board.add(st);
        }
    }

    /**
     * Indica se o tubo especificado está sem qualquer bola
     * @param tubo ídice do tubo, de 0 a nTubos - 1
     * @return
     */
    public boolean isTuboVazio(int tubo){
        if (tubo < 0 || tubo >= nTubos) throw new IllegalArgumentException();
        return board.get(tubo).isEmpty();
    }

    /**
     * Verifica se um tubo já se encontra cheio
     * @param tubo ídice do tubo, de 0 a nTubos - 1
     * @return
     */
    public boolean isTuboCheio(int tubo){
        if (tubo < 0 || tubo >= nTubos) throw new IllegalArgumentException();
        return  board.get(tubo).size() == altura;
    }

    /**
     * Compara a igualdade estrutural entre dois Boards
     * @param o objeto a ser comparado
     * @return true se ambos os objetos forem iguais, false caso contrário.
     */
    @Override
    public boolean equals(Object o){
        if(o == this){
            return true;
        }else if(!(o instanceof BallSortLayout)){
            return false;
        }

        BallSortLayout b = (BallSortLayout)o;
        return this.board.equals(b.board);
    }

    /**
     * Valida se uma bola de cor "c" pode ser inserida no tubo de destino
     * @param tubo índice de tubo de destino
     * @param c identificador da bola que se pretende mover
     * @return true se o tubo tiver espaço, vazio ou com o topo da mesma cor
     */
    private boolean isTuboValido (int tubo, char c){
        if (tubo < 0 || tubo >= nTubos) throw new IllegalArgumentException();
        if (isTuboCheio(tubo)) return false;

        return isTuboVazio(tubo) || board.get(tubo).peek() == c;
    }

    /**
     * Gera todos os estados sucessores a partir do estado atual
     * @return lista de configurações sucessoras
     */
    @Override
    public List<Ilayout> children() {
        List<Ilayout> list = new ArrayList<>();

        for (int i = 0; i < nTubos; i++) {
            if (board.get(i).isEmpty()) continue;
            char c = board.get(i).peek();

            boolean emptyUsed = false;

            for (int j = 0; j < nTubos; j++) {
                if(i == j) continue;

                if (board.get(j).isEmpty()){
                    if(emptyUsed){
                        continue;
                    }
                    emptyUsed = true;
                }

                if (isTuboValido(j, c)){
                    BallSortLayout b = new BallSortLayout(this);
                    char mover = b.board.get(i).pop();
                    b.board.get(j).push(mover);
                    list.add(b);
                }
            }
        }
        return list;
    }

    /**
     * Determina se o estado atual representa um objetivo atingido
     * @param l estado objetivo -> não vai ser utilizado, mas está definido na interface
     * @return true se o estado satisfaz as condições de vitória, caso contrário false.
     */
    @Override
    public boolean isGoal(Ilayout l) {
        for (int i = 0; i < nTubos; i++) {
            if (board.get(i).isEmpty()) continue;
            if (board.get(i).size() != altura) return false;

            char c =  board.get(i).peek();
            for (int j = 0; j < altura; j++) {
                if (board.get(i).get(j) != c) return false;
            }
        }
        return true;
    }

    /**
     * Retorna o custo de transição.
     * @return custo padrão de 1.0
     */
    @Override
    public double getK() {
        return 1.0;
    }

    @Override
    public double getH() {
        int bolasMal = 0;
        int tubosInc = 0;

        for (int i = 0; i < nTubos; i++) {
            Stack<Character> tubo = board.get(i);
            if (tubo.isEmpty()) continue;

            char cor = tubo.getFirst();
            int alturaBase = 0;
            while (alturaBase < tubo.size() && tubo.get(alturaBase) == cor) { // Vereficar se o tubo é só tem uma mesma cor
                alturaBase++;
            }

            if (alturaBase == tubo.size()) { // O tubo só tem uma cor
                if (tubo.size() < altura) { // O tubo ainda não está cheio
                    tubosInc++;
                }
            } else { // o tubo tem bolas de diferentes cores

                bolasMal += (tubo.size() - alturaBase); //
            }
        }

        return bolasMal + ((double) tubosInc / 2);
    }


    /**
     * Calcula o valor de hash code
     * @return valor de hash calculado para o estado atual
     */
    @Override
    public int hashCode() {
        return board.hashCode();
    }

    /**
     * Retorna a representação textual do tabuleiro
     * @return string formatada com os chars das bolas
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < altura; i++) {
            int a = (altura - 1) - i;

            for (int j = 0; j < nTubos; j++) {
                Stack<Character> tubo = board.get(j);

                if (tubo.size() > a) str.append(tubo.get(a));
                else str.append(vazio);

                if (j < nTubos - 1) str.append(" ");
            }
            str.append(System.lineSeparator());
        }

        return str.toString();
    }
}
