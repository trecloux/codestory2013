package codestory13.jajascript;

import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.compare;

public class JajascriptOptimizer {
    private static final Logger log = LoggerFactory.getLogger(JajascriptOptimizer.class);
    public OrderPath bestPath = null;
    private Map<String, OrderPath> bestPathPerOrder = new HashMap<>();

    public JajascriptOptimizer(List<Order> orders) {
        Stopwatch globalwatch = new Stopwatch().start();
        Stopwatch stopwatch = new Stopwatch().start();
        orders.sort((a, b) -> compare(a.start, b.start));
        stopwatch.stop();
        log.info("Sort took {}", stopwatch);
        stopwatch = new Stopwatch().start();
        for (int i = orders.size() - 1; i >= 0; i--) {
            findBestCombinationStartingWith(i, orders);

        }
        stopwatch.stop();
        log.info("loop took {}", stopwatch);
        globalwatch.stop();
        log.info("optimize took {}", globalwatch);

    }

    private void findBestCombinationStartingWith(int orderIndex, List<Order> orders) {
        Order startingOrder = orders.get(orderIndex);
        OrderPath bestCombinablePath = null;
        for (int i = orders.size() -1; i > orderIndex; i--) {
            OrderPath orderPath = bestPathPerOrder.get(orders.get(i).flight);
            if (orderPath.canStartWith(startingOrder)) {
                if (orderPath.isBetterThan(bestCombinablePath)) {
                    bestCombinablePath = orderPath;
                }
            } else {
                break;
            }
        }
        OrderPath bestCombinationIncludingStartingOrder = new OrderPath(startingOrder, bestCombinablePath);
        bestPathPerOrder.put(startingOrder.flight, bestCombinationIncludingStartingOrder);
        if (bestCombinationIncludingStartingOrder.isBetterThan(bestPath)) {
            bestPath = bestCombinationIncludingStartingOrder;
        }
    }
}