import junit.framework.Assert.assertEquals
import org.junit.jupiter.api.Test


class UtilsTest {

    // Влад, вот примерно так можно добавлять тесты. Проверять так гораздо удобнее и правильней

    @Test
    fun `test String calculate`() {
        assertEquals(2.0, "1+1".calculate())
        assertEquals(3.0, "9.53 - 6.53 + 32 * 0".calculate())
        assertEquals(0.5403023058681398, "cos(1)".calculate())
    }

    @Test
    fun `test der`() {
        // derivative of sin is cos. Cos(1) = 0.540302305009277
        assertEquals(0.540302305009277, der("sin(x)", 1.0))
        assertEquals(14.727892281734967, der("x^3-3*cos(x)-62", 2.0))
        assertEquals(1.7327247034745596, der("sin(x)/cos(x)", 15.0))
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

    @Test
    fun `test bisection`() {
        assertEquals(1.5, bisection(0.0, 3.0, 0.0001, "x*2 - 3"))
        assertEquals(-0.77850341796875, bisection(-10.0, 12.0, 0.001, "x^3+3*x^2+12*x+8"))
        assertEquals(3.1455078125, bisection(1.0, 5.0, 0.001, "ln(x)-x+2"))
    }

    @Test
    fun `test combination`() {
        assertEquals(1.5, combination(0.0, 3.0, 0.0001, "x*2 - 3"))
        assertEquals(-0.7789543046451879, combination(-10.0, 12.0, 0.001, "x^3+3*x^2+12*x+8"))
        assertEquals(3.1461930966412504, combination(1.0, 5.0, 0.001, "ln(x)-x+2"))
    }

    @Test
    fun `test iteration`() {
        assertEquals(1.5, iteration(0.0, 3.0, 0.0001, "3/2"))
        assertEquals(-0.779128585614236, iteration(-10.0, 10.0, 0.001, "-(x^3+3*x^2+8)/(12)"))
        assertEquals(3.1460268482614193, iteration(1.0, 5.0, 0.001, "ln(x)+2"))
    }
}