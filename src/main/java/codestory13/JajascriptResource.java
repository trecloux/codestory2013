package codestory13;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static java.util.Collections.sort;

@Path("/jajascript/optimize")
public class JajascriptResource {

    private Logger logger = LoggerFactory.getLogger(JajascriptResource.class);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public OrderPath optimize(List<Order> orders) {
        logger.info("jajascript request : " + orders);
        if (orders == null) {
            return null;
        }
        sort(orders, (a, b) -> a.start.compareTo(b.start));
        OrderPath bestPath = findTheBestPath(orders, null);
        logger.info(bestPath.toString());
        return bestPath;
    }

    private OrderPath findTheBestPath(List<Order> orders, OrderPath bestPath) {
        for (int i = 0; i < orders.size(); i++) {
            OrderPath currentPath = combinePossibleOrderAndKeepTheBestPath(i, orders);
            if (currentPath.isBetterThan(bestPath)) {
                bestPath = currentPath;
            }
        }
        return bestPath;
    }

    private OrderPath combinePossibleOrderAndKeepTheBestPath(int startingIndex, List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            return null;
        }

        Order startingOrder = orders.get(startingIndex);
        OrderPath pathStartingWithStartingOrder = new OrderPath(startingOrder);

        if (isLastOrder(startingIndex, orders)) {
            return pathStartingWithStartingOrder;
        }

        Order currentOrder;
        OrderPath bestPath = null;
        for (int currentIndex = startingIndex + 1; currentIndex < orders.size() ; currentIndex++) {
            currentOrder = orders.get(currentIndex);
            OrderPath currentPath = combinePossibleOrderAndKeepTheBestPath(currentIndex, orders);
            if (ordersCanBeChained(startingOrder, currentOrder) && currentPath.isBetterThan(bestPath)) {
                bestPath = currentPath;
            }
        }

        pathStartingWithStartingOrder.add(bestPath);
        return pathStartingWithStartingOrder;
    }

    private boolean ordersCanBeChained(Order startingOrder, Order nextOrder) {
        return nextOrder.start >= startingOrder.start + startingOrder.duration;
    }

    private boolean isLastOrder(int orderIndex, List<Order> ordersSortedByStart) {
        return orderIndex == ordersSortedByStart.size()-1;
    }
}