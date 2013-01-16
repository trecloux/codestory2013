package codestory13.jajascript;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class OrderPath {
    public int gain;
    private Order firstOrder;
    private OrderPath subPath;

    public OrderPath(Order firstOrder, OrderPath subPath) {
        this.firstOrder = firstOrder;
        this.subPath = subPath;
        gain = firstOrder.price;
        if (subPath != null) {
            gain += subPath.gain;
        }
    }

    public List<String> getPath() {
        List<String> path =  newArrayList();
        completePath(path);
        return path;
    }

    private void completePath(List<String> path) {
        path.add(firstOrder.flight);
        if (subPath != null) {
            subPath.completePath(path);
        }
    }

    public boolean canStartWith(Order order) {
        return (order.start + order.duration) <= firstOrder.start;
    }

    public boolean isBetterThan(OrderPath pathToCompare) {
        return pathToCompare == null || gain > pathToCompare.gain;
    }
}