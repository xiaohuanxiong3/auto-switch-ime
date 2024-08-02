package com.sqy.plugins.auto_switch_ime

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.ex.FocusChangeListener
import com.intellij.openapi.editor.impl.EditorImpl
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.sqy.plugins.auto_switch_ime.cause.MouseClickedHandler
import java.util.concurrent.ConcurrentHashMap

/**
 * 为 CustomEditorFactoryListener 的 editorCreated 做兜底操作
 * 因为我自己在用时会出现输入法切换不起作用，关闭并重新打开文件后就能解决问题的情况，原因暂时未知
 * ---------------------------------------------------------------------------------------------------------------------
 * 如果编辑器创建时 CustomEditorFactoryListener 的 editorCreated方法没有被调用，则此类不起作用
 * 似乎知道是什么原因了：
 * Caused by: com.intellij.psi.PsiInvalidElementAccessException:  Element: class com.intellij.psi.impl.source.PsiJavaFileImpl #JAVA
 * because: different providers:
 * com.intellij.psi.SingleRootFileViewProvider{vFile=file://C:.../xxx.java, vFileId=1691588, content=VirtualFileContent{size=1339}, eventSystemEnabled=true}(690ef848);
 * com.intellij.psi.SingleRootFileViewProvider{vFile=file://C:.../xxx.java, vFileId=1691588, content=VirtualFileContent{size=1339}, eventSystemEnabled=true}(726a96b1)
 * invalidated at: see attachment [Plugin: com.intellij.java]
 * chatgpt：你提供的错误信息 PsiInvalidElementAccessException 表明在JetBrains的IDE（如IntelliJ IDEA）中访问了无效的PSI元素（Program Structure Interface）。
 * 这么看可能就是我缓存的psiFile的原因了
 */
@Deprecated("")
class FocusGainedListener : FocusChangeListener {

    private val caretListenerMap : ConcurrentHashMap<Editor, SwitchIMECaretListener> = Map.caretListenerMap
    private val psiFileMap : ConcurrentHashMap<Editor, PsiFile> = Map.editorPsiFileMap

    override fun focusGained(editor: Editor) {
        if (!caretListenerMap.containsKey(editor) || !psiFileMap.containsKey(editor)) {
            editor.project?.let { project ->
                PsiDocumentManager.getInstance(project).getPsiFile(editor.document)
            }?.let { psiFile -> {
                val caretListener = SwitchIMECaretListener()
                // 缓存 CaretListener
                caretListenerMap[editor] = caretListener
                // 缓存 PsiFile
                psiFileMap[editor] = psiFile
                // 添加自定义的 CaretListener
                editor.caretModel.addCaretListener(caretListener)
                // 添加自定义的 EditorMouseListener（EditorImpl 调用 release() 方法时会清空 myMouseListeners,故此处只需添加，不用管删除）
                editor.addEditorMouseListener(MouseClickedHandler())
                // 添加自定义的 FocusChangeListener（删除如上）
                editor.let {
                    it as? EditorImpl
                }?.addFocusListener(FocusGainedListener())
            }}
        }
    }
}