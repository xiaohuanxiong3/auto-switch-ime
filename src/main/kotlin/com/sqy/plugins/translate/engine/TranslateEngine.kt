package com.sqy.plugins.translate.engine

import javax.swing.Icon

interface TranslateEngine : Translate {

    val id : String

    val name: String

    val icon: Icon

    /**
     * 主语言，用于设置默认的翻译目标语言
     */
    val primaryLanguage : String


}