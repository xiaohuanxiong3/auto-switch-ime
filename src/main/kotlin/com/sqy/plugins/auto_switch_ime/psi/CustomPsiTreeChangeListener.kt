package com.sqy.plugins.auto_switch_ime.psi

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.psi.*
import com.intellij.psi.impl.PsiTreeChangeEventImpl
import com.sqy.plugins.auto_switch_ime.Map
import com.sqy.plugins.auto_switch_ime.PsiFileLanguage
import com.sqy.plugins.auto_switch_ime.util.PsiFileUtil
import java.util.concurrent.ConcurrentHashMap

class CustomPsiTreeChangeListener : PsiTreeChangeAdapter() {

    private val editorPsiFileMap : ConcurrentHashMap<Editor, PsiFile> = Map.editorPsiFileMap
    private val psiFileMap : ConcurrentHashMap<PsiFile, Editor> = Map.psiFileEditorMap
    private val logger : Logger = Logger.getInstance(CustomPsiTreeChangeListener::class.java)

    // TODO 使用 ReadAction 读取 PsiFile 的内容提供安全和一致性保障
    // PsiFile树改变完成后判断光标所处位置
    // 此方法优点是几乎能覆盖所有需要输入法切换的场景
    //      缺点是触发次数频繁
    override fun childrenChanged(event: PsiTreeChangeEvent) {
//        if (curAction != null) {
//            return
//        }
        if (event !is PsiTreeChangeEventImpl || !event.isGenericChange  || !PsiFileLanguage.isLanguageAutoSwitchEnabled(event.file?.language)) {
            return
        }
        event.file?.let { psiFile ->
            checkAndHandleCache(psiFile)
            val editor = psiFileMap[psiFile]!!
            val curPsiElement = psiFile.findElementAt(editor.caretModel.offset)
            curPsiElement?.let { psiElement ->
//                // 处理字符串字面量
//                if (psiElement is PsiLiteralExpression) {
//                    // 更新 PsiElementLocation
//                    PsiTreeChangeHandlerDelegate.getPsiTreeChildChangedHandler(psiFile.language)
//                        ?.handleStringLiteralExpressionAddition(editor, psiElement)
//                }
//                // 单行注释末尾
//                else if (isLineEnd(editor.caretModel.offset, psiElement) && psiElement.prevSibling is PsiComment) {
//                    println("woo")
//                } else {
//
//                }
                PsiFileLanguage.getSingleLanguageSwitchIMEHandler(psiFile.language)?.switch(editor, psiElement, isLineEnd(editor.caretModel.offset, psiElement));
            }
        }
    }

    // 判断光标是否在行尾
    private fun isLineEnd(offset : Int, psiElement: PsiElement?) : Boolean {
        return psiElement is PsiWhiteSpace
                && psiElement.text.contains("\n")
                // 当包含多个'\n'，只有psiElement开始处直到第一个换行符（包含第一个换行符）才被认为是行尾
                // 当进行字符输入走到这时，offset已经更新，而psiElement似乎还未更新
                && offset <= psiElement.textRange.startOffset + psiElement.text.indexOf('\n')
                && psiElement.textRange.endOffset - psiElement.text.length <= offset
    }

    // 检查并处理缓存（必要的话）
    private fun checkAndHandleCache(psiFile: PsiFile) {
        // PsiFile缓存失效
        if (!psiFileMap.containsKey(psiFile)) {
            PsiFileUtil.getEditor(psiFile)?.let {
                // 移除旧缓存
                psiFileMap.remove(editorPsiFileMap[it])
                // 添加新缓存
                editorPsiFileMap[it] = psiFile
                psiFileMap[psiFile] = it
            }
        }
    }

    companion object{
        var curAction : String? = null
    }
}