package com.sqy.plugins.auto_switch_ime.setting

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@Service
@State(name = "SwitchIME.Settings", storages = [Storage("ime_switch_settings.xml")])
class SwitchIMESettings : PersistentStateComponent<SwitchIMESettings> {

    var isJavaEnabled: Boolean = false

    var isKotlinEnabled: Boolean = false

    override fun getState(): SwitchIMESettings? {
        return this
    }

    override fun loadState(state: SwitchIMESettings) {
        XmlSerializerUtil.copyBean(state, this);
    }

    companion object {
        val instance: SwitchIMESettings = ApplicationManager.getApplication().getService(SwitchIMESettings::class.java)
    }
}