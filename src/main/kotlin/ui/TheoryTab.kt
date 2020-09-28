package ui

import javafx.geometry.Orientation.VERTICAL
import tornadofx.*

class TheoryTab : Fragment("Теория") {
    override val root = form {
        fieldset("Теория", labelPosition = VERTICAL) {
            text("Введение")
        }
    }
}
