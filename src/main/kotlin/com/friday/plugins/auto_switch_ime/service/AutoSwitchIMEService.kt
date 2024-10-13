package com.friday.plugins.auto_switch_ime.service

import com.friday.plugins.auto_switch_ime.CustomEditorFactoryListener
import com.friday.plugins.auto_switch_ime.MyMap
import com.friday.plugins.auto_switch_ime.areaDecide.PsiElementLocation
import com.friday.plugins.auto_switch_ime.handler.HandleStrategy
import com.friday.plugins.auto_switch_ime.handler.SingleLanguageSwitchIMEDelegate
import com.friday.plugins.auto_switch_ime.language.PsiFileLanguage
import com.friday.plugins.auto_switch_ime.trigger.IMESwitchTrigger
import com.friday.plugins.auto_switch_ime.util.ApplicationUtil
import com.friday.plugins.auto_switch_ime.util.EditorUtil
import com.friday.plugins.auto_switch_ime.util.PsiFileUtil
import com.intellij.lang.Language
import com.intellij.lang.java.JavaLanguage
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.impl.EditorImpl
import com.intellij.openapi.fileTypes.PlainTextLanguage
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace
import org.jetbrains.kotlin.idea.KotlinLanguage
import java.util.concurrent.ConcurrentHashMap

object AutoSwitchIMEService {

    private val caretListenerMap : ConcurrentHashMap<Editor, CustomEditorFactoryListener.SwitchIMECaretListener> = MyMap.caretListenerMap
    private val editorPsiFileMap : ConcurrentHashMap<Editor, PsiFile> = MyMap.editorPsiFileMap
    private val psiFileEditorMap : ConcurrentHashMap<PsiFile, Editor> = MyMap.psiFileEditorMap
    private val psiElementLocation : PsiElementLocation = MyMap.psiElementLocation
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
                handleWhenNullPsiElement(editor,language)
                return
            }
            val isLineEnd = isLineEnd(editor.caretModel.offset, psiElement)
            SingleLanguageSwitchIMEDelegate.handle(language, trigger, HandleStrategy.UPDATE_LOCATION_AND_SWITCH, editor, psiElement, isLineEnd)
        }
    }

    /**
     * 事件发生，导致文件发生变化时，进行输入法切换相关操作
     */
    fun handleWhenAnActionHappened(editor: Editor, language: Language, action: AnAction, documentChange: Int, caretChange: Int) {
        val handleStrategy = SingleLanguageSwitchIMEDelegate.getHandleStrategyWhenAnActionHappened(language, action, documentChange, caretChange)
        if (handleStrategy == HandleStrategy.DO_NOT_HANDLE) return
        checkAndHandleCache(editor)
        editorPsiFileMap[editor]?.let { psiFile ->
            val documentManager = PsiDocumentManager.getInstance(psiFile.project)
            editor.document.let { document ->
                documentManager.performForCommittedDocument(document) {
                    val psiElement = psiFile.findElementAt(editor.caretModel.offset)
                    // 如果 psiElement为null
                    if (psiElement == null) {
                        handleWhenNullPsiElement(editor,language)
                        return@performForCommittedDocument
                    }
                    val isLineEnd = isLineEnd(editor.caretModel.offset, psiElement)
                    SingleLanguageSwitchIMEDelegate.handle(psiFile.language, IMESwitchTrigger.AN_ACTION_HAPPENED, handleStrategy, editor, psiElement, isLineEnd)
                }
            }
        }
    }

    fun handleWhenCharTyped(c : Char, psiFile: PsiFile, editor: Editor) {
        val handleStrategy = SingleLanguageSwitchIMEDelegate.getHandleStrategyWhenCharTyped(psiFile.language, c, editor)
        if (handleStrategy == HandleStrategy.DO_NOT_HANDLE) return
        // 在当前正在处理的所有UI事件完成后，执行下面的代码
        ApplicationUtil.invokeLater {
            val documentManager = PsiDocumentManager.getInstance(psiFile.project)
            documentManager.getDocument(psiFile)?.let { document ->
                documentManager.performForCommittedDocument(document) {
                    checkAndHandleCache(psiFile)
                    psiFileEditorMap[psiFile]?.let { editor ->
                        val language = psiFile.language
                        val psiElement = psiFile.findElementAt(editor.caretModel.offset)
                        // 如果 psiElement为null
                        if (psiElement == null) {
                            handleWhenNullPsiElement(editor, language)
                            return@performForCommittedDocument
                        }
                        val isLineEnd = isLineEnd(editor.caretModel.offset, psiElement)
                        SingleLanguageSwitchIMEDelegate.handle(language, IMESwitchTrigger.CHAR_TYPE, handleStrategy, editor, psiElement, isLineEnd)
                    }
                }
            }
        }
    }

    fun handleWhenSelectionUnSelected(editor: Editor) {
        checkAndHandleCache(editor)
        editorPsiFileMap[editor]?.let { psiFile ->
            val documentManager = PsiDocumentManager.getInstance(psiFile.project)
            editor.document.let { document ->
                documentManager.performForCommittedDocument(document) {
                    val language = psiFile.language
                    if (!PsiFileLanguage.isLanguageAutoSwitchEnabled(language)) {
                        return@performForCommittedDocument
                    }
                    val psiElement = psiFile.findElementAt(editor.caretModel.offset)
                    // 如果 psiElement为null
                    if (psiElement == null) {
                        handleWhenNullPsiElement(editor,language)
                        return@performForCommittedDocument
                    }
                    val isLineEnd = isLineEnd(editor.caretModel.offset, psiElement)
                    SingleLanguageSwitchIMEDelegate.handle(language, IMESwitchTrigger.CHAR_TYPE, HandleStrategy.UPDATE_LOCATION, editor, psiElement, isLineEnd)
                }
            }
        }
    }

    fun handleWhenEditorFocusGained(editor: Editor) {
        checkAndHandleCache(editor)
        ApplicationUtil.invokeLater {
            editorPsiFileMap[editor]?.let { psiFile ->
                val language = psiFile.language
                if (!PsiFileLanguage.isLanguageAutoSwitchEnabled(language)) {
                    return@invokeLater
                }
                val psiElement = psiFile.findElementAt(editor.caretModel.offset)
                // 如果 psiElement为null
                if (psiElement == null) {
                    handleWhenNullPsiElement(editor,language)
                    return@invokeLater
                }
                val isLineEnd = isLineEnd(editor.caretModel.offset, psiElement)
                SingleLanguageSwitchIMEDelegate.handle(language, IMESwitchTrigger.EDITOR_FOCUS_GAINED, HandleStrategy.UPDATE_LOCATION_AND_FORCE_SWITCH, editor, psiElement, isLineEnd)
            }
        }
    }

    // 当psiElement为null时，处理输入法切换
    private fun handleWhenNullPsiElement(editor: Editor, language: Language) {
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
                psiElementLocation.reset()
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
                    psiElementLocation.reset()
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