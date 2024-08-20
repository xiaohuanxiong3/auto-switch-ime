package com.friday.plugins.auto_switch_ime.trigger

import com.friday.plugins.auto_switch_ime.PsiFileLanguage
import com.friday.plugins.auto_switch_ime.service.AutoSwitchIMEService
import com.intellij.psi.PsiTreeChangeAdapter
import com.intellij.psi.PsiTreeChangeEvent
import com.intellij.psi.impl.PsiTreeChangeEventImpl

class CustomPsiTreeChangeListener : PsiTreeChangeAdapter() {

    // TODO 使用 ReadAction 读取 PsiFile 的内容提供安全和一致性保障
    // PsiFile树改变完成后判断光标所处位置
    // 此方法优点是几乎能覆盖所有需要输入法切换的场景
    //      缺点是触发次数频繁
    override fun childrenChanged(event: PsiTreeChangeEvent) {
        if (!PsiFileLanguage.isLanguageAutoSwitchEnabled(event.file?.language) || event !is PsiTreeChangeEventImpl || !event.isGenericChange) {
            return
        }
        event.file?.let { psiFile ->
            AutoSwitchIMEService.handleWithPsiFileChanged(psiFile)
        }
    }

}