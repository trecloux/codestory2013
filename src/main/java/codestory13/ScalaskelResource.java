package codestory13;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static codestory13.ScalaskelResource.Coin.foo;
import static java.util.Arrays.asList;

@Path("/scalaskel")
public class ScalaskelResource {


    @GET
    @Path("/change/{cents}")
    @Produces(MediaType.APPLICATION_JSON)
    public Iterable<Map<Coin, Integer>> decompose(@PathParam("cents") Integer cents) {
        return combinaisons(cents, asList(Coin.values()));
    }

    private List<Map<Coin, Integer>> combinaisons(int cents, List<Coin> coins) {
        if (containsOnlyFooCoin(coins)) {
            return asList(oneElementCombinaison(foo, cents));
        } else {
            return notOnlyFooCombinaisons(cents, coins);
        }
    }

    private List<Map<Coin, Integer>> notOnlyFooCombinaisons(int totalToGet, List<Coin> coins) {
        List<Map<Coin, Integer>> combinaisons = new ArrayList<>();
        Coin currentCoin = coins.get(0);
        int currentValue = currentCoin.value;
        int maxNbOfCurrentCoin = totalToGet / currentValue;
        for (int nbOfCurrentCoin = 0; nbOfCurrentCoin <= maxNbOfCurrentCoin; nbOfCurrentCoin++) {
            int remaining = totalToGet - (nbOfCurrentCoin * currentValue);
            if (remaining == 0) {
                combinaisons.add(oneElementCombinaison(currentCoin, nbOfCurrentCoin));
            } else {
                List<Map<Coin, Integer>> subCombinaisons = combinaisons(remaining, coins.subList(1, coins.size()));
                addCombinaison(currentCoin, nbOfCurrentCoin, subCombinaisons);
                combinaisons.addAll(subCombinaisons);
            }
        }
        return combinaisons;
    }

    private void addCombinaison(Coin coin, int numberCoins, List<Map<Coin, Integer>> combinaisons) {
        if (numberCoins > 0) {
            combinaisons.forEach(m -> { m.put(coin, numberCoins); });
        }
    }

    private boolean containsOnlyFooCoin(List<Coin> coins) {
        return coins.size() == 1 && coins.contains(foo);
    }

    private Map<Coin, Integer> oneElementCombinaison(Coin coin, int number) {
        Map<Coin, Integer> map = new HashMap<>();
        map.put(coin, number);
        return map;
    }

    public static enum Coin {
        baz(21),
        qix(11),
        bar(7),
        foo(1);

        final int value;

        Coin(int value) {
            this.value = value;
        }
    }
}
