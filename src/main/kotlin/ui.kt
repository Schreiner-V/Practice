import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation
import javafx.geometry.Side
import javafx.scene.control.TabPane
import javafx.scene.control.TextArea
import org.mariuszgromada.math.mxparser.Expression
import tornadofx.*
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

class MyApp : App(MyView::class)

class MyView : View() {
    override val root =
<<<<<<< HEAD
            tabpane {
                tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
                side = Side.BOTTOM

                tab<Tab1>()
                tab<Tab2>()
                tab<Tab3>()
            }
=======
        tabpane {
            tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
            side = Side.BOTTOM

            tab<Tab1>()
            tab<Tab2>()
            tab<Tab3>()
        }
>>>>>>> a2c8976ab101d1d5291bf1c93e00443c9adff276
}

class Tab1 : Fragment("Решение") {

    private val inputString = SimpleStringProperty()

    private var logsTextArea: TextArea by singleAssign()

    @ExperimentalTime
    override val root = form {
        fieldset("Решение", labelPosition = Orientation.VERTICAL) {
            field("Введите функцию вида: f(x) = 3*sin(x)") {

                textfield(inputString) {
                    requestFocus()
                }

                /*
                checkbox("Метод бисекции") { action{} }
                checkbox("Метод итерации") { action{
                    textfield(""){}//Для итерации нужен ввод обратной функции
                } }
                checkbox("Комбинированный метод") { action{} }

                 */

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
<<<<<<< HEAD
                                    """-----------------------
=======
                                """-----------------------
>>>>>>> a2c8976ab101d1d5291bf1c93e00443c9adff276
                                   |Calculation started...
                                   |""".trimMargin()
                            )
                            val measured = measureTimedValue { //
                                inpStrMod.calculate()
                            }
                            val e = Expression("der(x^3, x,1.0, )")

                            logsTextArea.appendText(
<<<<<<< HEAD
                                    "Calculation finished in ${measured.duration}.\nУравнение f(x)=$inpStrMod на промежутке [$left;$right]\nимеет корень $bis\n" +
                                            "погрешность составляет $accuracy" +
                                            "\n комбинаторный метод даёт: "
=======
                                "Calculation finished in ${measured.duration}.\nУравнение f(x)=$inpStrMod на промежутке [$left;$right]\nимеет корень $bis\n" +
                                        "погрешность составляет $accuracy" +
                                        "\n комбинаторный метод даёт: "
>>>>>>> a2c8976ab101d1d5291bf1c93e00443c9adff276
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
