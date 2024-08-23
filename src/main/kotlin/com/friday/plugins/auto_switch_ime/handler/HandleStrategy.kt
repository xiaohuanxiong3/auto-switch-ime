package com.friday.plugins.auto_switch_ime.handler

/**
 * DO_NOT_HANDLE：不做处理
 * UPDATE_LOCATION：更新PsiElementLocation
 * UPDATE_LOCATION_AND_SWITCH：更新PsiElementLocation并视情况进行输入法切换
 * @Author Handsome Young
 * @Date 2024/8/23 21:06
 */
enum class HandleStrategy {
    DO_NOT_HANDLE,
    UPDATE_LOCATION,
    UPDATE_LOCATION_AND_SWITCH
}