package ui

import javafx.geometry.Orientation.VERTICAL
import javafx.scene.control.TextArea
import tornadofx.*

class TestsTab : Fragment("Тест") {

    private var logsTextArea: TextArea by singleAssign()

    override val root = form {
        fieldset("Тест", labelPosition = VERTICAL) {
            field {
                text = "1. В чем заключается комбинированный метод?"
            }
            togglegroup {
                radiobutton("Метод хорд сочитается с методом касательных", value = true)
                radiobutton("Метод бисекции используется во время метода итерации", value = false)
                radiobutton("Метод интерполирования комбинируют с методом конечной разности", value = false)
            }
        }
        fieldset( labelPosition = VERTICAL) {
            field {
                text = "2. Что получится если попытаться решить уравнение f(x)=|x|-3 методом бисекции?"
            }
            togglegroup {
                radiobutton("Уравнение невозможно решить методом бисекции", value = true)
                radiobutton("Будет найден корень 3 или -3, какой именно - предсказать невозможно", value = false)
                radiobutton("Приближение корня не будет происходить - результат будет колебаться от -3 до 3", value = false)
            }
        }
        fieldset( labelPosition = VERTICAL) {
            field {
                text = "3. Чем отличается метод Ньютона от метода касательных?"
            }
            togglegroup {
                radiobutton("Метод Ньютона требует единственный корень на исследуемом участке", value = false)
                radiobutton("Это один и тот же метод", value = true)
                radiobutton("Метод касательных, в отличии от метода Ньютона, не подходит для решения систем тригонометрических уравнений", value = false)
            }
        }

        button("Проверить") {
            disableProperty()//пока где-то нет ответа
            action {
                runAsync {
                    logsTextArea.clear()
                    logsTextArea.appendText("Результат теста: \n")
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