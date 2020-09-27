import org.mariuszgromada.math.mxparser.Expression

fun main() {


    val bis1 = bisection(0.0,3.0,0.0001,"x*2 - 3")
    val bis2 = bisection(-10.0, 10.0, 0.001, "x^3+3*x^2+12*x+8")
    val bis3 = bisection(1.0, 5.0, 0.001, "ln(x)-x+2")

    val comb1 = combination(0.0,3.0,0.0001,"x*2 - 3")
    val comb2 = combination(-10.0, 10.0, 0.001, "x^3+3*x^2+12*x+8")
    val comb3 = combination(1.0, 5.0, 0.001, "ln(x)-x+2")

    val iter1 = iteration(0.0,3.0,0.0001,"3/2")
    val iter2 = iteration(-10.0, 10.0, 0.001, "-(x^3+3*x^2+8)/(12)")
    val iter3 = iteration(1.0, 5.0, 0.001, "ln(x)+2")
/*
    val der1 = Expression("cos(1)").calculate()
    val der2 = Expression("der(x^3-3*cos(x)-62,x,2.0)").calculate()
    val der3 = Expression("der(sin(x)/cos(x),x,15.0)").calculate()

    println(der1)
    println(der2)
    println(der3)


 */

    println("bisection = $bis1")
    println("bisection = $bis2")
    println("bisection = $bis3")
    println("comb = $comb1")
    println("comb = $comb2")
    println("comb = $comb3")
    println("iter = $iter1")
    println("iter = $iter2")
    println("iter = $iter3")


    //println("iteration = $iter")
}