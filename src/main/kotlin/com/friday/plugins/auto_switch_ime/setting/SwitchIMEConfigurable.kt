package com.friday.plugins.auto_switch_ime.setting

import com.intellij.openapi.options.ConfigurableWithId
import javax.swing.JComponent

class SwitchIMEConfigurable : ConfigurableWithId {

    private var component : SwitchIMESettingsComponent = SwitchIMESettingsComponent()

    private val settings : SwitchIMESettings = SwitchIMESettings.instance

    override fun createComponent(): JComponent? {
        component.getJavaCheckBox().setSelected(settings.isJavaEnabled)
        component.getKotlinCheckBox().setSelected(settings.isKotlinEnabled)
        component.getSwitchToEnWhenCursorFirstInSomeWindow().setSelected(settings.switchToEnWhenCursorFirstInSomeWindow)
        return component.getPanel()
    }

    override fun isModified(): Boolean {
        return  component.getJavaCheckBox().isSelected != settings.isJavaEnabled ||
                component.getKotlinCheckBox().isSelected != settings.isKotlinEnabled ||
                component.getSwitchToEnWhenCursorFirstInSomeWindow().isSelected != settings.switchToEnWhenCursorFirstInSomeWindow
    }

    override fun apply() {
        settings.isJavaEnabled = component.getJavaCheckBox().isSelected
        settings.isKotlinEnabled = component.getKotlinCheckBox().isSelected
        settings.switchToEnWhenCursorFirstInSomeWindow = component.getSwitchToEnWhenCursorFirstInSomeWindow().isSelected
    }

    override fun getDisplayName(): String {
        return "输入法自动切换插件配置"
    }

    override fun getId(): String {
        return "preferences.auto_switch_ime"
    }
}