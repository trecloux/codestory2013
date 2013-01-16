package codestory13.jajascript;

import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Iterables.getLast;
import static java.lang.Integer.compare;

public class JajascriptOptimizer {
    private static final Logger log = LoggerFactory.getLogger(JajascriptOptimizer.class);
    public OrderPath bestPath = null;
    private Map<Integer,OrderPath> bestPathFromHour = new HashMap<>();
    private int maxStartHour;

    public JajascriptOptimizer(List<Order> orders) {
        Stopwatch stopwatch = new Stopwatch().start();
        orders.sort((a, b) -> compare(a.start, b.start));
        maxStartHour = getLast(orders).start;
        for (int i = orders.size() - 1; i >= 0; i--) {
            findBestCombinationStartingWith(i, orders);
        }
        stopwatch.stop();
        log.info("optimize took {}", stopwatch);

    }

    private void findBestCombinationStartingWith(int orderIndex, List<Order> orders) {
        Order order = orders.get(orderIndex);
        OrderPath bestSubPath = findNonOverlappingBestPath(order);
        if (bestSubPath.isBetterThan(bestPath)) {
            bestPath = bestSubPath;
        }
        OrderPath bestPathStartingSameHour = bestPathFromHour.get(order.start);
        if (bestSubPath.isBetterThan(bestPathStartingSameHour)) {
            bestPathFromHour.put(order.start, bestSubPath);
        }
    }

    private OrderPath findNonOverlappingBestPath(Order order) {
        int startingHour = order.start + order.duration;
        while (startingHour <= maxStartHour) {
            OrderPath path = bestPathFromHour.get(startingHour);
            if (path != null) {
                return new OrderPath(order, path);
            }
            startingHour ++;
        }
        return new OrderPath(order, null);
    }

}