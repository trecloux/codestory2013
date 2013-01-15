package codestory13.jajascript;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.Integer.compare;

public class JajascriptOptimizer {
    public OrderPath bestPath = null;

    public JajascriptOptimizer(List<Order> orders) {
        orders.sort((a,b) -> compare(a.start, b.start));
        combinePossibleOrderAndKeepTheBestPath(0, orders);
    }

    private List<OrderPath> combinePossibleOrderAndKeepTheBestPath(int index, List<Order> orders) {
        List<OrderPath> possiblePaths = newArrayList();
        if (index == orders.size()) {
            return possiblePaths;
        }
        Order firstOrder = orders.get(index);
        rememberAndEvalCandidate(new OrderPath(firstOrder), possiblePaths);
        List<OrderPath> subPaths = combinePossibleOrderAndKeepTheBestPath(index + 1, orders);
        possiblePaths.addAll(subPaths);
        for (OrderPath subPath : subPaths) {
            if (firstOrder.canBeChainedWith(subPath)) {
                rememberAndEvalCandidate(new OrderPath(firstOrder, subPath), possiblePaths);
            }
        }
        return possiblePaths;
    }

    private void rememberAndEvalCandidate(OrderPath path, List<OrderPath> possiblePaths) {
        possiblePaths.add(path);
        if (path.isBetterThan(bestPath)) {
            bestPath = path;
        }
    }

}