package com.sqy.plugins.translation.setting.dialog

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBPasswordField
import com.intellij.ui.components.JBTextField
import com.intellij.ui.scale.JBUIScale
import com.intellij.util.ui.JBUI
import com.sqy.plugins.icons.TranslationIcons
import com.sqy.plugins.translation.setting.TranslationSettings
import com.sqy.plugins.translation.setting.engineSettings.BaiduTranslationEngineSetting
import com.sqy.plugins.translation.ui.UI.fillX
import com.sqy.plugins.translation.ui.UI.migLayout
import com.sqy.plugins.translation.ui.UI.wrap
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class BaiduTranslationDialog : DialogWrapper(false){
    private val panel : JComponent = JPanel()

    private val logo : JLabel = JLabel(TranslationIcons.load("/image/baidu_translate_logo.svg"))

    private val appIdTextField : JBTextField = JBTextField()

    private val appSecretJBPasswordField : JBPasswordField = JBPasswordField()

    private val baiduTranslationEngineSetting : BaiduTranslationEngineSetting = TranslationSettings.instance.baiduTranslationEngineSetting

    init {
        title = "百度翻译"
        isResizable = false
        // 初始化 appId 和 secret
        appIdTextField.text = baiduTranslationEngineSetting.appId
        appSecretJBPasswordField.text = baiduTranslationEngineSetting.appSecret
        panel.init()
        super.init()
    }

    private fun JComponent.init() = run {
        layout = migLayout()
        minimumSize = Dimension(300,0)

        logo.border = JBUI.Borders.empty(10, 0, 18, 0)

        add(logo, wrap().span(2).alignX("50%"))

        val gap = JBUIScale.scale(8).toString()

        add(JLabel("APP ID:"))
        add(appIdTextField, fillX().wrap().gapLeft(gap))

        add(JLabel("密钥:"))
        add(appSecretJBPasswordField, fillX().wrap().gapLeft(gap))
    }

    override fun createCenterPanel(): JComponent? = panel

    override fun doOKAction() {
        baiduTranslationEngineSetting.appId = appIdTextField.text
        baiduTranslationEngineSetting.appSecret = appSecretJBPasswordField.password.concatToString()
        super.doOKAction()
    }

}