package com.friday.plugins.auto_switch_ime.handler

/**
 * DO_NOT_HANDLE：不做处理
 * UPDATE_LOCATION：更新PsiElementLocation
 * UPDATE_LOCATION_AND_SWITCH：更新PsiElementLocation并视情况进行输入法切换,当前区域是其他区域（PsiElementLocation.isInOtherLocation）时判断之前的区域是否也是其他区域来决定是否进行输入法切换
 * UPDATE_LOCATION_AND_FORCE_SWITCH: 更新PsiElementLocation并视情况进行输入法切换,当前区域是其他区域时强制进行输入法切换
 * @Author Handsome Young
 * @Date 2024/8/23 21:06
 */
enum class HandleStrategy {
    /**
     * 不做处理
     */
    DO_NOT_HANDLE,

    /**
     * 更新PsiElementLocation
     */
    UPDATE_LOCATION,

    /**
     * 更新PsiElementLocation并视情况进行输入法切换,当前区域是其他区域（PsiElementLocation.isInOtherLocation）时判断之前的区域是否也是其他区域来决定是否进行输入法切换
     */
    UPDATE_LOCATION_AND_SWITCH,

    /**
     * 更新PsiElementLocation并视情况进行输入法切换,当前区域是其他区域时强制进行输入法切换，即切换为英文
     */
    UPDATE_LOCATION_AND_FORCE_SWITCH
}