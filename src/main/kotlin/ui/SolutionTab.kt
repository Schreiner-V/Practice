package ui

import bisection
import calculate
import combination
import iteration
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation
import javafx.scene.control.TextArea
import javafx.scene.control.ToggleGroup
import replaceAndCount
import tornadofx.*
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

class SolutionTab : Fragment("Решение") {

    private val inputString = SimpleStringProperty()
    private val leftBorder = SimpleStringProperty()
    private val rightBorder = SimpleStringProperty()
    private val error = SimpleStringProperty()
    private var logsTextArea: TextArea by singleAssign()
    private val toggleGroup = ToggleGroup()

    @ExperimentalTime
    override val root = form {
        fieldset("Решение", labelPosition = Orientation.VERTICAL) {
            field("Введите функцию вида: f(x) = 3*sin(x) и перечислите через ';' границы промежутка и погрешность") {
                textfield(inputString) {
                    requestFocus()
                }
            }
            field("Укажите границы промежутка и погрешность") {
                textfield(leftBorder) {}
                textfield(rightBorder) {}
                textfield(error) {}
            }
            field {
                radiobutton("Метод бисекции", toggleGroup)
                radiobutton("Метод итерации", toggleGroup)
                radiobutton("Комбинированный метод", toggleGroup)
            }
            field {
                button("ОК") {
                    action {
                        this.isDisable = true
                        runAsync {
                            logsTextArea.appendText("-----------------------\nCalculation started...\n")

                            val duration = measureTime {
                                val inputString = handleRun(inputString.value)
                                inputString.calculate()
                            }

//                            logsTextArea.appendText(
//                                "Calculation finished in $duration.\nУравнение f(x)=$inputString на промежутке [$left;$right]\nимеет корень $bis\n" +
//                                        "погрешность составляет $accuracy" +
//                                        "\n комбинаторный метод даёт:$comb"
//                            )
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

private fun handleRun(inputString: String): String {
    var inpStrMod = inputString.replace("\\s".toRegex(), "") // убираем пустоты
    inpStrMod = inpStrMod.substringAfter("f(x)=") //после "f(x)="


    /** очень криво,но работает*/
    val parameters = inpStrMod.substringAfter(";")
    val left = parameters.substringBefore(";").toDouble()
    val right = parameters.substringBeforeLast(";").substringAfter(";").toDouble()
    val accuracy = parameters.substringAfterLast(";").toDouble()
    inpStrMod = inpStrMod.substringBefore(";")


    val bis = bisection(left, right, accuracy, inpStrMod)
    val comb = combination(left, right, accuracy, inpStrMod)
    // TODO!!! Не используется, надо бы удалить, либо использовать
    val iter = iteration(left, right, accuracy, inpStrMod)

    val arrayBis = graphic(-10.0, 10.0, inpStrMod)

    return inpStrMod
}

fun graphic(left: Double, right: Double, function: String): Array<DoubleArray> {
    val frequency = (right - left) / 100.0
    var newleft = left
    val points = Array(100) { DoubleArray(2) }

    for (i in 1..100) {
        newleft += frequency
        points[i - 1][0] = (newleft) // x
        points[i - 1][1] = function.replaceAndCount((newleft)) // y
    }
    return points
}
