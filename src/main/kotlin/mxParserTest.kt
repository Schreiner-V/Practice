import org.mariuszgromada.math.mxparser.Expression

fun main() {

    val bis = bisection(-10.0, 10.0, 0.001, "x^3+3*x^2+12*x+8")
    val comb = combination(-10.0, 10.0, 0.001, "x^3+3*x^2+12*x+8")
    val iter = iteration(-10.0, 10.0, 0.01, "(1/2)*(25/x+x)")

    println("bisection = $bis")
    println("combination = $comb")
    println("iteration = $iter")
}