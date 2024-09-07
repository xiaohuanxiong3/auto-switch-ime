package com.friday.plugins.auto_switch_ime.action

import com.friday.plugins.auto_switch_ime.setting.SwitchIMESettings
import com.friday.plugins.auto_switch_ime.util.EditorUtil
import com.friday.plugins.auto_switch_ime.util.NotificationUtil
import com.intellij.lang.java.JavaLanguage
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import org.jetbrains.kotlin.idea.KotlinLanguage

class SwitchAutoSwitchEnableAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        event.getData(CommonDataKeys.EDITOR)?.let { editor ->
            val language = EditorUtil.getLanguage(editor)
            when (language) {
                JavaLanguage.INSTANCE -> {
                    SwitchIMESettings.instance.isJavaEnabled = !SwitchIMESettings.instance.isJavaEnabled
                    EditorUtil.updateStatusBarWidgetStatus(editor, language)
                    if (SwitchIMESettings.instance.isJavaEnabled) {
                        NotificationUtil.info("Java-开启输入法自动切换")
                    } else {
                        NotificationUtil.info("Java-关闭输入法自动切换")
                    }
                }
                KotlinLanguage.INSTANCE -> {
                    SwitchIMESettings.instance.isKotlinEnabled = !SwitchIMESettings.instance.isKotlinEnabled
                    EditorUtil.updateStatusBarWidgetStatus(editor, language)
                    if (SwitchIMESettings.instance.isKotlinEnabled) {
                        NotificationUtil.info("Kotlin-开启输入法自动切换")
                    } else {
                        NotificationUtil.info("Kotlin-关闭输入法自动切换")
                    }
                }
                else -> {
                    // do nothing
                    NotificationUtil.warn("${language.toString()}-暂不支持输入法自动切换")
                }
            }
        }
    }

}