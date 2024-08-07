package com.friday.plugins.auto_switch_ime.service

import com.friday.plugins.auto_switch_ime.Map
import com.friday.plugins.auto_switch_ime.PsiElementLocation
import com.friday.plugins.auto_switch_ime.PsiFileLanguage
import com.friday.plugins.auto_switch_ime.SwitchIMECaretListener
import com.friday.plugins.auto_switch_ime.handler.SingleLanguageSwitchIMEDelegate
import com.friday.plugins.auto_switch_ime.trigger.IMESwitchTrigger
import com.friday.plugins.auto_switch_ime.util.EditorUtil
import com.friday.plugins.auto_switch_ime.util.PsiFileUtil
import com.intellij.lang.Language
import com.intellij.lang.java.JavaLanguage
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.impl.EditorImpl
import com.intellij.openapi.fileTypes.PlainTextLanguage
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace
import org.jetbrains.kotlin.idea.KotlinLanguage
import java.util.concurrent.ConcurrentHashMap

object AutoSwitchIMEService {

    private val caretListenerMap : ConcurrentHashMap<Editor, SwitchIMECaretListener> = Map.caretListenerMap
    private val editorPsiFileMap : ConcurrentHashMap<Editor, PsiFile> = Map.editorPsiFileMap
    private val psiFileEditorMap : ConcurrentHashMap<PsiFile, Editor> = Map.psiFileEditorMap
    private val psiElementLocationMap : ConcurrentHashMap<Editor, PsiElementLocation> = Map.psiElementLocationMap
    private val logger : Logger = Logger.getInstance(AutoSwitchIMEService::class.java)

    fun prepareWithNoPsiFileChanged(editor: Editor) {
        try {
            caretListenerMap[EditorUtil.getEditor(editor)]!!.caretPositionChange = 0
        } catch (error : Throwable) {
            logger.error("caught error in prepareWithNoPsiFileChanged method ${error.message}")
        }
    }

    fun handleWithNoPsiFileChanged(editor: Editor, trigger: IMESwitchTrigger) {
        val caretPositionChange = caretListenerMap[EditorUtil.getEditor(editor)]!!.caretPositionChange
        if (caretPositionChange <= 0) {
            return
        }
        val targetEditor = EditorUtil.getEditor(editor)
        checkAndHandleCache(targetEditor)
        editorPsiFileMap[editor]?.let { psiFile ->
            val language = psiFile.language
            if (!PsiFileLanguage.isLanguageAutoSwitchEnabled(language)) {
                return
            }
            val psiElement = psiFile.findElementAt(editor.caretModel.offset)
            // 如果 psiElement为null
            if (psiElement == null) {
                handleSwitchWhenNullPsiElement(editor,language)
                return
            }
            val isLineEnd = isLineEnd(editor.caretModel.offset, psiElement)
            SingleLanguageSwitchIMEDelegate.handle(language, trigger, caretPositionChange, editor, psiElement, isLineEnd)
        }
    }

    fun handleWithPsiFileChanged(psiFile: PsiFile) {
        checkAndHandleCache(psiFile)
        psiFileEditorMap[psiFile]?.let { editor ->
            val language = psiFile.language
            val psiElement = psiFile.findElementAt(editor.caretModel.offset)
            // 如果 psiElement为null
            if (psiElement == null) {
                handleSwitchWhenNullPsiElement(editor,language)
                return
            }
            val isLineEnd = isLineEnd(editor.caretModel.offset, psiElement)
            SingleLanguageSwitchIMEDelegate.handle(language, IMESwitchTrigger.PSI_FILE_CHANGED, 0, editor, psiElement, isLineEnd)
        }
    }

    // 当psiElement为null时，处理输入法切换
    private fun handleSwitchWhenNullPsiElement(editor: Editor,language: Language) {
        when(language) {
            JavaLanguage.INSTANCE -> {
//                IMESwitchSupport.switchToEn()
            }
            KotlinLanguage.INSTANCE -> {
//                IMESwitchSupport.switchToEn()
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

    // 检查并处理缓存（必要的话）-- 知道有效的PsiFile
    private fun checkAndHandleCache(psiFile: PsiFile) {
        if (!psiFileEditorMap.containsKey(psiFile)){
            PsiFileUtil.getEditor(psiFile)?.let {
                // 移除旧缓存
                editorPsiFileMap[it]?.let { file ->
                    psiFileEditorMap.remove(file)
                }
                // 添加新缓存
                editorPsiFileMap[it] = psiFile
                psiFileEditorMap[psiFile] = it
                // 重置 PsiElementLocation
                psiElementLocationMap[it] = PsiElementLocation()
            }
        }
    }

    // 检查并处理缓存（必要的话）-- 知道Editor
    private fun checkAndHandleCache(editor: Editor) {
        editorPsiFileMap[editor]?.let { psiFile ->
            if (!psiFile.isValid) {
                EditorUtil.getPsiFile(editor)?.let { file ->
                    // 移除旧缓存
                    psiFileEditorMap.remove(psiFile)
                    // 添加新缓存
                    editorPsiFileMap[editor] = file
                    psiFileEditorMap[file] = editor
                    // 重置 PsiElementLocation
                    psiElementLocationMap[editor] = PsiElementLocation()
                }
            }
        }
    }

    // 判断光标是否在行尾
    private fun isLineEnd(offset: Int, psiElement: PsiElement?) : Boolean {
        return psiElement is PsiWhiteSpace
                && psiElement.text.contains("\n")
                // 当包含多个'\n'，只有psiElement开始处直到第一个换行符（包含第一个换行符）才被认为是行尾
                && offset <= psiElement.textRange.startOffset + psiElement.text.indexOf('\n')
                && psiElement.textRange.endOffset - psiElement.text.length <= offset
    }
}