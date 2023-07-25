package com.sqy.plugins.translation.setting

import com.intellij.openapi.options.Configurable
import com.sqy.plugins.translation.engine.TranslationEngineEnum
import javax.swing.JComponent

class TranslationConfigurable : Configurable {

    private val component : TranslationSettingsComponent = TranslationSettingsComponent()

    private val settings : TranslationSettings = TranslationSettings.instance

    override fun createComponent(): JComponent? {
        // TODO("Not yet implemented")
        component.setSelectedEngine(settings.translationEngine)
        component.setPrimaryLanguage(settings.primaryLanguage)
        return component.getPanel()
    }

    override fun isModified(): Boolean {
        // TODO("Not yet implemented")
        return component.getSelectedEngine() != settings.translationEngine ||
                !component.getPrimaryLanguage().equals(settings.primaryLanguage)
    }

    override fun apply() {
        // TODO("Not yet implemented")
        settings.translationEngine = component.getSelectedEngine() ?: TranslationEngineEnum.Default
        settings.primaryLanguage = component.getPrimaryLanguage() ?: "zh"
    }

    override fun getDisplayName(): String {
        // TODO("Not yet implemented")
        return "Translate Settings"
    }

}