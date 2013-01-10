import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("/")
public class Resource {

    @GET
    @Produces("text/plain;charset=utf-8")
    public String getAnswer(@QueryParam("q") String question) {
        if ("Quelle est ton adresse email".equals(question)) {
            return "tometjerem@gmail.com";
        } else if (question.startsWith("Es tu")) {
            return "OUI";
        } else if ("ping".equals(question)) {
            return "OK";
        } else {
            return "Vous pouvez répéter la question ?";
        }
    }
}