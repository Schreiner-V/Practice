import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation.VERTICAL
import javafx.geometry.Side
import javafx.scene.control.TabPane
import javafx.scene.control.TextArea
import org.mariuszgromada.math.mxparser.Expression
import tornadofx.*
import kotlin.math.abs
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue


class MyApp : App(MyView::class)

class MyView : View() {
    override val root =
        tabpane {
            tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
            side = Side.BOTTOM

            tab<Tab1>()
            tab<Tab2>()
            tab<Tab3>()
        }
}

class Tab1 : Fragment("Решение") {

    private val inputString = SimpleStringProperty()

    private var logsTextArea: TextArea by singleAssign()

    @ExperimentalTime
    override val root = form {
        fieldset("Решение", labelPosition = VERTICAL) {
            field("Введите функцию вида: f(x) = 3*sin(x)") {

                textfield(inputString) {
                    requestFocus()
                }
                button("ОК") {
                    action {

                        var inpStrMod = inputString.value.replace("\\s".toRegex(), "") // убираем пустоты
                        inpStrMod = inpStrMod.substringAfter("f(x)=") //после "f(x)="


                        /** очень криво,но работает*/
                        val parameters = inpStrMod.substringAfter(";")
                        val left = parameters.substringBefore(";").toDouble()
                        val right = parameters.substringBeforeLast(";").substringAfter(";").toDouble()
                        val accuracy = parameters.substringAfterLast(";").toDouble()
                        inpStrMod = inpStrMod.substringBefore(";")



                        val bis = bisection(left, right, accuracy, inpStrMod)
                        //val comb = combination(left, right, accuracy, inpStrMod)

                        this.isDisable = true
                        runAsync {
                            logsTextArea.appendText(
                                    """-----------------------
                                   |Calculation started...
                                   |""".trimMargin()
                            )
                            val measured = measureTimedValue { //
                                inpStrMod.calculate()
                            }
                            val e = Expression("der(x^3, x,1.0, )")

                            logsTextArea.appendText("Calculation finished in ${measured.duration}.\nУравнение f(x)=$inpStrMod на промежутке [$left;$right]\nимеет корень $bis\n" +
                                        "погрешность составляет $accuracy" +
                                    "\n комбинаторный метод даёт: ")

                        } ui {
                            this.isDisable = false
                        }
                    }
                }
            }
        }
        fieldset("Ход выполнения") {
            textarea() {
                logsTextArea = this

                isEditable = false
                isMouseTransparent = true
                isFocusTraversable = false
            }
        }
    }
}

class Tab2 : Fragment("Теория") {
    override val root = form {
        fieldset("Теория", labelPosition = VERTICAL) {
            text("Введение")
        }
    }
}

class Tab3 : Fragment("Тест") {
    override val root = form {
        fieldset("Тест", labelPosition = VERTICAL) {
            textarea {
            }
        }
    }
}
//получаем x-3x
fun replaceAndCount(string: String, value: Double): Double = string // расширение класса  без наследования, https://tproger.ru/articles/kotlin-magic-functions/
        .replace("x", "$value")
        .calculate()

private fun String.calculate(): Double {
    val expression = Expression(this)
    return expression.calculate()
}
// TODO сделать вывод первых трех уточнений корня, погрешности, итерации,если корень посчитан точно, то сообщить и т.д.

fun bisection(left: Double, right: Double, accuracy: Double, function: String): Double {

    var newLeftEdge: Double = left
    var newRightEdge: Double = right
    var result: Double = newLeftEdge

    while (newRightEdge - newLeftEdge >= accuracy) {
        result = (newLeftEdge + newRightEdge) / 2

        val fResult = replaceAndCount(function, result)
        val fLeft = replaceAndCount(function, newLeftEdge)

        if (fResult == 0.0) {
            break }
        if (fResult * fLeft < 0) {
            newRightEdge = result
        } else {
            newLeftEdge = result
        }
    }

    println(secDer(function,1.0))

    return result
}

fun der(str: String,value: Double):Double{

    val e = Expression("der($str, x, $value)")
    return e.calculate()
}
//TODO функция второй производной, добавить расчетное время работы функции, доделать итарацию,убрать дичь на 50-й строке



/**НУЖНА ВТОРАЯ ПРОИЗВОДНАЯ, У МЕНЯ ВЫДАЕТ NAN*/
fun secDer(str: String,value: Double):Double {

    var ew = Expression("dern((x^3,x,3),2)") // https://mathparser.org/mxparser-math-collection/calculus-operators/
    println(ew.calculate())

    return ew.calculate()

}

fun combination (left: Double, right: Double, accuracy: Double, function: String ): Double {

    //http://cyclowiki.org/wiki/%D0%9A%D0%BE%D0%BC%D0%B1%D0%B8%D0%BD%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D0%BD%D0%BD%D1%8B%D0%B9_%D0%BC%D0%B5%D1%82%D0%BE%D0%B4
    //

    var a: Double = left //левый край
    var b: Double = right // правый край
    var fA = replaceAndCount(function, left) //функция от a
    var fB = replaceAndCount(function, right) // функция от b

    while (abs(a - b) > 2 * accuracy) {
        if (fA * secDer(function, a) < 0) {
            a -= (fA * (b - a)) / (fB - fA)
        } else if (fA * secDer(function, a) > 0) {
            a -= fA / der(function, a)
        } else if (fB * secDer(function, b) < 0) {
            b -= fB * (b - a) / (fB - fA)
        } else if (fB * secDer(function, b) > 0) {
            b -= fB / der(function, b)
        }
    }
    return (a + b)/2.0
}

fun main(args: Array<String>) {
    launch<MyApp>(args)
}
