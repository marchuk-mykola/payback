package com.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * An abstract base class for a ViewModel in the MVI architecture,
 * which should be extended when creating new ViewModels.
 *
 * This class includes basic ViewModel logic and properties such as a state, action, and effect, which are
 * key components in the MVI architecture.
 *
 * @param State The ViewState representing the current state of the view. This should be a mutable data class in Kotlin.
 * This class should include all the variable content that the view depends on.
 * Every time there is any user input or action, we expose a modified copy of this class while maintaining
 * the previous state that is not being modified.
 *
 * @param Effect The ViewEffect representing actions that are fire-and-forget
 * and we do not want to maintain their state.
 * Examples of such actions are showing a snackbar or presenting a dialog.
 * This should be a sealed class in Kotlin.
 *
 * @param Action The ViewAction representing all actions or events a user can perform on the view.
 * This is used to pass user input or action to the ViewModel. This can include things like button press,
 * input field value changes, etc. This should also be a sealed class in Kotlin.
 *
 * @param initialState The initial ViewState of the screen, which is required to instantiate the ViewModel.
 *
 * @property process(action: Action) A method to process ViewActions passed by the UI
 * and update ViewState and ViewEffect accordingly.
 */
abstract class BaseViewModel<
      State : Mvi.ViewState,
      Action : Mvi.ViewAction,
      Effect : Mvi.ViewEffect>
    (initialState: State) : ViewModel() {

    /**
     * Indicates whether logging is enabled. Override this variable to enable or disable logging.
     * By default, it is set to *false*. It should not be set to *true* in a release app.
     */
    protected open val isLoggingEnabled: Boolean = false

    /**
     * A [MutableStateFlow] that holds the current state of the screen.
     */
    private val _uiState = MutableStateFlow<State>(initialState)

    /**
     * The public state of the screen that should be observed in the UI.
     * Typically, you would use [androidx.compose.runtime.collectAsState] to collect this state in the UI.
     */
    val uiState: StateFlow<State> = _uiState
    protected val currentState: State
        get() = uiState.value

    private val effectChannel = Channel<Effect>(Channel.BUFFERED)
    val effectsFlow = effectChannel.receiveAsFlow()

    /**
     * A method to process the [Action] passed by the UI and update ViewState and ViewEffect accordingly.
     * This method should be overridden in each ViewModel class that extends this base class.
     */
    abstract fun process(action: Action)

    /**
     * Function to emit [Effect].
     * This should be called when there is a change in the ViewEffect that should be reflected in the UI.
     *
     * @param effect The new [Effect] that should be emitted.
     */
    protected fun emitEffect(effect: Effect) {
        viewModelScope.launch {
            effectChannel.send(effect)
            log("effectChannel", "Effect emitted: $effect")
        }
    }

    /**
     * Function to emit a new [State] by applying a [reducer] function to the current state.
     *
     * @param reducer A function that takes the current [State] and returns a new [State].
     * This function is applied to the current [State] to produce a new [State].
     * Typically used with a data class' `copy` method to emit a new state based on the current one.
     */
    protected fun emit(reducer: State.() -> State) {
        _uiState.value = currentState.reducer()
        log("uiState", "New state: $currentState")
    }

    /**
     * Function to log messages if logging is enabled.
     *
     * @param tag The tag of the log message.
     * @param message The log message.
     */
    private fun log(tag: String, message: String) {
        if (isLoggingEnabled) {
            Timber.tag(tag).i(message)
        }
    }

}
