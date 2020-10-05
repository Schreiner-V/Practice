package ui

import Method
import Method.BISECTION
import Method.COMBO
import Method.ITERATION
import bisection
import calculate
import combination
import iteration
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation
import javafx.scene.chart.NumberAxis
import javafx.scene.control.TextArea
import replaceAndCount
import tornadofx.*
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

class SolutionTab : Fragment("Решение") {

    private val inputString = SimpleStringProperty()
    private val leftBorder = SimpleStringProperty()
    private val rightBorder = SimpleStringProperty()
    private val accuracy = SimpleStringProperty()
    private var logsTextArea: TextArea by singleAssign()

    private val selectedMethod = SimpleObjectProperty<Method>()

    @ExperimentalTime
    override val root = form {
        fieldset("Решение", labelPosition = Orientation.VERTICAL) {
            field {
                label("f(x)=")
                textfield(inputString) {
                    promptText = "x^2"
                }
            }
            field {
                label("Левая граница")
                textfield(leftBorder) {
                    promptText = "-3"
                }
            }
            field {
                label("Правая граница")
                textfield(rightBorder) {
                    promptText = "32"
                }
            }
            field {
                label("Погрешность")
                textfield(accuracy) {
                    promptText = "0.001"
                }
            }
            field {
                togglegroup {
                    selectedMethod.bind(selectedValueProperty())
                    radiobutton("Метод бисекции", value = BISECTION) {
                        isSelected = true
                    }
                    radiobutton("Метод итерации", value = ITERATION)
                    radiobutton("Комбинированный метод", value = COMBO)
                }
            }
            field {
                button("Рассчитать") {
                    action {
                        this.isDisable = true
                        runAsync {
                            logsTextArea.appendText("-----------------------\nРасчёт начался...\n")

                            val timedValue = measureTimedValue {
                                handleRun(inputString.value, leftBorder.value, rightBorder.value, accuracy.value, selectedMethod.value)
                            }

                            logsTextArea.appendText(
                                """Уравнение f(x)=${inputString.value} на промежутке [${leftBorder.value};${rightBorder.value}]
                                   |имеет корень ${timedValue.value} погрешность составляет ${accuracy.value}.
                                   |Расчёт был выполнен за ${timedValue.duration}.
                                   |""".trimMargin()
                            )
                        } ui {
                            this.isDisable = false
                        }
                    }
                }
            }
        }

//        val arrayBis = graphic(-10.0, 10.0, inpStrMod)

//        linechart("График поведения функции", NumberAxis(), NumberAxis()) {
//            series("График f(x)") {
//                for (i in 1..100){
//                    println("[${arrayBis[i-1][0]}]-[${arrayBis[i-1][1]}]")
//                    data(arrayBis[i-1][0],arrayBis[i-1][1])
//                }
//            }
//        }

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

private fun handleRun(
    inputString: String,
    leftBorder: String,
    rightBorder: String,
    accuracyString: String,
    method: Method
): Double {
    val left = leftBorder.toDouble()
    val right = rightBorder.toDouble()
    val accuracy = accuracyString.toDouble()

    val result = when (method) {
        BISECTION -> bisection(left, right, accuracy, inputString)
        ITERATION -> iteration(left, right, accuracy, inputString)
        COMBO -> combination(left, right, accuracy, inputString)
    }.toString()

    return inputString
        .replace("x", result)
        .calculate()
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
