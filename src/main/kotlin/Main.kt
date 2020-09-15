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

                // TODO обработка строки

                textfield(inputString) {
                    requestFocus()
                }
                button("ОК") {
                    action {

                        var inpStrMod = inputString.value.replace("\\s".toRegex(),"") // убираем нули
                        inpStrMod = inpStrMod.substringAfter("f(x)=") //после "f(x)="

                        this.isDisable = true
                        runAsync {
                            logsTextArea.appendText(
                                """-----------------------
                                   |Calculation started...
                                   |""".trimMargin()
                            )
                            val measured = measureTimedValue {
                                Expression(inpStrMod).calculate()
                            }
                            logsTextArea.appendText("Calculation finished in ${measured.duration}. Result = ${measured.value}\n")
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

/*fun bisection(left: Double,right: Double,accuracy: Double){

    var newLeftEdge:Double = left
    var newRightEdge:Double = right
    var result:Double = newLeftEdge

    while (newRightEdge - newLeftEdge >= accuracy) {
        result = (newLeftEdge + newRightEdge) / 2
        //замена + калькулятор для result
        
        if (f(result) == 0.0){
            break
        }
        if (f(result) * f(newLeftEdge) < 0) {
            newRightEdge = result
        }
        else{
            newLeftEdge = result
        }
    }
    return result
}
*/
fun replaceAndCount(){

}
/*fun combination (left: Double, right: Double, accuracy: Double, f: (x: Double) -> Double, diff: (x: Double ) -> Double ): Double{

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


fun interashion(left: Double, right: Double,accuracy: Double, F: (x: Double) -> Double): Double {
    // Проверка

    require(F(left) * F(right) < 0) { "You have not assumed right left and right edges" }

    //http://www.machinelearning.ru/wiki/index.php?title=%D0%9C%D0%B5%D1%82%D0%BE%D0%B4_%D0%BF%D1%80%D0%BE%D1%81%D1%82%D1%8B%D1%85_%D0%B8%D1%82%D0%B5%D1%80%D0%B0%D1%86%D0%B8%D0%B9
    //https://www.youtube.com/watch?v=fkkYVuUPzxc


    var newLeftEdge:Double = left
    var newRightEdge:Double = right

    var x0:Double = (left + right) / 2
    var result:Double = F(x0);

    do {
        x0 = result
        result = f(x0)
        if (newRightEdge < x0 < newLeftEdge){
            break
        }
        while abs( x0 - x1 ) < accuracy
    }

    return result

}
*/

fun main(args: Array<String>) {
    launch<MyApp>(args)
}



