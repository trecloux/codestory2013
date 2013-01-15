package codestory13.jajascript;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Integer.compare;
import static java.util.Collections.emptySet;

public class JajascriptOptimizer {
    public OrderPath bestPath = null;

    public JajascriptOptimizer(List<Order> orders) {
        orders.sort((a,b) -> compare(a.start, b.start));
        combinePossibleOrderAndKeepTheBestPath(0, orders);
    }

    private Set<OrderPath> combinePossibleOrderAndKeepTheBestPath(int index, List<Order> orders) {
        if (index == orders.size()) {
            return emptySet();
        }
        Set<OrderPath> possiblePaths = new HashSet<>();
        Order firstOrder = orders.get(index);
        rememberAddEvalCandidate(new OrderPath(firstOrder), possiblePaths);
        Set<OrderPath> subPaths = combinePossibleOrderAndKeepTheBestPath(index + 1, orders);
        possiblePaths.addAll(subPaths);
        for (OrderPath subPath : subPaths) {
            if (firstOrder.canBeChainedWith(subPath)) {
                rememberAddEvalCandidate(new OrderPath(firstOrder, subPath), possiblePaths);
            }
        }
        return possiblePaths;
    }

    private void rememberAddEvalCandidate(OrderPath path, Set<OrderPath> possiblePaths) {
        possiblePaths.add(path);
        if (path.isBetterThan(bestPath)) {
            bestPath = path;
        }
    }

}