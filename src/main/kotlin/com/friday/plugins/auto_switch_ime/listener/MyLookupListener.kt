package com.friday.plugins.auto_switch_ime.listener

import com.intellij.codeInsight.lookup.LookupEvent
import com.intellij.codeInsight.lookup.LookupListener

/**
 * 用户选择自动补全项后导致document和光标变化而需要进行输入法切换判断
 * @Author Handsome Young
 * @Date 2024/8/28 13:33
 */
class MyLookupListener : LookupListener {

    // 暂时不需要
    override fun itemSelected(event: LookupEvent) {

    }

}