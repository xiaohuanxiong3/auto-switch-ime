package com.friday.plugins.auto_switch_ime.ui

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.IconWidgetPresentation
import com.intellij.openapi.wm.WidgetPresentationDataContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.job
import javax.swing.Icon
import kotlin.time.Duration.Companion.milliseconds

/**
 * 官方标记实现依赖为 ApiStatus.Experimental，暂时使用老的实现
 * @Author Handsome Young
 * @Date 2024/9/6 16:57
 */
class AutoSwitchIMEPanel(private val dataContext: WidgetPresentationDataContext, scope: CoroutineScope) : IconWidgetPresentation {

    private val updateIconRequests = MutableSharedFlow<Unit>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    init {
        val disposable = Disposer.newDisposable()
//        val connection = dataContext.project.messageBus.connect(disposable)
        scope.coroutineContext.job.invokeOnCompletion { Disposer.dispose(disposable) }
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun createEditorFlow(): Flow<Editor?> {
        return merge(updateIconRequests.mapLatest { dataContext.currentFileEditor.value }, dataContext.currentFileEditor)
            .debounce(100.milliseconds)
            .mapLatest { fileEditor ->
                fileEditor?.let { it as? TextEditor }?.editor
            }
    }

    override fun icon(): Flow<Icon?> {
        TODO("Not yet implemented")
    }

}