import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BallSortTest {
    private String resolver(String input) {
        String[] linhas = input.strip().split("\\R");
        String[] dims = linhas[0].trim().split("\\s+");
        int altura = Integer.parseInt(dims[0]);
        int tubos = Integer.parseInt(dims[1]);

        StringBuilder config = new StringBuilder();
        for (int i = 1; i <= altura; i++) config.append(linhas[i]).append('\n');

        InformedSearch s = new InformedSearch();
        Iterator<InformedSearch.State> it =
                s.solve(new BallSortLayout(altura, tubos, config.toString()), null);

        if (it == null) return "no solution found";
        InformedSearch.State ultimo = null;
        while (it.hasNext()) ultimo = it.next();
        return String.valueOf((int) ultimo.getG());
    }

    @Test @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void T01() {
        assertEquals("23", resolver("""
                5 7
                G E E E R E E
                G P E E B E P
                B Y G E Y Y R
                R Y B E P B P
                G R R P B G Y
                """));
    }

    @Test @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void T02() {
        assertEquals("22", resolver("""
                5 7
                E E E P G E E
                G E E B P E E
                Y E R G Y B P
                Y R P B P R R
                Y G R B G Y B
                """));
    }

    @Test @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void T03() {
        assertEquals("23", resolver("""
                5 7
                P Y R P E R E
                G G G Y E P E
                R P B Y E Y E
                B B G B E R E
                G P R B E Y E
                """));
    }

    @Test @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void T04() {
        assertEquals("28", resolver("""
                6 7
                Y R R G B E E
                P R B Y B E E
                B R Y P G E E
                G R Y P G E E
                G R P Y B E E
                P P G Y B E E
                """));
    }

    @Test @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void T05() {
        assertEquals("32", resolver("""
                6 7
                R E E P R G G
                Y E E P R B R
                G E E R B P P
                G E E G B Y B
                Y E E Y Y B Y
                P E E B R P G
                """));
    }

    @Test @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void T06() {
        assertEquals("31", resolver("""
                6 7
                E B E E E E E
                E Y E P G R E
                E B E P G B E
                G P G Y R Y R
                B R G P B B G
                P R Y P Y R Y
                """));
    }

    @Test @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void T07() {
        assertEquals("37", resolver("""
                9 6
                B R Y E E R
                Y P P E E B
                B Y P E E B
                P R R E E Y
                P Y R E E R
                B P B E E R
                Y Y B E E Y
                P P R E E R
                P B Y E E B
                """));
    }

    @Test @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void T08() {
        assertEquals("37", resolver("""
                9 6
                B E E E E E
                P E P E E E
                Y E R E E E
                Y P R E R E
                Y P Y B B E
                R Y Y B P E
                B R B B R E
                B Y Y P P R
                R R B P P Y
                """));
    }

    @Test @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void T09() {
        assertEquals("46", resolver("""
                14 5
                B P E E B
                R P E E R
                P B E E R
                B R E E B
                P R E E P
                R R E E B
                B P E E R
                P B E E R
                P B E E P
                R B E E R
                P B E E B
                R R E E P
                P P E E R
                P B E E B
                """));
    }

    @Test @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void T10() {
        assertEquals("0", resolver("""
                2 3
                E R B
                E R B
                """));
    }

    // Nota: o input do T11 que enviaste é igual ao do T09
    @Test @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void T11() {
        assertEquals("46", resolver("""
                14 5
                B P E E B
                R P E E R
                P B E E R
                B R E E B
                P R E E P
                R R E E B
                B P E E R
                P B E E R
                P B E E P
                R B E E R
                P B E E B
                R R E E P
                P P E E R
                P B E E B
                """));
    }

    @Test @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void T12() {
        assertEquals("0", resolver("""
                3 6
                E E P R Y B
                E E P R Y B
                E E P R Y B
                """));
    }

    @Test @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void T13() {
        assertEquals("no solution found", resolver("""
                5 5
                P Y P R E
                B P B R E
                Y R Y Y E
                R P Y P E
                B B B R E
                """));
    }

    @Test @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void T14() {
        assertEquals("no solution found", resolver("""
                5 5
                P B P Y E
                Y Y Y B E
                P R B R E
                B P R P E
                R Y B R E
                """));
    }
}