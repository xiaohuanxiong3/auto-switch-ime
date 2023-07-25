package com.sqy.plugins.translation.setting

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.messages.Topic
import com.intellij.util.xmlb.XmlSerializerUtil
import com.sqy.plugins.translation.engine.TranslationEngineEnum
import com.sqy.plugins.translation.setting.engineSettings.BaiduTranslationEngineSetting
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

@State(name = "Translation.Settings", storages = [Storage("translation.xml")])
class TranslationSettings : PersistentStateComponent<TranslationSettings>{

    @Volatile
    @Transient
    private var isInitialized : Boolean = false

    var translationEngine : TranslationEngineEnum by Delegates.observable(TranslationEngineEnum.Default){
        _: KProperty<*>, oldValue: TranslationEngineEnum, newValue: TranslationEngineEnum ->
        if (isInitialized && oldValue != newValue) {
            SETTINGS_CHANGE_PUBLISHER.onTranslationEngineChanged(newValue.id)
        }
    }

    var primaryLanguage : String = "zh"

    var baiduTranslationEngineSetting : BaiduTranslationEngineSetting = BaiduTranslationEngineSetting()


    override fun getState(): TranslationSettings? {
        return this
    }

    override fun loadState(state: TranslationSettings) {
        XmlSerializerUtil.copyBean(state, this);
        isInitialized = true
    }

    companion object {

        val instance : TranslationSettings = ApplicationManager.getApplication().getService(TranslationSettings::class.java)

    }

}

val SETTINGS_CHANGE_PUBLISHER: SettingsChangeListener =
        ApplicationManager.getApplication().messageBus.syncPublisher(SettingsChangeListener.TOPIC)

interface SettingsChangeListener {

    fun onTranslationEngineChanged(newTranslationEngineId: String)

    companion object {
        val TOPIC: Topic<SettingsChangeListener> =
                Topic.create("TranslationSettingsChanged", SettingsChangeListener::class.java)
    }
}