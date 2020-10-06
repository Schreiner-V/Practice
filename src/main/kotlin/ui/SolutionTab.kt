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
import javafx.collections.FXCollections
import javafx.geometry.Orientation.VERTICAL
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.control.TextArea
import replaceAndCount
import tornadofx.*
import kotlin.math.abs
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

private const val POINTS_COUNT_IN_GRAPH = 100

class SolutionTab : Fragment("Решение") {

    private val inputString = SimpleStringProperty()
    private val leftBorder = SimpleStringProperty()
    private val rightBorder = SimpleStringProperty()
    private val accuracy = SimpleStringProperty()
    private var logsTextArea: TextArea by singleAssign()
    private val selectedMethod = SimpleObjectProperty<Method>() //выбранный метод -> на enum

    private val chartPoints = FXCollections.observableArrayList<XYChart.Data<Number, Number>>()

    @ExperimentalTime
    override val root = form {
        fieldset("Решение", labelPosition = VERTICAL) {
            field {
                label("f(x)=")
                textfield(inputString) {
                    promptText = "x"
                }
            }
            field {
                label("Левая граница")
                textfield(leftBorder) {
                    promptText = "-10"
                }
            }
            field {
                label("Правая граница")
                textfield(rightBorder) {
                    promptText = "10"
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
                    disableProperty() //кнопка неактивно пока:
                        .bind(inputString.isEmpty
                        .or(leftBorder.isEmpty)
                        .or(rightBorder.isEmpty)
                        .or(accuracy.isEmpty))
                    action {
                        val left = leftBorder.value.toDouble()
                        val right = rightBorder.value.toDouble()

                        runAsync {
                            logsTextArea.clear()
                            logsTextArea.appendText("-----------------------\nРасчёт начался...\n")

                            val timedValue = measureTimedValue {
                                handleRun(inputString.value, left, right, accuracy.value.toDouble(), selectedMethod.value)
                            }

                            logsTextArea.appendText(
                                """Уравнение f(x)=${inputString.value} на промежутке [${leftBorder.value};${rightBorder.value}]
                                   |имеет корень ${timedValue.value} погрешность составляет ${accuracy.value}.
                                   |Расчёт был выполнен за ${timedValue.duration}.
                                   |""".trimMargin()
                            )
                            logsTextArea.appendText("---------------------------------------------\n")

                        } ui {
                            chartPoints.clear()
                            chartPoints.addAll(calculateGraphPoints(left, right, inputString.value))
                        }
                    }
                }
            }
            field {
                linechart("График поведения функции", NumberAxis(), NumberAxis()) {
                    series("График f(x)", chartPoints) {}
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

private fun handleRun(
    inputString: String,
    leftBorder: Double,
    rightBorder: Double,
    accuracy: Double,
    method: Method
): String {
    val result = when (method) {
        BISECTION -> bisection(leftBorder, rightBorder, accuracy, inputString)
        ITERATION -> iteration(leftBorder, rightBorder, accuracy, inputString)
        COMBO -> combination(leftBorder, rightBorder, accuracy, inputString)
    }.toString()

    inputString
        .replace("x", result)
        .calculate()

    return result
}

private fun calculateGraphPoints(left: Double, right: Double, function: String): List<XYChart.Data<Number, Number>> {
    val piece = (abs(left) + abs(right)) / POINTS_COUNT_IN_GRAPH

    return (0..POINTS_COUNT_IN_GRAPH)
        .map {
            val x = left + piece * it
            XYChart.Data(x, function.replaceAndCount(x))
        }
}
