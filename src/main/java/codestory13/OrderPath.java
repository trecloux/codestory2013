package codestory13;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class OrderPath {
    Integer gain;
    List<String> path = new ArrayList<>();

    public OrderPath(Order firstOrder) {
        gain = firstOrder.price;
        path = newArrayList(firstOrder.flight);
    }

    public void add(OrderPath subPath) {
        if (subPath != null) {
            gain = gain + subPath.gain;
            path.addAll(subPath.path);
        }
    }

    public boolean isBetterThan(OrderPath otherPath) {
        return otherPath == null || gain > otherPath.gain;
    }

    public Integer getGain() {
        return gain;
    }

    public List<String> getPath() {
        return path;
    }
}
