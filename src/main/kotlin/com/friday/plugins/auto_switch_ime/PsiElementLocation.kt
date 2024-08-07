package com.friday.plugins.auto_switch_ime

import com.intellij.psi.PsiElement

/**
 * 主要思想是用户第一次进入注释区或字符串字面量区域，进行必要的输入法自动切换
 * 之后如果用户触发的光标移动没有离开上面说的区域，则不做处理，由用户决定此区域的输入法状态
 */
class PsiElementLocation {

    /**
     * 第一语言为英语
     * 第二语言为汉语等
     * 属性表示此位置是否允许非英语字符输入
     * 暂时只作为标识
     */
    var isSecondLanguageEnabled : Boolean = false

    /**
     * 是否位于注释区
     */
    var isCommentArea : Boolean = false

    /**
     * 位置标识
     */
    var locationId : String = Constants.INIT_LOCATION_ID

    fun isInitState() : Boolean {
        return locationId == Constants.INIT_LOCATION_ID
    }

    fun reset() {
        isSecondLanguageEnabled = false
        isCommentArea = false
        locationId = Constants.INIT_LOCATION_ID
    }

    fun setLocationId(psiElement: PsiElement) {
        locationId = psiElement.javaClass.simpleName + "@" + System.identityHashCode(psiElement)
    }

    /**
     * 供单行注释使用
     * 用前一个PsiElement和后一个PsiElement标识单行注释的原因在于：
     * 单行注释改变时对应的PsiElement会发生改变（触发PsiTreeChangeListener的childReplaced相关方法）
     */
    fun setLocationId(prePsiElement: PsiElement?, nextPsiElement: PsiElement?) {
        locationId = Constants.SINGLE_LINE_COMMENT_BETWEEN
        if (prePsiElement == null) {
            locationId += "-" + "null"
        } else {
            locationId += "-" + prePsiElement.javaClass.simpleName +
                    "@" + System.identityHashCode(prePsiElement)
        }
        if (nextPsiElement == null) {
            locationId += "-" + "null"
        } else {
            locationId += "-" + nextPsiElement.javaClass.simpleName +
                    "@" + System.identityHashCode(nextPsiElement)
        }
    }

    fun equal(that : PsiElementLocation) : Boolean {
        return locationId == that.locationId
    }

    fun copyFrom(that : PsiElementLocation) {
        isSecondLanguageEnabled = that.isSecondLanguageEnabled
        isCommentArea = that.isCommentArea
        locationId = that.locationId
    }

}