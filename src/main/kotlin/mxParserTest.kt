import org.mariuszgromada.math.mxparser.Expression

fun main() {
    val e = Expression("2+2*4")
    val result = e.calculate()

    println("Result = $result")
}