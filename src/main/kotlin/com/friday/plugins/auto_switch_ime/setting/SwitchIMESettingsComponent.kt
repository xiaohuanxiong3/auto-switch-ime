package com.friday.plugins.auto_switch_ime.setting

import com.intellij.ui.IdeBorderFactory
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JPanel

class SwitchIMESettingsComponent {

    private val mainPanel : JPanel = JPanel()

    private val javaCheckBox : JCheckBox = JCheckBox("Java")

    private val kotlinCheckBox : JCheckBox = JCheckBox("Kotlin")

    fun getPanel() : JPanel {
        initLayout()
        return mainPanel
    }

    private fun initLayout() {
        val singleLanguageSetting = titledPanel("设置单语言是否开启输入法自动切换") {
            add(javaCheckBox,UI.fillX().wrap())
            add(kotlinCheckBox,UI.fillX().wrap())
        }

        mainPanel.addVertically(singleLanguageSetting)
    }

    fun getJavaCheckBox() : JCheckBox {
        return javaCheckBox
    }

    fun getKotlinCheckBox() : JCheckBox {
        return kotlinCheckBox
    }

    companion object {

        private fun titledPanel(title: String, fill: Boolean = false, body: JPanel.() -> Unit): JComponent {
            val innerPanel = JPanel(UI.migLayout(UI.migSize(4)))
            innerPanel.body()
            return JPanel(UI.migLayout()).apply {
                border = IdeBorderFactory.createTitledBorder(title)
                if (fill) {
                    add(innerPanel, UI.fillX())
                } else {
                    add(innerPanel)
                    add(JPanel(), UI.fillX())
                }
            }
        }

        private fun JPanel.addVertically(vararg components: JComponent) {
            layout = UI.migLayoutVertical()
            components.forEach {
                add(it, UI.fillX())
            }
            add(JPanel(), UI.fillY())
        }

    }
}