package codestory13;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class BaseResourceTest {

    @Test
    public void should_initialize_mailer() throws Exception {
        new BaseResource();
        assertThat(BaseResource.mailer).isNotNull();
    }
}
