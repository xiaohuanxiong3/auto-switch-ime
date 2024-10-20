package com.friday.plugins.auto_switch_ime.listener

import com.friday.plugins.auto_switch_ime.setting.SwitchIMESettings
import com.friday.plugins.auto_switch_ime.support.IMEStatus
import com.friday.plugins.auto_switch_ime.support.IMESwitchSupport
import com.friday.plugins.auto_switch_ime.util.ApplicationUtil
import com.intellij.find.actions.FindInPathAction
import com.intellij.ide.actions.*
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ex.AnActionListener

class CursorFirstInSomeWindowActionListener : AnActionListener {

    override fun beforeActionPerformed(action: AnAction, event: AnActionEvent) {
        if (isTargetAction(action) && SwitchIMESettings.instance.switchToEnWhenCursorFirstInSomeWindow) {
            ApplicationUtil.executeOnPooledThread {
                IMESwitchSupport.switchTo(++IMESwitchSupport.seq, IMEStatus.EN)
//                IMESwitchSupport.switchToEn(++IMESwitchSupport.seq)
            }
        }
    }

    // 全局查找字符串时不起作用，初步怀疑提交的任务被吞了
//    override fun afterActionPerformed(action: AnAction, event: AnActionEvent, result: AnActionResult) {
//        if (isTargetAction(action) && SwitchIMESettings.instance.switchToEnWhenCursorFirstInSomeWindow) {
//            ApplicationUtil.executeOnPooledThread {
//                IMESwitchSupport.switchToEn(++IMESwitchSupport.seq)
//            }
//        }
//    }

    /**
     * action依次对应（快捷键是windows下的默认快捷键）
     * 1. 创建目录或包
     * 2. 从模板创建文件
     * 3. 创建文件
     * 4. 按下 shift + shift 对应的全局搜索
     * 5. ctrl + shift + F 对应的查找字符串
     * 6. ctrl + shift + N 对应的查找文件
     */
    private fun isTargetAction(action: AnAction) : Boolean{
        return action is CreateDirectoryOrPackageAction
                || action is CreateFromTemplateAction<*>
                || action is CreateFileAction
                || action is SearchEverywhereAction
                || action is FindInPathAction
                || action is GotoFileAction
    }

}