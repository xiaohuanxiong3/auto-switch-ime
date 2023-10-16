package com.sqy.plugins.auto_switch_ime

import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.sqy.plugins.support.IMESwitchSupport
import java.util.concurrent.ConcurrentHashMap

object AutoSwitchIMEService {

    private val caretListenerMap : ConcurrentHashMap<Editor, SwitchIMECaretListener> = EditorMap.caretListenerMap
    private val psiFileMap : ConcurrentHashMap<Editor, PsiFile> = EditorMap.psiFileMap

    fun prepare(editor: Editor) {
        caretListenerMap[editor]!!.caretPositionChange = 0
    }

    fun handle(editor: Editor,cause: IMEChangeCause) {
        val caretListener = caretListenerMap[editor]!!
        val psiFile = psiFileMap.getOrDefault(editor,null)
        val psiElement = psiFile?.findElementAt(editor.caretModel.offset)
        when(cause) {
            IMEChangeCause.MouseClick -> handleMouseClick(caretListener.caretPositionChange,psiElement)
            IMEChangeCause.ONE_CARET_MOVE -> handleOneCaretMove(caretListener.caretPositionChange,psiElement)
            IMEChangeCause.Enter -> handleEnter(caretListener.caretPositionChange,psiElement)
        }
    }

    /**
     * 处理鼠标点击引起的光标移动
     */
    private fun handleMouseClick(caretPositionChange : Int,psiElement: PsiElement?) {
        if (caretPositionChange > 0) {
            psiElement?.let {
                if (CommentDecider.isComment(it)) {
                    IMESwitchSupport.switchToZh()
                } else {
                    IMESwitchSupport.switchToEn()
                }
            }
        }
    }

    /**
     * 处理按下方向键引起的光标移动
     */
    private fun handleOneCaretMove(caretPositionChange : Int,psiElement: PsiElement?) {
        if (caretPositionChange > 0) {
            psiElement?.let {

            }
        }
    }

    /**
     * 处理 按下回车键引起的光标移动
     */
    private fun handleEnter(caretPositionChange : Int,psiElement: PsiElement?) {
        if (caretPositionChange == 1) {
            IMESwitchSupport.switchToEn()
        } else {
            psiElement?.let {

            }
        }
    }
}