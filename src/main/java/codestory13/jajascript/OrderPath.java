package codestory13.jajascript;

import java.util.ArrayList;
import java.util.List;

public class OrderPath {
    public int gain;
    public List<String> path = new ArrayList<>();

    @Override
    public String toString() {
        return "gain : " + gain + ", path : " + path;
    }
}