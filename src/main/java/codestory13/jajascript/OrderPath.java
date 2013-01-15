package codestory13.jajascript;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class OrderPath {
    public final int gain;
    public final List<String> path;
    final Order firstOrder;

    public OrderPath(Order firstOrder) {
        gain = firstOrder.price;
        path = newArrayList(firstOrder.flight);
        this.firstOrder = firstOrder;

    }

    public OrderPath(Order firstOrder, OrderPath subPath) {
        gain = firstOrder.price + subPath.gain;
        path = newArrayList(firstOrder.flight);
        path.addAll(subPath.path);
        this.firstOrder = firstOrder;
    }

    public boolean isBetterThan(OrderPath otherPath) {
        return otherPath == null || gain > otherPath.gain;
    }

    @Override
    public String toString() {
        return "gain : " + gain + ", path : " + path;
    }
}
