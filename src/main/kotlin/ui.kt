import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation
import javafx.geometry.Side
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.NumberAxis
import javafx.scene.control.TabPane
import javafx.scene.control.TextArea
import org.mariuszgromada.math.mxparser.Expression
import tornadofx.*
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
        fieldset("Решение", labelPosition = Orientation.VERTICAL) {
            field("Введите функцию вида: f(x) = 3*sin(x) и перечислите через ';' границы промежутка и погрешность ") {

                textfield(inputString) {
                    requestFocus()
                }
                checkbox("Метод бисекции") { action{} }
                checkbox("Метод итерации") { action{} }
                checkbox("Комбинированный метод") { action{} }

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
                        val comb = combination(left, right, accuracy, inpStrMod)
                        val iter = iteration(left, right, accuracy,inpStrMod)

                        val arrayBis = graphic(-10.0, 10.0, inpStrMod)

                        linechart("График поведения функции", NumberAxis(), NumberAxis()) {
                            series("График f(x)") {
                                for (i in 1..100){
                                    println("[${arrayBis[i-1][0]}]-[${arrayBis[i-1][1]}]")
                                    data(arrayBis[i-1][0],arrayBis[i-1][1])
                                }
                            }
                        }

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

                            logsTextArea.appendText(
                                    "Calculation finished in ${measured.duration}.\nУравнение f(x)=$inpStrMod на промежутке [$left;$right]\nимеет корень $bis\n" +
                                            "погрешность составляет $accuracy" +
                                            "\n комбинаторный метод даёт:$comb"
                            )

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

        fieldset("Теория", labelPosition = Orientation.VERTICAL) {
            text("Введение")
        }
    }
}

class Tab3 : Fragment("Тест") {
    override val root = form {
        fieldset("Тест", labelPosition = Orientation.VERTICAL) {
            textarea {
            }
        }
    }
}



    fun graphic(left: Double,right: Double,function: String): Array<DoubleArray> {

        var frequency = (right - left)/100.0
        var newleft = left
        val points = Array(100) { DoubleArray(2) }

        for (i in 1..100){
            newleft += frequency
            points[i-1][0] = (newleft) // x
            points[i-1][1] = function.replaceAndCount((newleft)) // y
        }
    return points
    }

