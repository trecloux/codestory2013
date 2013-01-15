package codestory13.jajascript;

import java.util.ArrayList;
import java.util.List;

public class OrderPath {
    public Integer gain = new Integer(0);
    public List<String> path = new ArrayList<>();


    public OrderPath() {
        super();
    }

    public Integer getGain() {
        return gain;
    }

    public List<String> getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "gain : " + gain + ", path : " + path;
    }
}
