package codestory13.jajascript;

import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.google.common.collect.Iterables.getLast;
import static java.lang.Integer.compare;

public class JajascriptOptimizer {
    private static final Logger log = LoggerFactory.getLogger(JajascriptOptimizer.class);
    public OrderPath bestPath = null;
    private OrderPath[] bestPathFromHour;

    public JajascriptOptimizer(List<Order> orders) {
        Stopwatch stopwatch = new Stopwatch().start();
        orders.sort((a, b) -> compare(a.start, b.start));
        bestPathFromHour = new OrderPath[getLast(orders).start + 1];
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
        OrderPath bestPathStartingSameHour = bestPathFromHour[order.start];
        if (bestSubPath.isBetterThan(bestPathStartingSameHour)) {
            bestPathFromHour[order.start] = bestSubPath;
        }
    }

    private OrderPath findNonOverlappingBestPath(Order order) {
        for(int hour = order.start + order.duration; hour < bestPathFromHour.length; hour++) {
            OrderPath path = bestPathFromHour[hour];
            if (path != null) {
                return new OrderPath(order, path);
            }
        }
        return new OrderPath(order, null);
    }

}