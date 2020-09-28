package ui

import javafx.geometry.Orientation.VERTICAL
import tornadofx.*

class TestsTab : Fragment("Тест") {
    override val root = form {
        fieldset("Тест", labelPosition = VERTICAL) {
            textarea {
            }
        }
    }
}
