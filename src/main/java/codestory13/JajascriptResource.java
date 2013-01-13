package codestory13;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static java.util.Collections.sort;

@Path("/jajascript/optimize")
public class JajascriptResource {
    public JajascriptResource() {
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public OrderPath optimize(List<Order> orders) {
        if (orders == null) {
            return null;
        }
        sort(orders, (a, b) -> a.start.compareTo(b.start));
        OrderPath bestPath = null;
        return findTheBestPath(orders, bestPath);
    }

    private OrderPath findTheBestPath(List<Order> orders, OrderPath bestPath) {
        for (int i = 0; i < orders.size(); i++) {
            OrderPath currentPath = combinePossibleOrderAndKeepTheBestPath(i, orders);
            if (bestPath == null || currentPath.gain > bestPath.gain) {
                bestPath = currentPath;
            }
        }
        return bestPath;
    }

    private OrderPath combinePossibleOrderAndKeepTheBestPath(int startingFromOrderIndex, List<Order> ordersSortedByStart) {

        if (ordersSortedByStart == null || ordersSortedByStart.isEmpty()) {
            return null;
        }

        Order startingOrder = ordersSortedByStart.get(startingFromOrderIndex);
        OrderPath pathStartingWithStartingOrder = new OrderPath(
                startingOrder.price,
                startingOrder.flight);

        if (isLastOrder(startingFromOrderIndex, ordersSortedByStart)) {
            return pathStartingWithStartingOrder;
        }

        Order currentOrder;
        OrderPath bestPath = null;
        for (int currentOrderIndex = startingFromOrderIndex + 1; currentOrderIndex < (ordersSortedByStart.size()) ; currentOrderIndex++) {
            currentOrder = ordersSortedByStart.get(currentOrderIndex);
            OrderPath currentPath = combinePossibleOrderAndKeepTheBestPath(currentOrderIndex, ordersSortedByStart);
            if (ordersCanBeChained(startingOrder, currentOrder)) {
                if (bestPath == null || currentPath.gain > bestPath.gain) {
                    bestPath = currentPath;
                }
            }
        }

        if (bestPath != null) {
            pathStartingWithStartingOrder.add(bestPath);
        }
        return pathStartingWithStartingOrder;
    }

    private boolean ordersCanBeChained(Order startingOrder, Order nextOrder) {
        return nextOrder.start >= startingOrder.start + startingOrder.duration;
    }

    private boolean isLastOrder(int orderIndex, List<Order> ordersSortedByStart) {
        return orderIndex == (ordersSortedByStart.size()-1);
    }
}