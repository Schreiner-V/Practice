package ui

import javafx.geometry.Side.BOTTOM
import javafx.scene.control.TabPane.TabClosingPolicy.UNAVAILABLE
import tornadofx.*

class RootView : View() {
    override val root =
        tabpane {
            tabClosingPolicy = UNAVAILABLE
            side = BOTTOM

            tab<SolutionTab>()
            tab<TheoryTab>()
            tab<TestsTab>()
        }
}
