package codestory13.jajascript;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class OrderPath {
    public int gain;
    public List<String> path = new ArrayList<>();
    private Order firstOrder;

    public OrderPath(Order firstOrder) {
        this.firstOrder = firstOrder;
        path = newArrayList(firstOrder.flight);
        gain = firstOrder.price;
    }

    public OrderPath(Order firstOrder, OrderPath subPath) {
        this(firstOrder);
        if (subPath != null) {
            path.addAll(subPath.path);
            gain += subPath.gain;
        }
    }

    public boolean canStartWith(Order order) {
        return (order.start + order.duration) <= firstOrder.start;
    }

    @Override
    public String toString() {
        return "gain : " + gain + ", path : " + path;
    }

    public boolean isBetterThan(OrderPath pathToCompare) {
        return pathToCompare == null || gain > pathToCompare.gain;
    }
}