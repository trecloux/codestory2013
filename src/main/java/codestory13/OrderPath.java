package codestory13;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OrderPath {
    Integer gain;
    List<String> path = new ArrayList<>();

    public OrderPath(Integer gain, String... path) {
        this.gain = gain;
        this.path = Lists.newArrayList(path);
    }

    public int getGain() {
        return gain;
    }

    public List<String> getPath() {
        return path;
    }

    public void add(OrderPath path) {
        gain = gain + path.gain;
        this.path.addAll(path.path);
    }
}
