package com.sqy.plugins.translation.setting

import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.components.labels.LinkLabel
import com.intellij.ui.scale.JBUIScale
import com.intellij.workspaceModel.storage.cpp.CppLanguage
import com.sqy.plugins.translation.engine.TranslationEngineEnum
import com.sqy.plugins.translation.ui.UI.migLayout
import com.sqy.plugins.translation.ui.UI.migLayoutVertical
import net.miginfocom.layout.CC
import java.awt.event.ItemEvent
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class TranslationSettingsComponent {

    private val mainPanel : JPanel = JPanel()

    private val engineSelectComboBox : ComboBox<TranslationEngineEnum> = ComboBox(TranslationEngineEnum.values()).apply {
        renderer = SimpleListCellRenderer.create { label, value, _ ->
            label.text = value.translateEngineName
            label.icon = value.icon
        }

        this.addItemListener {
            if (it.stateChange == ItemEvent.SELECTED) {
                determineConfigLinkVisible()
            }
        }

    }

    fun getSelectedEngine() : TranslationEngineEnum? {
        return engineSelectComboBox.selectedItem.run {
            this as? TranslationEngineEnum
        }
    }

    fun setSelectedEngine(engine : TranslationEngineEnum?) {
        engineSelectComboBox.selectedItem = engine
    }

    private val configLink : LinkLabel<String> = LinkLabel<String>("配置...",null).apply {
        setListener({ _: LinkLabel<String>, _ ->
            engineSelectComboBox.selectedItem.run {
                this as? TranslationEngineEnum
            }?.showConfigDialog()
        },
        null)
    }


    private val languageSelectComboBox : ComboBox<String> = ComboBox<String>().apply {
        this.addItem("zh")
        this.addItem("en")
    }

    fun getPrimaryLanguage() : String? {
        return  languageSelectComboBox.selectedItem.run {
            this as? String
        }
    }

    fun setPrimaryLanguage(primaryLanguage: String?) {
        languageSelectComboBox.selectedItem = primaryLanguage
    }

    private fun initLayout() {
        val general = titledPanel("常规") {
//            val labelGroup = "label"
            val comboBoxGroup = "comboBox"

//            add(JLabel("翻译引擎："),CC().sizeGroupX(labelGroup))
            add(JLabel("翻译引擎："))
            add(engineSelectComboBox,CC().sizeGroupX(comboBoxGroup).minWidth(migSize(200)))
            add(configLink, warp().gapLeft(JBUIScale.scale(2).toString()).spanX(2))

//            add(JLabel("主语言："),CC().sizeGroupX(labelGroup))
            add(JLabel("主语言："))
            add(languageSelectComboBox, warp().sizeGroupX(comboBoxGroup))

        }

        mainPanel.addVertically(
                general
        )
    }

    fun getPanel() : JPanel {
        initLayout()
        determineConfigLinkVisible()
        return mainPanel
    }

    /**
     * 决定配置链接是否可见，因为有些翻译引擎不需要进行配置
     */
    private fun determineConfigLinkVisible() {
        val t = engineSelectComboBox.selectedItem?.run {
            this as? TranslationEngineEnum
        }
        configLink.isVisible = t?.hasConfiguration() ?: false
    }

    companion object {

        private fun titledPanel(title: String, fill: Boolean = false, body: JPanel.() -> Unit): JComponent {
            val innerPanel = JPanel(migLayout(migSize(4)))
            innerPanel.body()
            return JPanel(migLayout()).apply {
                border = IdeBorderFactory.createTitledBorder(title)
                if (fill) {
                    add(innerPanel, fillX())
                } else {
                    add(innerPanel)
                    add(JPanel(), fillX())
                }
            }
        }

        private fun JPanel.addVertically(vararg components: JComponent) {
            layout = migLayoutVertical()
            components.forEach {
                add(it, fillX())
            }
            add(JPanel(), fillY())
        }

        private fun migSize(size: Int, scale: Boolean = true) : String =
                "${if (scale) JBUIScale.scale(size) else size}px"

        /**
         * growX和pushX都可以用来控制组件和布局中行的增长能力。growX设置组件在其单元格中水平方向上的增长能力，
         * 这意味着如果窗口大小增加，使用growX约束的组件将在其单元格内水平增长以填充额外的空间。
         * 而pushX则使组件所在的行具有增长能力，这意味着如果窗口大小增加，使用pushX约束的行将增长以填充额外的空间。
         * 例如，如果您希望一个文本框在窗口大小增加时水平增长以填充额外的空间，您可以使用growX约束。
         * 而如果您希望整个行都增长以填充额外的空间，您可以使用pushX约束。
         */

        /**
         * 横向填充剩余空间
         */
        private fun fillX() : CC = CC().growX().pushX()

        /**
         * 纵向填充剩余空间
         */
        private fun fillY() : CC = CC().growY().pushY()

        /**
         * 换行
         */
        private fun warp() : CC = CC().wrap()


    }
}