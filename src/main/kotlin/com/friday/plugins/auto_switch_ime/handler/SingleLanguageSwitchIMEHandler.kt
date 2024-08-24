package com.friday.plugins.auto_switch_ime.handler

import com.intellij.lang.Language

/**
 * 单语言输入法自动切换处理器
 */
interface SingleLanguageSwitchIMEHandler : CharTypeSwitchIMEHandler, MouseClickedSwitchIMEHandler, ArrowKeysPressedSwitchIMEHandler, ActionHappenedSwitchIMEHandler{

    fun getLanguage() : Language

}