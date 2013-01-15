package codestory13.jajascript;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.compare;

public class JajascriptOptimizer {
    public OrderPath bestPath = null;
    private Map<String, OrderCombination> bestCombinationPerOrder = new HashMap<>();
    private OrderCombination bestCombination = null;

    public JajascriptOptimizer(List<Order> orders) {
        orders.sort((a, b) -> compare(a.start, b.start));
        for (int i = orders.size() - 1; i >= 0; i--) {
            findBestCombinationStartingWith(i, orders);
        }
        bestPath = bestCombination.getOrderPath();
    }

    private void findBestCombinationStartingWith(int orderIndex, List<Order> orders) {
        Order startingOrder = orders.get(orderIndex);
        OrderCombination bestCombinableCombination = null;

        for (int i = orderIndex + 1; i < orders.size(); i++) {
            OrderCombination orderCombination = bestCombinationPerOrder.get(orders.get(i).flight);
            if (orderCombination.canStartWith(startingOrder) && orderCombination.isBetterThan(bestCombinableCombination)) {
                bestCombinableCombination = orderCombination;
            }
        }

        OrderCombination bestCombinationIncludingStartingOrder = createNewCombination(startingOrder, bestCombinableCombination);
        bestCombinationPerOrder.put(startingOrder.flight, bestCombinationIncludingStartingOrder);

        if (bestCombinationIncludingStartingOrder.isBetterThan(bestCombination)) {
            bestCombination = bestCombinationIncludingStartingOrder;
        }

    }

    private OrderCombination createNewCombination(Order startingOrder, OrderCombination orderCombinationToAppend) {
        OrderCombination newOrderCombination = new OrderCombination();
        newOrderCombination.add(startingOrder, orderCombinationToAppend);
        return newOrderCombination;
    }

}