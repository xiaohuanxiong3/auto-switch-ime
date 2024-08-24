package com.friday.plugins.auto_switch_ime

import com.friday.plugins.auto_switch_ime.service.AutoSwitchIMEService
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.actionSystem.ex.AnActionListener
import com.intellij.openapi.editor.Editor
import java.util.concurrent.ConcurrentHashMap

class DocumentChangedAnActionListener : AnActionListener {

    private val documentListenerMap : ConcurrentHashMap<Editor, CustomEditorFactoryListener.DocumentChangeCountListener> = MyMap.documentListenerMap

//    private val log : Logger = LoggerFactory.getLogger(DocumentChangedAnActionListener::class.java)

    // 假设这里拿到的Editor是 EditorImpl
    override fun beforeActionPerformed(action: AnAction, event: AnActionEvent) {
        event.getData(CommonDataKeys.EDITOR)?.let { editor ->
            documentListenerMap[editor]?.numberOfChanges = 0
        }
    }

    override fun afterActionPerformed(action: AnAction, event: AnActionEvent, result: AnActionResult) {
        event.getData(CommonDataKeys.EDITOR)?.let { editor ->
            documentListenerMap[editor]?.let { documentListener ->
                if (documentListener.numberOfChanges <= 0) return
                AutoSwitchIMEService.handleWhenAnActionHappened(editor)
            }
        }
    }

    override fun beforeEditorTyping(c: Char, dataContext: DataContext) {
        dataContext.getData(CommonDataKeys.PSI_FILE)?.let { psiFile ->
            if (!PsiFileLanguage.isLanguageAutoSwitchEnabled(psiFile.language)) {
                return
            }
            dataContext.getData(CommonDataKeys.EDITOR)?.let { editor ->
                AutoSwitchIMEService.handleWhenCharTyped(c, psiFile, editor)
            }
        }
    }

    // 由于字符输入只处理特定的字符，在某些情况下，会导致当前光标所在的PsiElement 和 PsiElementLocation不对应，暂时不做处理
//    override fun afterEditorTyping(c: Char, dataContext: DataContext) {
//        dataContext.getData(CommonDataKeys.PSI_FILE)?.let { psiFile ->
//            if (!PsiFileLanguage.isLanguageAutoSwitchEnabled(psiFile.language)) {
//                return
//            }
//            dataContext.getData(CommonDataKeys.EDITOR)?.let { editor ->
//                AutoSwitchIMEService.handleWhenCharType(c, psiFile, editor)
//            }
//        }
//    }


}