package com.sqy.plugins.translation.engine

import com.sqy.plugins.icons.TranslationIcons
import com.sqy.plugins.translation.setting.dialog.BaiduTranslationDialog
import javax.swing.Icon

enum class TranslationEngineEnum(
        val id:String,
        val translateEngineName:String,
        val icon: Icon?
) {
    Default("doNothing","默认不翻译",null),
    MicroSoft("microSoft","微软翻译",TranslationIcons.Engines.Microsoft),
    Baidu("baidu","百度翻译", TranslationIcons.Engines.Baidu);

    fun hasConfiguration() : Boolean {
        return when(this) {
            Default,MicroSoft -> false
            else -> true
        }
    }

    fun showConfigDialog() : Boolean{
        return when(this) {
            Baidu -> BaiduTranslationDialog().showAndGet()
            else -> false
        }
    }
}