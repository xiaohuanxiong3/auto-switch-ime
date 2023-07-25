package com.sqy.plugins.translation.service

import com.intellij.openapi.application.Application
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.sqy.plugins.translation.engine.BaiduTranslationEngine
import com.sqy.plugins.translation.engine.DoNothingTranslationEngine
import com.sqy.plugins.translation.engine.TranslationEngine
import com.sqy.plugins.translation.engine.TranslationEngineEnum
import com.sqy.plugins.translation.setting.SettingsChangeListener
import com.sqy.plugins.translation.setting.TranslationSettings
import com.sqy.plugins.translation.ui.TranslationBalloon

class TranslationService private constructor(){

    @Volatile
    var translationEngine:TranslationEngine = DoNothingTranslationEngine
        private set

    init {
        setTranslateEngine(TranslationSettings.instance.translationEngine.id)
        ApplicationManager.getApplication()
                .messageBus
                .connect()
                .subscribe(SettingsChangeListener.TOPIC, object : SettingsChangeListener {
                    override fun onTranslationEngineChanged(newTranslationEngineId: String) {
                        logger.info("翻译引擎更改为：${newTranslationEngineId}")
                        setTranslateEngine(newTranslationEngineId)
                    }
                })
    }

    fun setTranslateEngine(newTranslateEngineId: String) {
        if (newTranslateEngineId != translationEngine.id) {
            translationEngine = when (newTranslateEngineId) {
                TranslationEngineEnum.Baidu.id -> BaiduTranslationEngine

                else -> DoNothingTranslationEngine
            }
        }
    }

    private val application : Application = ApplicationManager.getApplication()

    private val logger : Logger = Logger.getInstance(TranslationService::class.java)

    fun translate(ballon : TranslationBalloon,text : String) {
        if (text.isBlank()) {
            return
        }

        // 对原始文本进行预处理
        val textToTranslate = prepareText(text)

        ballon.showStartTranslate()

        check(ApplicationManager.getApplication().isDispatchThread) {
            "${TranslationService::class.java.simpleName} must only be used from the Event Dispatch Thread."
        }

        application.executeOnPooledThread {
            try {
                val translator = translationEngine
                with(translator) {
                    translate(textToTranslate).let {
                        application.invokeLater {
                            ballon.showTranslation(it,false)
                        }
                    }
                }
            } catch (error : Throwable) {
                logger.error("翻译错误",error)
                application.invokeLater {
                    ballon.showError(error)
                }
            }
        }
    }

    private fun prepareText(text: String) : String {
        val textToTranslate = text.replace("*","")
        return textToTranslate
    }



}