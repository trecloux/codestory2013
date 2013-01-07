import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/")
public class Resource {

    @GET
    @Produces("text/plain")
    public String getAnswer() {
        return "tometjerem@gmail.com";
    }
}