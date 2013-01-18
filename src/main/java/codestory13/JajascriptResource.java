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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Path("/jajascript/optimize")
public class JajascriptResource {

    private Logger logger = LoggerFactory.getLogger(JajascriptResource.class);

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response optimize(String body) throws IOException {
        if (body.isEmpty()) {
            logger.info("Received an empty body");
            return Response.status(BAD_REQUEST).entity("Empty body").build();
        } else {
            List<Order> orders = new ObjectMapper().readValue(body, new TypeReference<List<Order>>() { });
            if (orders != null && orders.size() <= 100) logger.info("String body : {}", body);
            return Response.ok(optimize(orders), MediaType.APPLICATION_JSON).build();
        }
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public OrderPath optimize(List<Order> orders) {
        if (orders == null) {
            return null;
        }
        logger.info("Number of orders: {}", orders.size());
        OrderPath bestPath = new JajascriptOptimizer(orders).bestPath;
        if (orders.size() <= 100) logger.info("Best path : {}", bestPath.getPath());
        logger.info("Best path gain : {}", bestPath.gain);
        return bestPath;
    }
}