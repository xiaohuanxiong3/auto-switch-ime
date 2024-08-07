package com.friday.plugins.auto_switch_ime.handler

import com.friday.plugins.auto_switch_ime.Constants
import com.friday.plugins.auto_switch_ime.Map
import com.friday.plugins.auto_switch_ime.PsiElementLocation
import com.friday.plugins.auto_switch_ime.areaDecide.AreaDeciderDelegate
import com.friday.plugins.auto_switch_ime.support.IMESwitchSupport
import com.friday.plugins.auto_switch_ime.trigger.IMESwitchTrigger
import com.friday.plugins.auto_switch_ime.trigger.IMESwitchTrigger.*
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import java.util.concurrent.ConcurrentHashMap

abstract class AbstractSingleLanguageSwitchIMEHandler : SingleLanguageSwitchIMEHandler{

    private val psiElementLocationMap : ConcurrentHashMap<Editor, PsiElementLocation> = Map.psiElementLocationMap

    override fun handle(
        trigger: IMESwitchTrigger,
        caretPositionChange: Int,
        editor: Editor,
        psiElement: PsiElement,
        isLineEnd: Boolean
    ) {
        when (trigger.description) {
            MOUSE_CLICKED.description -> handleMouseClicked(caretPositionChange, editor, psiElement, isLineEnd)
            ARROW_KEYS_PRESSED.description -> handleArrowKeysPressed(caretPositionChange, editor, psiElement, isLineEnd)
            PSI_FILE_CHANGED.description -> handlePsiFileChanged(editor, psiElement, isLineEnd)
            else -> {
                throw Error(Constants.UNREACHABLE_CODE + "in SingleLanguageSwitchIMEHandler.handle method")
            }
        }
    }

    override fun switch(editor: Editor, curPsiElement: PsiElement, isLineEnd : Boolean) {
        val psiElementLocation = psiElementLocationMap[editor]!!
        val curPsiElementLocation = AreaDeciderDelegate.getPsiElementLocation(getLanguage(), curPsiElement, isLineEnd)
        // 初始状态，直接根据注释区和代码区进行输入法切换
        if (psiElementLocation.isInitState()) {
            doSwitch(curPsiElementLocation)
            psiElementLocation.copyFrom(curPsiElementLocation)
        } else {
            // 判断当前location是否和上一location相同。
            // 如果相同，则不进行任何操作，由用户自己操作
            // 否则，根据注释区和代码区进行输入法切换
            if (!curPsiElementLocation.equal(psiElementLocation)) {
                doSwitch(curPsiElementLocation)
                psiElementLocation.copyFrom(curPsiElementLocation)
            }
        }
    }

    private fun doSwitch(psiElementLocation: PsiElementLocation) {
        if (psiElementLocation.isCommentArea) {
            ApplicationManager.getApplication().executeOnPooledThread{
                IMESwitchSupport.switchToZh(++IMESwitchSupport.seq)
            }
        } else {
            ApplicationManager.getApplication().executeOnPooledThread{
                IMESwitchSupport.switchToEn(++IMESwitchSupport.seq)
            }
        }
    }

}