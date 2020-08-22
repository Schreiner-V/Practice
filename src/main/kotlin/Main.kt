import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation
import javafx.geometry.Orientation.VERTICAL
import javafx.geometry.Side
import javafx.scene.control.TabPane
import javafx.scene.control.TextArea
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
            field("Введите функцию") {
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
            textarea {
            }
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

//    println(
//        """Определение переменных !x = 3
//           |Определение функций !f(x) = x^2
//           |Поддерживаемые операторы: % ! ^ √ * / + - > < = & |
//           |Логические операторы буду считать любое число больше 1 - true, меньше - false
//           |Логические операторы возвращают 1 - true, 0 - false
//           |Обозначать отрицательные числа: _, а не -
//           |Чтобы остановить приложение введите exit""".trimMargin()
//    )
//
//    val env = ExpressionEnvironment()
//    println("Константы:")
//
//    env.parseAndPrint("!e = 3")
//    env.parseAndPrint("!p = 3.14")
//
//    while (true) {
//        println("Введите выражение:")
//        val exprRaw = readLine() ?: continue
//
//        if (exprRaw.equals("exit", ignoreCase = true)) break
//
//        env.parseAndPrint(exprRaw)
//    }
}

// TODO пока не смотрел - надо разобраться

/**
 * Сглаживает токены, указанные в списке, и упрощает их.
 */
fun flatten(tokens: ArrayList<Any>, vararg ops: Operation) {
    val tokenSwap = ArrayList<Any>()

    tokenSwap.addAll(tokens)

    while (!ops.none { tokens.contains(it) }) {
        println(tokens.joinToString(separator = " "))

        for (i in tokens.indices) {
            var topBreak = false
            for (o in ops) {
                if (tokens[i] != o) continue

                // левый и правый токен должен быть Double
                if (i == 0 && o.requireLeftValue()) {
                    throw IllegalArgumentException("Выражение не может начинаться с оператора: $o")
                } else if (i == tokens.size - 1 && o.requireRightValue()) {
                    throw IllegalArgumentException("Выражение не может заканчиваться оператором: $o")
                }

                if (o.requireRightValue() && o.requireLeftValue()) {
                    val a = tokens[i - 1] as? Double ?: throw IllegalArgumentException("Цепные операторы: $o и ${tokens[i - 1]}")
                    val b = tokens[i + 1] as? Double ?: throw IllegalArgumentException("Цепные операторы: $o и ${tokens[i + 1]}")

                    val v = o.execute(a, b)

                    val localIndex = findIndex(tokens[i - 1], tokens[i], tokens[i + 1], list = tokenSwap)
                    for (j in 0..2) {
                        tokenSwap.removeAt(localIndex)
                    }

                    tokenSwap.add(localIndex, v)
                    topBreak = true
                    break
                } else if (o.requireRightValue()) {
                    val b = tokens[i + 1] as? Double ?: throw IllegalArgumentException("Цепные операторы: $o и ${tokens[i + 1]}")

                    val v = o.execute(0.0, b)

                    val localIndex = findIndex(tokens[i], tokens[i + 1], list = tokenSwap)
                    for (j in 0..1) {
                        tokenSwap.removeAt(localIndex)
                    }

                    tokenSwap.add(localIndex, v)
                    topBreak = true
                    break
                } else if (o.requireLeftValue()) {
                    val a = tokens[i - 1] as? Double ?: throw IllegalArgumentException("Цепные операторы: $o и ${tokens[i - 1]}")

                    val v = o.execute(a, 0.0)

                    val localIndex = findIndex(tokens[i - 1], tokens[i], list = tokenSwap)
                    for (j in 0..1) {
                        tokenSwap.removeAt(localIndex)
                    }

                    tokenSwap.add(localIndex, v)
                    topBreak = true
                    break
                }
            }

            if (topBreak)
                break
        }

        tokens.clear()
        tokens.addAll(tokenSwap)
    }
}

/**
 * находит ииндекс в заданном списке ArrayList
 */
fun findIndex(vararg objs: Any, list: ArrayList<Any>) =
    list.indices.firstOrNull { i -> list[i] == objs[0] && objs.indices.none { list[i + it] != objs[it] } } ?: -1
