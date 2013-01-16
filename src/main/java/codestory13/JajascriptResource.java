package codestory13;

import codestory13.jajascript.JajascriptOptimizer;
import codestory13.jajascript.Order;
import codestory13.jajascript.OrderPath;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@Path("/jajascript/optimize")
public class JajascriptResource {

    private Logger logger = LoggerFactory.getLogger(JajascriptResource.class);


    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public OrderPath optimize(String body) throws IOException {
        List<Order> orders = new ObjectMapper().readValue(body, new TypeReference<List<Order>>() { });
        return optimize(orders);
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public OrderPath optimize(List<Order> orders) {
        logger.info("Number of orders: {}", orders.size());
        if (orders == null) {
            return null;
        }
        OrderPath bestPath = new JajascriptOptimizer(orders).bestPath;
        logger.info("Best path gain : {}", bestPath.gain);
        return bestPath;
    }
}