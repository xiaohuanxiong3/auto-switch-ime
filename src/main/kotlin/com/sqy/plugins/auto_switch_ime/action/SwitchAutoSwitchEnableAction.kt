package com.sqy.plugins.auto_switch_ime.action

import com.intellij.lang.java.JavaLanguage
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.sqy.plugins.auto_switch_ime.setting.SwitchIMESettings
import com.sqy.plugins.auto_switch_ime.util.EditorUtil
import org.jetbrains.kotlin.idea.KotlinLanguage

class SwitchAutoSwitchEnableAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        event.getData(CommonDataKeys.EDITOR)?.let { editor ->
            when (EditorUtil.getLanguage(editor)) {
                JavaLanguage.INSTANCE -> {
                    SwitchIMESettings.instance.isJavaEnabled = !SwitchIMESettings.instance.isJavaEnabled
                    if (SwitchIMESettings.instance.isJavaEnabled) {
                        // 本地打印中文会有乱码，淦！
                        println("开启Java语言输入法自动切换功能")
                    } else {
                        println("关闭Java语言输入法自动切换功能")
                    }
                }
                KotlinLanguage.INSTANCE -> {
                    SwitchIMESettings.instance.isKotlinEnabled = !SwitchIMESettings.instance.isKotlinEnabled
                    if (SwitchIMESettings.instance.isKotlinEnabled) {
                        println("开启Kotlin语言输入法自动切换功能")
                    } else {
                        println("关闭Kotlin语言输入法自动切换功能")
                    }
                }
                else -> {
                    // do nothing
                }
            }
        }
    }

}