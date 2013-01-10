import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("/")
public class Resource {

    @GET
    @Produces("text/plain")
    public String getAnswer(@QueryParam("q") String question) {
        if ("Quelle est ton adresse email".equals(question)) {
            return "tometjerem@gmail.com";
        } else if ("Es tu abonne a la mailing list(OUI/NON)".equals(question)) {
            return "OUI";
            } else if ("ping".equals(question)) {
            return "OK";
        } else {
            return "Vous pouvez répéter la question ?";
        }
    }
}