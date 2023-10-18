package com.sqy.plugins.auto_switch_ime

import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.sqy.plugins.support.IMESwitchSupport
import java.util.concurrent.ConcurrentHashMap

object AutoSwitchIMEService {

    private val caretListenerMap : ConcurrentHashMap<Editor, SwitchIMECaretListener> = EditorMap.caretListenerMap
    private val psiFileMap : ConcurrentHashMap<Editor, PsiFile> = EditorMap.psiFileMap
    private var lastMoveTime : Long = 0
    private val moveAllowInterval : Long = 300

    fun prepare(editor: Editor) {
        caretListenerMap[editor]!!.caretPositionChange = 0
    }

    fun handle(editor: Editor,cause: CaretPositionChangeCause) {
        val caretListener = caretListenerMap[editor]!!
        val psiFile = psiFileMap.getOrDefault(editor,null)
        val psiElement = psiFile?.findElementAt(editor.caretModel.offset)
        psiElement.let {
            it as? LeafPsiElement
        }?.let {
            val isLineEnd = isLineEnd(psiElement)
            when(cause) {
                CaretPositionChangeCause.MOUSE_CLIKED -> handleMouseClick(caretListener.caretPositionChange,it,isLineEnd)
                CaretPositionChangeCause.ONE_CARET_MOVE -> handleOneCaretMove(caretListener.caretPositionChange,it,isLineEnd)
                CaretPositionChangeCause.ENTER -> handleEnter(caretListener.caretPositionChange,it,isLineEnd)
                CaretPositionChangeCause.TYPED -> handleType(caretListener.caretPositionChange,it,isLineEnd)
                else -> {}
            }
        }
    }

    /**
     * 处理鼠标点击引起的光标移动
     */
    private fun handleMouseClick(caretPositionChange : Int, psiElement: LeafPsiElement, isLineEnd : Boolean) {
        if (caretPositionChange > 0) {
            psiElement.let {
                if (AreaDecider.isComment(it,isLineEnd)) {
                    IMESwitchSupport.switchToZh()
                } else if (AreaDecider.isCode(it,isLineEnd)){
                    IMESwitchSupport.switchToEn()
                }
            }
        }
    }

    /**
     * 处理按下方向键引起的光标移动
     */
    private fun handleOneCaretMove(caretPositionChange : Int, psiElement: LeafPsiElement, isLineEnd : Boolean) {
        if (caretPositionChange > 0) {
            psiElement.let {
                // 似乎有时间限制
//                if (System.currentTimeMillis() - lastMoveTime > moveAllowInterval) {
//                    if (AreaDecider.isComment(it,isLineEnd)) {
//                        IMESwitchSupport.switchToZh()
//                    } else if (AreaDecider.isCode(it,isLineEnd)){
//                        IMESwitchSupport.switchToEn()
//                    }
//                    lastMoveTime = System.currentTimeMillis()
//                }
            }
        }
    }

    /**
     * 处理 按下回车键引起的光标移动
     */
    private fun handleEnter(caretPositionChange : Int, psiElement: LeafPsiElement, isLineEnd : Boolean) {
        if (caretPositionChange == 1) {
            IMESwitchSupport.switchToEn()
        } else if(caretPositionChange > 1){
            psiElement.let {
                // 似乎只有在 /** **/区会出现 caretPositionChange > 1 的情况
                // 先无脑切中文吧
                IMESwitchSupport.switchToZh()
            }
        }
    }

    /**
     * 处理 输入字符引起的光标移动，主要是处理检测以//开头的注释
     */
    private fun handleType(caretPositionChange : Int, psiElement: LeafPsiElement, isLineEnd : Boolean) {
        if (caretPositionChange > 0) {
            psiElement.let {
                if (isLineEnd && psiElement.prevSibling is PsiErrorElement) {
                    IMESwitchSupport.switchToZh()
                }
            }
        }
    }

    private fun isLineEnd(psiElement: PsiElement?) : Boolean {
        return psiElement is PsiWhiteSpace &&
                psiElement.text.startsWith("\n")
    }
}