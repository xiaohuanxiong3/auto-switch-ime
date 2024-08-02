package com.sqy.plugins.auto_switch_ime

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.AnActionResult
import com.intellij.openapi.actionSystem.ex.AnActionListener

class CustomAnActionListener : AnActionListener {

    // TODO 无法确定 Action事件和 PsiTreeChangeEvent的执行顺序
    // IDE内置的事件如Enter、Backspace、Undo从本地测试来看执行顺序固定
    // 但是其他的事件顺序无法确定，尤其是第三方插件修改PsiFile时触发的 Action 和 PsiTreeChangeEvent的执行顺序
    // 并且并发场景下出现问题的可能性较大
    // 为了插件稳定性，暂时不适用此方法进行性能优化
    override fun beforeActionPerformed(action: AnAction, event: AnActionEvent) {
//         println("beforeActionPerformed:" + action)
    }

    override fun afterActionPerformed(action: AnAction, event: AnActionEvent, result: AnActionResult) {
//        event.getData(CommonDataKeys.EDITOR)?.let{ editor ->
//            val psiFile = Map.editorPsiFileMap[editor]!!
//            psiFile.findElementAt(editor.caretModel.offset)?.let { psiElement ->
//                PsiFileLanguage.getSingleLanguageSwitchIMEHandler(psiFile.language)?.switch(editor, psiElement, isLineEnd(editor.caretModel.offset, psiElement));
//            }
//        }
//        println("afterActionPerformed:" + action)
    }

}