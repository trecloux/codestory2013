package codestory13.jajascript;

import org.codehaus.jackson.annotate.JsonProperty;

public class Order {
    @JsonProperty("VOL") String flight;
    @JsonProperty("DEPART") int start;
    @JsonProperty("DUREE") int duration;
    @JsonProperty("PRIX") int price;

    public Order() {}

    public Order(String flight, int start, int duration, int price) {
        this.flight = flight;
        this.start = start;
        this.duration = duration;
        this.price = price;
    }

    @Override
    public String toString() {
        return "flight : " + flight + ", start : " + start + ", duration : " + duration + ", price : " + price;
    }
}
