import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sqrt

/** задаем операторы */
enum class Operation {
    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,
    POWER,
    MODULE,
    ROOT,
    FACTORIAL;
    /**SINE, // %  разобраться с префиксной записью
    ARCSINE,
    COSINE,//#
    ARCCOS,
    TANGENT,
    COTANGEL,
    ARCTANGEL,
    COSECANT,
    SECANT;*/

    /**описываем операторы*/

    companion object {
        // обработка оператора для символов
        fun getOperationForChar(c: Char) = when (c) {
            '+' -> PLUS
            '-' -> MINUS
            '/' -> DIVIDE
            '*' -> MULTIPLY
            '^' -> POWER
            '√' -> ROOT
            '!' -> FACTORIAL
            else -> null
        }

        fun isOperator(c: Char) = getOperationForChar(c) != null
    }
    // в разных случаях нам нужны или нет значение слева
    // TODO А почему всегда true возвращается? Может тогда просто true сразуи вернуть?
    fun requireLeftValue() = when (this) {
        FACTORIAL -> true
        else -> true
    }

    // TODO Я так понимаю эта функция потом будет изменяться, поэтому when, а не if
    fun requireRightValue() = when (this) {
        FACTORIAL -> false
        else -> true
    }

    fun execute(a: Double, b: Double) = when(this) {
        PLUS -> a + b
        MINUS -> a - b
        MULTIPLY -> a * b
        DIVIDE -> a / b
        POWER -> a.pow(b)
        MODULE -> a % b
        ROOT -> executeRoot(a, b)
        FACTORIAL -> executeFactorial(a)
    }

    private fun executeRoot(a: Double, b: Double) = when (a) {
        2.0 -> sqrt(b)
        3.0 -> Math.cbrt(b)
        else -> round(b.pow(1.0f / a))
    }

    private fun executeFactorial(a: Double): Double {
        if (a > 170.0) { // факториал считается до 170!
            throw IllegalArgumentException("Число под знаком факториала слишком велико $a")
        }

        return (1..a.toInt())
            // Каждый входящий Int превращаем во Double
            .map { it.toDouble() }
            // Производим умножение. accumulator - будет результат, current - новое значение
            // Так как цикл начинается с 1, допустим a = 3, то будет вот так:
            // 1 - accumulator просто будет равен 1
            // 2 - accumulator = 1, current = 2 -> 1 * 2 = 2
            // 3 - accumulator = 2, current = 3 -> 2 * 3 = 3
            .reduce { accumulator, current -> accumulator * current }
    }
}