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
                        this.isDisable = true
                        runAsync {
                            logsTextArea.appendText(
                                """-----------------------
                                   |Calculation started...
                                   |""".trimMargin()
                            )
                            val measured = measureTimedValue {
                                Expression(inputString.value).calculate()
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


fun main(args: Array<String>) {
    launch<MyApp>(args)
}



