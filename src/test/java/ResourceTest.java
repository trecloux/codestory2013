import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class ResourceTest {

    @Test
    public void should_initialize_mailer() throws Exception {
        new Resource();
        assertThat(Resource.mailer).isNotNull();
    }
}
