package codestory13;

import org.codehaus.jackson.annotate.JsonProperty;

public class Order {
    @JsonProperty("VOL") String flight;
    @JsonProperty("DEPART") Integer start;
    @JsonProperty("DUREE") Integer duration;
    @JsonProperty("PRIX") Integer price;


    public Order() {
        super();
    }

    public Order(String flight, Integer start, Integer duration, Integer price) {
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
