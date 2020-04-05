package guru.springframework;

import java.util.HashMap;
import java.util.Map;

public class Bank {

    private Map<Pair, Integer> rateMap = new HashMap<>();

    public void addRate(String from, String to, int rate) {
        if (from.equals(to)) {
            return;
        }

        Pair pair = new Pair(from, to);
        rateMap.put(pair, rate);
    }

    public int rate(String from, String to) {
        if (from.equals(to)) {
            return 1;
        }

        return rateMap.get(new Pair(from, to));
    }
}
