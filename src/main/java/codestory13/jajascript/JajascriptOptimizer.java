package codestory13.jajascript;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.compare;

public class JajascriptOptimizer {
    public OrderPath bestPath = null;
    private Map<String, OrderPath> bestPathPerOrder = new HashMap<>();

    public JajascriptOptimizer(List<Order> orders) {
        orders.sort((a, b) -> compare(a.start, b.start));
        for (int i = orders.size() - 1; i >= 0; i--) {
            findBestCombinationStartingWith(i, orders);
        }
    }

    private void findBestCombinationStartingWith(int orderIndex, List<Order> orders) {
        Order startingOrder = orders.get(orderIndex);
        OrderPath bestCombinablePath = null;
        for (int i = orderIndex + 1; i < orders.size(); i++) {
            OrderPath orderPath = bestPathPerOrder.get(orders.get(i).flight);
            if (orderPath.canStartWith(startingOrder) && orderPath.isBetterThan(bestCombinablePath)) {
                bestCombinablePath = orderPath;
            }
        }
        OrderPath bestCombinationIncludingStartingOrder = new OrderPath(startingOrder, bestCombinablePath);
        bestPathPerOrder.put(startingOrder.flight, bestCombinationIncludingStartingOrder);
        if (bestCombinationIncludingStartingOrder.isBetterThan(bestPath)) {
            bestPath = bestCombinationIncludingStartingOrder;
        }
    }
}