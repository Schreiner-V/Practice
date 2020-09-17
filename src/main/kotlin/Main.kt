import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation
import javafx.geometry.Orientation.VERTICAL
import javafx.geometry.Side
import javafx.scene.control.TabPane
import javafx.scene.control.TextArea
import javafx.scene.text.Text
import org.mariuszgromada.math.mxparser.Expression
import tornadofx.*
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime
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

                        var inpStrMod = inputString.value.replace("\\s".toRegex(),"") // убираем пустоты
                        inpStrMod = inpStrMod.substringAfter("f(x)=") //после "f(x)="


                        /** очень криво,но работает*/
                        val parameters = inpStrMod.substringAfter(";")

                        val left = parameters.substringBefore(";").toDouble()
                        val right = parameters.substringBeforeLast(";").substringAfter(";").toDouble()
                        val accuracy = parameters.substringAfterLast(";").toDouble()

                        inpStrMod = inpStrMod.substringBefore(";")

                        val bis = bisection(left,right,accuracy,inpStrMod)

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

                            logsTextArea.appendText("Calculation finished in ${measured.duration}.\nУравнение f(x)=$inpStrMod на промежутке [$left;$right]\nимеет корень $bis\n" +
                                    "погрешность составляет $accuracy")
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
            textarea{}


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
fun replaceAndCount(string: String, value: Double): Double = string
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

        val fResult = replaceAndCount(function, value = result)
        val fLeft = replaceAndCount(function, value = newLeftEdge)

        if (fResult == 0.0) {
            break
        }

        if (fResult * fLeft < 0) {
            newRightEdge = result
        } else {
            newLeftEdge = result
        }
    }

    return result
}
//TODO производная
/*
fun replaceAndDerivatives(string: String,value: Double,degree: Double): Double {
    var expression = Expression("der($string),x,1")
    expression = expression.replace("x","$value")
    return expression.calculate()
}
*/


/*
fun interashion(left: Double, right: Double,accuracy: Double,function: String): Double {

    // Проверка
    var x1 = (left + right)/2.0

    var newLeftEdge:Double = left;
    var newRightEdge:Double = right;



    var fLeft = replaceAndCount(function,value = left)
    var fRight = replaceAndCount(function, value = right)

fun combination (left: Double, right: Double, accuracy: Double, f: (x: Double) -> Double, diff: (x: Double ) -> Double ): Double{
    require(f(left) * f(right) < 0) { "You have not assumed right left and right edges" }
    //http://dit.isuct.ru/IVT/sitanov/Literatura/M501/Pages/Glava2_5.htm
    var SecondDiff:Double = diff (diff( f(x) ) ) //
    var newLeftEdge:Double = left
    var newRightEdge:Double = right
    if diff(F)*SecondDiff(F) < 0{ //меняем значения
        newLeftEdge = newLeftEdge + newRightEdge
        newRightEdge = newLeftEdge - newRightEdge
        newLeftEdge = newLeftEdge - newRightEdge
    }
    while accuracy >= abs(newRightEdge - newLeftEdge){
        int newLeftEdge = newLeftEdge - (F(newLeftEdge)*(newRightEdge - newLeftEdge))/(F(newRightEdge) - F(newLeftEdge))
        int newRightEdge = newRightEdge - F(newRightEdge) / diff(F(newRightEdge))
    }
    result = abs(newRightEdge - newLeftEdge)
    return result
}

 */

fun main(args: Array<String>) {
    launch<MyApp>(args)
}
