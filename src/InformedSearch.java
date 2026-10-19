
import java.util.*;

/**
 * Classe que implementa o algoritmo de procura Uniform Cost Search
 */
class InformedSearch {
    protected Queue<State> abertos;
    protected Map<Ilayout, Double> melhorState;
    private Map<Ilayout, State> fechados;
    private State actual;
    private Ilayout objective;

    static class State {
        private Ilayout Ilayout;
        private State father;
        private double g;
        private double f;

        /**
         * Construtor da classe State
         * @param l O Ilayout do estado atual
         * @param n O estado pai (pode ser null para o estado inicial)
         */
        public State(Ilayout l, State n) {
            Ilayout = l;
            father = n;
            if (father != null) {
                g = father.g + l.getK();
                f = g + l.getH();
            }
            else{
                g = 0.0;
                f = l.getH();
            }
        }

        /**
         * Retorna a representação em texto do estado
         * @return String representando o Ilayout
         */
        public String toString() {
            return Ilayout.toString();
        }

        /**
         * Retorna o custo acumulado do estado
         * @return O valor do custo g
         */
        public double getG() {
            return g;
        }

        public double getF() {
            return f;
        }

        /**
         * Calcula o código hash para o estado
         * @return O valor de hash
         */
        public int hashCode() {
            return Ilayout.hashCode();
        }

        /**
         * Compara se dois estados são iguais, baseando-se no seu Ilayout
         * @param o O objeto a ser comparado
         * @return true se os estados forem iguais, false caso contrário
         */
        public boolean equals(Object o) {
            if (o == null) return false;
            if (this.getClass() != o.getClass()) return false;
            State n = (State) o;
            return this.Ilayout.equals(n.Ilayout);
        }
    }

    /**
     * Gera e retorna a lista de estados sucessores para um determinado estado
     * @param n O estado atual a partir do qual os sucessores são gerados
     * @return Uma lista de objetos State que são filhos do estado atual
     */
    final private List<State> sucessores(State n) {
        List<State> sucs = new ArrayList<>();
        List<Ilayout> children = n.Ilayout.children();
        for (Ilayout e : children) {
            if (n.father == null || !e.equals(n.father.Ilayout)) {
                State nn = new State(e, n);
                sucs.add(nn);
            }
        }
        return sucs;
    }

    /**
     * Executa o algoritmo de procura para encontrar o caminho do estado inicial até ao objetivo
     * @param s O Ilayout do estado inicial
     * @param goal O Ilayout do estado objetivo
     * @return Um iterador sobre a lista de estados que formam o caminho da solução ou null se não for encontrada nenhuma solução
     */
    final public Iterator<State> solve(Ilayout s, Ilayout goal) {
        objective = goal;

        abertos = new PriorityQueue<>(10, Comparator.comparingDouble(State::getF).thenComparingDouble(sta -> -sta.getG()));
        abertos.add(new State(s, null));

        fechados = new HashMap<>();
        melhorState = new HashMap<>();

        List<State> sucs;

        while (!abertos.isEmpty()) {
            actual = abertos.poll();

            if (actual.Ilayout.isGoal(objective)) {
                List<State> path = new ArrayList<>();
                State nn = actual;
                while (nn != null) {
                    path.add(nn);
                    nn = nn.father;
                }

                Collections.reverse(path);
                return path.iterator();
            }

            sucs = sucessores(actual);
            fechados.put(actual.Ilayout, actual);

            for (State st : sucs) {
                Double g = melhorState.get(st.Ilayout);
                if(g == null || st.g < g) {
                    melhorState.put(st.Ilayout, st.g);
                    abertos.add(st);
                    fechados.remove(st.Ilayout);
                }
            }
        }

        return null;
    }
}
