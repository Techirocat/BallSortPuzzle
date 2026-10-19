# Ball Sort Puzzle — Procura A* com Heurística

Resolução do **Ball Sort Puzzle** com o algoritmo de procura informada **A\***, desenvolvida no âmbito do trabalho prático TP2 da unidade curricular de Inteligência Artificial (2026/27).

O programa recebe uma configuração inicial do tabuleiro e devolve o **número de movimentos** da solução encontrada, ou `no solution found` se o puzzle não tiver solução.

## O puzzle

O tabuleiro tem vários tubos, todos com a mesma altura, e cada tubo contém bolas de várias cores.

- Em cada jogada move-se a bola do topo de um tubo para outro tubo.
- O tubo de destino tem de ter espaço, e tem de estar vazio ou ter no topo uma bola da mesma cor.
- O puzzle está resolvido quando cada tubo não vazio está cheio e contém bolas de uma só cor.

## Formato de entrada e saída

A primeira linha tem a altura dos tubos e o número de tubos. As linhas seguintes descrevem o tabuleiro de cima para baixo, com uma coluna por tubo. `E` representa uma posição vazia.

**Exemplo de entrada:**

```
5 7
G E E E R E E
G P E E B E P
B Y G E Y Y R
R Y B E P B P
G R R P B G Y
```

**Saída:**

```
23
```

## Estrutura do projeto

| Ficheiro | Descrição |
|---|---|
| `Ilayout.java` | Interface de um estado do problema: sucessores (`children`), teste de objetivo (`isGoal`), custo (`getK`) e heurística (`getH`) |
| `BallSortLayout.java` | Representação do tabuleiro, geração de sucessores, teste de objetivo e heurística |
| `InformedSearch.java` | Implementação do algoritmo A* |
| `Main.java` | Lê a entrada, corre a procura e imprime o resultado |
| `BallSortTest.java` | Testes JUnit 5 com os casos de teste do enunciado |

## Abordagem

### Representação do estado (`BallSortLayout`)

- O tabuleiro é uma `List<Stack<Character>>`, com uma pilha por tubo. O topo da pilha é a bola do topo do tubo.
- A entrada é lida de baixo para cima, para que as bolas fiquem empilhadas pela ordem certa. As posições `E` são ignoradas.
- O construtor de cópia faz uma *deep copy* das pilhas. Cada sucessor é uma cópia do estado atual com um movimento aplicado.
- `equals` e `hashCode` comparam o conteúdo dos tubos, o que permite guardar estados em `HashMap`.

### Geração de sucessores (`children`)

Para cada tubo não vazio, a bola do topo é movida para todos os tubos onde a jogada é válida (tubo com espaço e vazio, ou com o topo da mesma cor).

Quando há vários tubos vazios, só se gera o movimento para o primeiro. Mover a bola para qualquer outro tubo vazio daria um estado equivalente.

### Algoritmo A* (`InformedSearch`)

- Os estados abertos ficam numa `PriorityQueue`, ordenada por `f = g + h`. Em caso de empate, é escolhido o estado com maior `g`, ou seja, o que está mais avançado no caminho.
- O mapa `melhorState` guarda o menor custo `g` conhecido para cada layout. Um sucessor só entra na fila se o layout ainda não foi visto ou se o novo caminho até ele é mais barato. Esta verificação é O(1), ao contrário de procurar na fila.
- Quando se encontra um caminho mais barato para um estado já fechado, esse estado é retirado dos fechados para poder ser expandido de novo.
- Os sucessores que voltam ao estado pai são descartados logo à partida, para evitar desfazer a jogada anterior.
- Quando o estado retirado da fila é objetivo, o caminho é reconstruído seguindo os pais até ao estado inicial. O custo final é o `g` do último estado.

### Heurística (`getH`)

A heurística percorre os tubos e soma duas parcelas:

1. **Bolas mal colocadas.** Chama-se base ao bloco contíguo de bolas da mesma cor no fundo de um tubo. Num tubo com mais de uma cor, cada bola acima da base conta 1, porque tem de ser movida pelo menos uma vez.
2. **Tubos incompletos.** Cada tubo de uma só cor que ainda não está cheio conta 0,5, porque ainda vai precisar de receber bolas.

```
h = bolasMal + tubosIncompletos / 2
```

O custo de cada movimento (`getK`) é 1.

## Como executar

**Requisitos:** Java 25 ou superior. O `Main` usa o método `main` sem `public` e a classe `IO`, que só existem a partir desta versão.

```bash
javac Ilayout.java BallSortLayout.java InformedSearch.java Main.java
java Main < input.txt
```

## Testes

O ficheiro `BallSortTest.java` tem os 14 casos de teste do enunciado. Cada teste tem um limite de 1 segundo.

Para os correr no IntelliJ IDEA:

1. Adiciona o JUnit 5 ao projeto. Se `@Test` aparecer a vermelho, Alt+Enter e "Add JUnit 5 to classpath".
2. Clica com o botão direito em `BallSortTest` e escolhe **Run**.

Os testes têm de estar no mesmo package que `InformedSearch`, porque essa classe não é pública.

| Teste | Esperado | Teste | Esperado |
|---|---|---|---|
| T01 | 23 | T08 | 37 |
| T02 | 22 | T09 | 46 |
| T03 | 23 | T10 | 0 |
| T04 | 28 | T11 | 46 |
| T05 | 32 | T12 | 0 |
| T06 | 31 | T13 | no solution found |
| T07 | 37 | T14 | no solution found |