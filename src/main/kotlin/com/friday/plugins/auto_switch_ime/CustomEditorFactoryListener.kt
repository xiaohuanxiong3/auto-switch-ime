package com.friday.plugins.auto_switch_ime

import com.friday.plugins.auto_switch_ime.service.AutoSwitchIMEService
import com.friday.plugins.auto_switch_ime.trigger.MouseClickedHandler
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.event.*
import com.intellij.openapi.editor.ex.FocusChangeListener
import com.intellij.openapi.editor.impl.EditorImpl
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import java.awt.event.InputMethodEvent
import java.awt.event.InputMethodListener
import java.util.concurrent.ConcurrentHashMap

class CustomEditorFactoryListener : EditorFactoryListener {

    private val caretListenerMap : ConcurrentHashMap<Editor,SwitchIMECaretListener> = MyMap.caretListenerMap
    private val editorPsiFileMap : ConcurrentHashMap<Editor,PsiFile> = MyMap.editorPsiFileMap
    private val psiFileMap : ConcurrentHashMap<PsiFile, Editor> = MyMap.psiFileEditorMap
    private val documentListenerMap : ConcurrentHashMap<Editor, DocumentChangeCountListener> = MyMap.documentListenerMap

    // editor 是 文本编辑器
    override fun editorCreated(event: EditorFactoryEvent) {
        event.editor.project?.let {
            PsiDocumentManager.getInstance(it).getPsiFile(event.editor.document)
        }?.let { psiFile ->
            val caretListener = SwitchIMECaretListener()
            val documentListener = DocumentChangeCountListener()
            // 缓存 CaretListener
            caretListenerMap[event.editor] = caretListener
            // 缓存 Editor -> PsiFile 映射关系
            editorPsiFileMap[event.editor] = psiFile
            // 缓存 PsiFile -> Editor 映射关系
            psiFileMap[psiFile] = event.editor
            // 缓存 DocumentListener
            documentListenerMap[event.editor] = documentListener
            // 添加自定义的 CaretListener
            event.editor.caretModel.addCaretListener(caretListener)
            // 添加自定义的 EditorMouseListener（EditorImpl 调用 release() 方法时会清空 myMouseListeners,故此处只需添加，不用管删除）
            event.editor.addEditorMouseListener(MouseClickedHandler())
            event.editor.document.addDocumentListener(documentListener)
            // 添加自定义的 FocusChangeListener（删除如上）
            event.editor.let {
                it as? EditorImpl
            }?.addFocusListener(FocusListener())
            // 添加自定义的 InputMethodListener（处理中文输入未确认时不会触发CharTyped事件问题）
            event.editor.contentComponent.addInputMethodListener(MyInputMethodListener(event.editor))
        }
    }

    override fun editorReleased(event: EditorFactoryEvent) {
        caretListenerMap[event.editor]
        // 删除 CaretListener 缓存
        caretListenerMap.remove(event.editor)
        // 删除 PsiFile -> Editor 缓存
        editorPsiFileMap[event.editor]?.let {
            psiFileMap.remove(it)
        }
        // 删除 Editor -> PsiFile 缓存
        editorPsiFileMap.remove(event.editor)
        // 删除 DocumentListener 缓存
        documentListenerMap.remove(event.editor)
        // 删除自定义的 CaretListener
//        caretListener?.let {
//            event.editor.caretModel.removeCaretListener(it)
//        }
    }

    class DocumentChangeCountListener : DocumentListener {
        var numberOfChanges = 0

        override fun documentChanged(event: DocumentEvent) {
            numberOfChanges++
        }
    }

    class FocusListener : FocusChangeListener {

        // 有了 focusGained 和 全局唯一的PsiElementLocation 这个似乎没啥用了
//        override fun focusLost(editor: Editor) {
//            // 编辑器失去焦点时重置PsiElementLocation
//            MyMap.psiElementLocationMap[editor]?.reset()
//        }

        /**
         * 编辑器获得焦点时强制进行输入法切换
         * windows：
         *  官方输入法自带应用间状态保存和恢复功能，故无需考虑在离开IDEA后切换输入法，再回到IDEA编辑器可能的输入法不正确情况（主要是注释区和字符串常量区目前没有强制进行输入法切换）
         *  但是在IDEA编辑器之外如左侧文件导航区域进行了输入法切换，再回到编辑器时，如果当前光标处于注释区，则没有进行输入法切换，原因如上
         *  暂时将 注释区和字符串常量区 的输入法强制切换加上（目前只有 focusGained 会在进入注释区和字符串常量区时进行输入法强制切换），后续看有没有问题
         * macos 未知
         */
        override fun focusGained(editor: Editor) {
            AutoSwitchIMEService.handleWhenEditorFocusGained(editor)
        }
    }

    class SwitchIMECaretListener : CaretListener {

        var caretPositionChange : Int = 0

        override fun caretPositionChanged(event: CaretEvent) {
            caretPositionChange++
        }

    }

    class MyInputMethodListener(val editor: Editor) : InputMethodListener {

        // 假设 event.committedCharacterCount == 0 且 event.text != null 时，会取消选择已选择文本
        // 你要问我为什么这么喜欢假设，问就是不会
        override fun inputMethodTextChanged(event: InputMethodEvent?) {
            event?.let {
                if (event.committedCharacterCount == 0 && event.text != null && editor.selectionModel.hasSelection()) {
                    AutoSwitchIMEService.handleWhenSelectionUnSelected(editor)
                }
            }
        }

        override fun caretPositionChanged(event: InputMethodEvent?) {

        }

    }

}