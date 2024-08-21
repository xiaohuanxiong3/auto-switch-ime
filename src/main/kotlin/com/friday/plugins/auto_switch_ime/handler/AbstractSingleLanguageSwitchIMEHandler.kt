package com.friday.plugins.auto_switch_ime.handler

import com.friday.plugins.auto_switch_ime.Constants
import com.friday.plugins.auto_switch_ime.MyMap
import com.friday.plugins.auto_switch_ime.PsiElementLocation
import com.friday.plugins.auto_switch_ime.areaDecide.AreaDeciderDelegate
import com.friday.plugins.auto_switch_ime.support.IMESwitchSupport
import com.friday.plugins.auto_switch_ime.trigger.IMESwitchTrigger
import com.friday.plugins.auto_switch_ime.trigger.IMESwitchTrigger.*
import com.friday.plugins.auto_switch_ime.util.ApplicationUtil
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import java.util.concurrent.ConcurrentHashMap

abstract class AbstractSingleLanguageSwitchIMEHandler : SingleLanguageSwitchIMEHandler {

    private val psiElementLocationMap : ConcurrentHashMap<Editor, PsiElementLocation> = MyMap.psiElementLocationMap

    override fun handle(
        trigger: IMESwitchTrigger,
        caretPositionChange: Int,
        editor: Editor,
        psiElement: PsiElement,
        isLineEnd: Boolean
    ) {
        when (trigger.description) {
            CHAR_TYPED.description -> handleWhenCharTyped(editor, psiElement, isLineEnd)
            MOUSE_CLICKED.description -> handleWhenMouseClicked(caretPositionChange, editor, psiElement, isLineEnd)
            ARROW_KEYS_PRESSED.description -> handleWhenArrowKeysPressed(caretPositionChange, editor, psiElement, isLineEnd)
            PSI_FILE_CHANGED.description -> handleWhenPsiFileChanged(editor, psiElement, isLineEnd)
            AN_ACTION_HAPPENED.description -> handleWhenAnActionHappened(editor, psiElement, isLineEnd)
            else -> {
                throw Error(Constants.UNREACHABLE_CODE + "in SingleLanguageSwitchIMEHandler.handle method")
            }
        }
    }

    override fun shouldHandleWhenCharTyped(c: Char): Boolean {
        return false
    }

    override fun handleWhenCharTyped(editor: Editor, psiElement: PsiElement, isLineEnd: Boolean) {
        switch(editor, psiElement, isLineEnd)
    }

    override fun handleWhenMouseClicked(caretPositionChange: Int, editor: Editor, psiElement: PsiElement, isLineEnd: Boolean) {
        switch(editor, psiElement, isLineEnd)
    }

    override fun handleWhenArrowKeysPressed(caretPositionChange: Int, editor: Editor, psiElement: PsiElement, isLineEnd: Boolean) {
        switch(editor, psiElement, isLineEnd)
    }

    override fun handleWhenPsiFileChanged(editor: Editor, psiElement: PsiElement, isLineEnd: Boolean) {
        switch(editor, psiElement, isLineEnd)
    }

    override fun handleWhenAnActionHappened(editor: Editor, psiElement: PsiElement, isLineEnd: Boolean) {
        switch(editor, psiElement, isLineEnd)
    }

    override fun switch(editor: Editor, curPsiElement: PsiElement, isLineEnd : Boolean) {
        val psiElementLocation = psiElementLocationMap[editor]!!
        val curPsiElementLocation = AreaDeciderDelegate.getPsiElementLocation(getLanguage(), curPsiElement, isLineEnd)
        // 初始状态，直接返回
        if (curPsiElementLocation.isInitState()) return
        // 位于其他区域
        if (curPsiElementLocation.isInOtherLocation()) {
            doSwitchInOtherLocation()
            psiElementLocation.copyFrom(curPsiElementLocation)
        } else {
            // 判断当前location是否和上一location相同。
            // 如果相同，则不进行任何操作，由用户自己操作
            // 否则，根据策略进行输入法切换
            if (!curPsiElementLocation.equal(psiElementLocation)) {
                doSwitch(curPsiElementLocation)
                psiElementLocation.copyFrom(curPsiElementLocation)
            }
        }
    }

    private fun doSwitch(psiElementLocation: PsiElementLocation) {
        if (!psiElementLocation.doSwitchWhenFirstInThisLocation) {
            return
        }
        if (psiElementLocation.switchToSecondLanguageWhenFirstInThisLocation) {
            ApplicationUtil.executeOnPooledThread {
                IMESwitchSupport.switchToZh(++IMESwitchSupport.seq)
            }
        } else {
            ApplicationUtil.executeOnPooledThread {
                IMESwitchSupport.switchToEn(++IMESwitchSupport.seq)
            }
        }
    }

    private fun doSwitchInOtherLocation() {
        ApplicationUtil.executeOnPooledThread {
            IMESwitchSupport.switchToEn(++IMESwitchSupport.seq)
        }
    }
}