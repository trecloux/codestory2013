package codestory13;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import static codestory13.ScalaskelResource.Coin.*;
import static com.google.common.collect.ImmutableMap.of;
import static org.fest.assertions.Assertions.assertThat;


public class ScalaskelResourceTest {

    ScalaskelResource scalaskel = new ScalaskelResource();
    
    @Test
    public void should_convert_1_cent() throws Exception {
        assertDecomposition(1, of(foo, 1));
    }

    private void assertDecomposition(int cents, ImmutableMap<ScalaskelResource.Coin, Integer> ... expected) {
        assertThat(scalaskel.decompose(cents)).containsOnly(expected);
    }


    @Test
    public void should_convert_5_cents() throws Exception {
        assertDecomposition(5, of(foo, 5));
    }

    @Test
    public void should_convert_7_cents() throws Exception {
        assertDecomposition(7, of(bar, 1), of(foo, 7));
    }

    @Test
    public void should_convert_8_cents() throws Exception {
        assertDecomposition(8,
                of(bar, 1, foo, 1),
                of(foo, 8));
    }

    @Test
    public void should_convert_11_cents() throws Exception {
        assertDecomposition(11,
                of(qix, 1),
                of(bar, 1, foo, 4),
                of(foo, 11));
    }

    @Test
    public void should_convert_15_cents() throws Exception {
        assertDecomposition(15,
                of(qix, 1, foo, 4),
                of(bar, 1, foo, 8),
                of(bar, 2, foo, 1),
                of(foo, 15));
    }

    @Test
    public void should_convert_23_cents() throws Exception {
        assertDecomposition(23,
                of(baz, 1, foo, 2),
                of(qix, 2, foo, 1),
                of(qix, 1, bar, 1, foo, 5),
                of(qix, 1, foo, 12),
                of(bar, 3, foo, 2),
                of(bar, 2, foo, 9),
                of(bar, 1, foo, 16),
                of(foo, 23));
    }

}
