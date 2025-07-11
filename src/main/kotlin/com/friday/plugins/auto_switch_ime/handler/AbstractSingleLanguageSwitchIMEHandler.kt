package com.friday.plugins.auto_switch_ime.handler

import com.friday.plugins.auto_switch_ime.Constants
import com.friday.plugins.auto_switch_ime.MyMap
import com.friday.plugins.auto_switch_ime.areaDecide.AreaDeciderDelegate
import com.friday.plugins.auto_switch_ime.areaDecide.PsiElementLocation
import com.friday.plugins.auto_switch_ime.support.IMEStatus
import com.friday.plugins.auto_switch_ime.support.IMESwitchSupport
import com.friday.plugins.auto_switch_ime.trigger.IMESwitchTrigger
import com.friday.plugins.auto_switch_ime.trigger.IMESwitchTrigger.*
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actions.EnterAction
import com.intellij.psi.PsiElement

abstract class AbstractSingleLanguageSwitchIMEHandler : SingleLanguageSwitchIMEHandler {

    private val psiElementLocation : PsiElementLocation = MyMap.psiElementLocation

    override fun handle(
        trigger: IMESwitchTrigger,
        handleStrategy: HandleStrategy,
        editor: Editor,
        psiElement: PsiElement,
        isLineEnd: Boolean
    ) {
        when (trigger.description) {
            CHAR_TYPE.description -> handleWhenCharType(handleStrategy, editor, psiElement, isLineEnd)
            MOUSE_CLICKED.description -> handleWhenMouseClicked(handleStrategy, editor, psiElement, isLineEnd)
            ARROW_KEYS_PRESSED.description -> handleWhenArrowKeysPressed(handleStrategy, editor, psiElement, isLineEnd)
            AN_ACTION_HAPPENED.description -> handleWhenAnActionHappened(handleStrategy, editor, psiElement, isLineEnd)
            EDITOR_FOCUS_GAINED.description -> {
                // 编辑器获得焦点时，更新psiElementLocation信息
                updatePsiElementLocation(editor, psiElement, isLineEnd)
                // 恢复离开时的输入法状态
//                ApplicationUtil.executeOnPooledThread {
//                    MyMap.editorIMEStatusMap[editor]?.let { IMESwitchSupport.switchTo(++IMESwitchSupport.seq, it) }
//                }
            }
            else -> {
                throw Error(Constants.UNREACHABLE_CODE + "in SingleLanguageSwitchIMEHandler.handle method")
            }
        }
    }

    // 此处默认单纯的不感兴趣的字符输入不会离开或更改当前的 PsiElementLocation（注释区和代码区）
    // 只有选中内容后进行输入时才可能离开或更改当前的 PsiElementLocation，此时需要更新 PsiElementLocation
    // 后续如果有其他情况可以增加判断
    // 如果情况很复杂，可以干脆对不感兴趣的字符输入默认就是 UPDATE_LOCATION 策略
    override fun getHandleStrategyWhenCharTyped(c: Char, editor: Editor): HandleStrategy {
        return  if (editor.selectionModel.hasSelection()) {
                    if (isInterestChar(c)) {
                        HandleStrategy.UPDATE_LOCATION_AND_SWITCH
                    } else {
                        HandleStrategy.UPDATE_LOCATION
                    }
                } else if (isInterestChar(c)) {
                    HandleStrategy.UPDATE_LOCATION_AND_SWITCH
                } else {
                    HandleStrategy.DO_NOT_HANDLE
                }
    }

    private fun isForceSwitchAction(action: AnAction) : Boolean {
        return action is EnterAction
    }

    override fun getHandleStrategyWhenAnActionHappened(action: AnAction, documentChange: Int, caretChange: Int): HandleStrategy {
        if (documentChange > 0) {
            return  if (isForceSwitchAction(action)) {
                        HandleStrategy.UPDATE_LOCATION_AND_FORCE_SWITCH
                    } else {
                        HandleStrategy.UPDATE_LOCATION_AND_SWITCH
                    }
        } else if (caretChange > 0) {
            return HandleStrategy.UPDATE_LOCATION_AND_SWITCH
        } else {
            return HandleStrategy.DO_NOT_HANDLE
        }
    }

    override fun updatePsiElementLocation(editor: Editor, curPsiElement: PsiElement, isLineEnd: Boolean) {
        val curPsiElementLocation = AreaDeciderDelegate.getPsiElementLocation(getLanguage(), curPsiElement, editor.caretModel.offset, isLineEnd)
        psiElementLocation.copyFrom(curPsiElementLocation)
    }

    override fun updatePsiElementLocationAndSwitch(editor: Editor, curPsiElement: PsiElement, isLineEnd : Boolean, forceSwitchInOtherLocation : Boolean, forceSwitchInSecondLanguageEnabledLocation: Boolean) {
        val curPsiElementLocation = AreaDeciderDelegate.getPsiElementLocation(getLanguage(), curPsiElement, editor.caretModel.offset, isLineEnd)
        // 初始状态，直接返回
        if (curPsiElementLocation.isInitState()) return
        // 位于其他区域
        if (curPsiElementLocation.isInOtherLocation()) {
            if (forceSwitchInOtherLocation || !psiElementLocation.isInOtherLocation()) {
                doSwitchWhenInOtherLocation()
            }
            if (!psiElementLocation.isInOtherLocation()) {
                psiElementLocation.copyFrom(curPsiElementLocation)
            }
        } else {
            // 判断当前location是否和上一location相同。
            // 如果相同，则不进行任何操作，由用户自己操作
            // 否则，根据策略进行输入法切换
            if (forceSwitchInSecondLanguageEnabledLocation || !curPsiElementLocation.equal(psiElementLocation)) {
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
//                IMESwitchSupport.switchToZh(++IMESwitchSupport.seq)
            IMESwitchSupport.switchTo(++IMESwitchSupport.seq, IMEStatus.OTHER)
        } else {
//                IMESwitchSupport.switchToEn(++IMESwitchSupport.seq)
            IMESwitchSupport.switchTo(++IMESwitchSupport.seq, IMEStatus.EN)
        }
    }

    private fun doSwitchWhenInOtherLocation() {
//            IMESwitchSupport.switchToEn(++IMESwitchSupport.seq)
        IMESwitchSupport.switchTo(++IMESwitchSupport.seq, IMEStatus.EN)
    }
}