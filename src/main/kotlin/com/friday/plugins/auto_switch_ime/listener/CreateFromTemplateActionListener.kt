package com.friday.plugins.auto_switch_ime.listener

import com.friday.plugins.auto_switch_ime.setting.SwitchIMESettings
import com.friday.plugins.auto_switch_ime.support.IMESwitchSupport
import com.friday.plugins.auto_switch_ime.util.ApplicationUtil
import com.intellij.ide.actions.CreateFromTemplateAction
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.AnActionResult
import com.intellij.openapi.actionSystem.ex.AnActionListener

class CreateFromTemplateActionListener : AnActionListener {

    override fun afterActionPerformed(action: AnAction, event: AnActionEvent, result: AnActionResult) {
        if (action is CreateFromTemplateAction<*> && SwitchIMESettings.instance.switchToEnWhenCreateFromTemplate) {
            ApplicationUtil.executeOnPooledThread {
                IMESwitchSupport.switchToEn(++IMESwitchSupport.seq)
            }
        }
    }

}