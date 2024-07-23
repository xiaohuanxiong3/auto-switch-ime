package com.sqy.plugins.auto_switch_ime.service

import com.intellij.injected.editor.EditorWindow
import com.intellij.lang.Language
import com.intellij.lang.java.JavaLanguage
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.impl.EditorImpl
import com.intellij.openapi.fileTypes.PlainTextLanguage
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace
import com.sqy.plugins.auto_switch_ime.EditorMap
import com.sqy.plugins.auto_switch_ime.SwitchIMECaretListener
import com.sqy.plugins.auto_switch_ime.cause.CaretPositionChangeCause
import com.sqy.plugins.auto_switch_ime.support.IMESwitchSupport
import org.jetbrains.kotlin.idea.KotlinLanguage
import java.util.concurrent.ConcurrentHashMap

object AutoSwitchIMEService {

    private val caretListenerMap : ConcurrentHashMap<Editor, SwitchIMECaretListener> = EditorMap.caretListenerMap
    private val psiFileMap : ConcurrentHashMap<Editor, PsiFile> = EditorMap.psiFileMap
    private val logger : Logger = Logger.getInstance(AutoSwitchIMEService::class.java)

    fun prepare(editor: Editor) {
        try {
            val targetEditor = editor.let {
                it as? EditorWindow
            }?.delegate?:editor
            caretListenerMap[targetEditor]!!.caretPositionChange = 0
        } catch (error : Throwable) {
            logger.error("caught error in prepare method",error)
        }
    }

    fun handle(editor: Editor,cause: CaretPositionChangeCause) {
        try {
            val targetEditor = editor.let {
                it as? EditorWindow
            }?.delegate?:editor
            doHandle(targetEditor, cause)
        } catch (error : Throwable) {
            logger.error("caught error in handle method",error)
        }
    }

    private fun doHandle(editor: Editor, cause: CaretPositionChangeCause) {
        val caretListener = caretListenerMap[editor]!!
        if (caretListener.caretPositionChange <= 0) {
            return
        }
        val psiFile = psiFileMap.getOrDefault(editor,null)
        psiFile?.let { file ->
            val language = file.language
            val psiElement = file.findElementAt(editor.caretModel.offset)
            // 如果 psiElement为null
            if (psiElement == null) {
                handleSwitchWhenNullPsiElement(editor,language)
                return
            }
            val isLineEnd = isLineEnd(editor.caretModel.offset,psiElement,cause)
            CaretPositionChangeCause.getIMESwitchHandler(cause)?.handle(language, caretListener.caretPositionChange, psiElement, isLineEnd)
        }
    }

    /**
     * 当psiElement为null时，处理输入法切换
     */
    private fun handleSwitchWhenNullPsiElement(editor: Editor,language: Language) {
        when(language) {
            JavaLanguage.INSTANCE -> {
                IMESwitchSupport.switchToEn()
            }
            KotlinLanguage.INSTANCE -> {
                IMESwitchSupport.switchToEn()
            }
            PlainTextLanguage.INSTANCE -> {
                val placeholder = "" + editor.let {
                    it as? EditorImpl
                }?.placeholder
                when(placeholder) {
                    // commit 输入git信息 区域，暂时不做处理
                    "Commit Message" -> {

                    }
                    // editor转EditorImpl失败或 editor不是EditorImpl的实例 或 EditorImpl的placeholder为null
                    "" -> {

                    }
                }
            }
        }

    }

    // 判断光标是否在行尾
    private fun isLineEnd(offset : Int, psiElement: PsiElement?, cause: CaretPositionChangeCause) : Boolean {
        return psiElement is PsiWhiteSpace
                && psiElement.text.contains("\n")
                // 当包含多个'\n'，只有psiElement开始处直到第一个换行符（包含第一个换行符）才被认为是行尾
                // 当进行字符输入走到这时，offset已经更新，而psiElement似乎还未更新
                && offset <= psiElement.textRange.startOffset + psiElement.text.indexOf('\n') + (( if (cause == CaretPositionChangeCause.CHAR_TYPED)  1 else 0 ))
                && psiElement.textRange.endOffset - psiElement.text.length + (( if (cause == CaretPositionChangeCause.CHAR_TYPED)  1 else 0 )) <= offset
    }
}