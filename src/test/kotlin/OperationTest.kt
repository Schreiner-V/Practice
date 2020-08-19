import Operation.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class OperationTest {

    @Test
    fun `test getOperationForChar`() {
        assertEquals(Operation.getOperationForChar('+'), PLUS)
        assertEquals(Operation.getOperationForChar('-'), MINUS)
        assertEquals(Operation.getOperationForChar('/'), DIVIDE)
        assertEquals(Operation.getOperationForChar('*'), MULTIPLY)
        assertEquals(Operation.getOperationForChar('^'), POWER)
        assertEquals(Operation.getOperationForChar('√'), ROOT)
        assertEquals(Operation.getOperationForChar('!'), FACTORIAL)
        assertEquals(Operation.getOperationForChar('&'), null)
    }

    @Test
    fun `test isOperator`() {
        assertEquals(Operation.isOperator('+'), true)
        assertEquals(Operation.isOperator('-'), true)
        assertEquals(Operation.isOperator('/'), true)
        assertEquals(Operation.isOperator('*'), true)
        assertEquals(Operation.isOperator('^'), true)
        assertEquals(Operation.isOperator('√'), true)
        assertEquals(Operation.isOperator('!'), true)
        assertEquals(Operation.isOperator('&'), false)
    }

    @Test
    fun `test requireLeftValue`() {
        assertEquals(PLUS.requireLeftValue(), true)
        assertEquals(MINUS.requireLeftValue(), true)
        assertEquals(DIVIDE.requireLeftValue(), true)
        assertEquals(MULTIPLY.requireLeftValue(), true)
        assertEquals(POWER.requireLeftValue(), true)
        assertEquals(ROOT.requireLeftValue(), true)
        assertEquals(FACTORIAL.requireLeftValue(), true) // TODO мне кажется тут должен быть false
    }

    @Test
    fun `test requireRightValue`() {
        assertEquals(PLUS.requireRightValue(), true)
        assertEquals(MINUS.requireRightValue(), true)
        assertEquals(DIVIDE.requireRightValue(), true)
        assertEquals(MULTIPLY.requireRightValue(), true)
        assertEquals(POWER.requireRightValue(), true)
        assertEquals(ROOT.requireRightValue(), true)
        assertEquals(FACTORIAL.requireRightValue(), false)
    }

    @Test
    fun `test execute`() {
        assertEquals(2.0, PLUS.execute(1.0, 1.0))
        assertEquals(2.0, MINUS.execute(3.0, 1.0))
        assertEquals(4.0, MULTIPLY.execute(2.0, 2.0))
        assertEquals(2.0, DIVIDE.execute(4.0, 2.0))
        assertEquals(8.0, POWER.execute(2.0, 3.0))
        assertEquals(1.0, MODULE.execute(4.0, 3.0))
        assertEquals(2.0, ROOT.execute(2.0, 4.0))
        assertEquals(2.0, ROOT.execute(3.0, 8.0))
        assertEquals(2.0, ROOT.execute(5.0, 64.0))
        assertEquals(6.0, FACTORIAL.execute(3.0, Double.NaN))
        assertEquals(7.257415615307994E306, FACTORIAL.execute(170.0, Double.NaN))
        assertThrows(IllegalArgumentException::class.java) {
            FACTORIAL.execute(171.0, Double.NaN)
        }
    }
}