import junit.framework.Assert.assertEquals
import org.junit.jupiter.api.Test

class UtilsTest {

    // Влад, вот примерно так можно добавлять тесты. Проверять так гораздо удобнее и правильней

    @Test
    fun `test String calculate`() {
        assertEquals(2.0, "1+1".calculate())
    }

    @Test
    fun `test der`() {
        // derivative of sin is cos. Cos(1) = 0.540302305009277
        assertEquals(0.540302305009277, der("sin(x)", 1.0))
    }

    // Examples & test https://www.wolframalpha.com/examples/mathematics/calculus-and-analysis/derivatives/
    @Test
    fun `test secDer`() {
        // second derivative of sin(x) is -sin(x). -sin(1) = -0.841470982413739
        assertEquals(-0.841470982413739, secDer("sin(x)", 1.0))
    }

    @Test
    fun `test replaceAndCount`() {
        assertEquals(2.0, "1+x".replaceAndCount(1.0))
        assertEquals(0.0, "1-x".replaceAndCount(1.0))
        assertEquals(4.0, "2*x".replaceAndCount(2.0))
        assertEquals(1.0, "2/x".replaceAndCount(2.0))
    }
}
