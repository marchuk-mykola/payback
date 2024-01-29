package com.presentation.mvi

/**
 * The `Mvi` object encapsulates a set of interfaces and a class following
 * the Model-View-Intent (MVI) architecture pattern.
 * These interfaces include `ViewState`, `ViewAction`, and `ViewEffect`,
 * which provide the basic structure for components within an MVI-based ViewModel.
 */
object Mvi {

    /**
     * A marker interface that represents a ViewState in the MVI architecture.
     * A ViewState typically encapsulates the entire state of a view at any given point.
     */
    interface ViewState

    /**
     * A marker interface that represents a ViewAction in the MVI architecture.
     * A ViewAction is a user action that triggers some business logic and can potentially change the ViewState.
     */
    interface ViewAction

    /**
     * A marker interface that represents a ViewEffect in the MVI architecture.
     * A ViewEffect is a one-off event like showing a toast or navigating to a different screen.
     */
    interface ViewEffect

    /**
     * A stub class that implements [ViewAction], [ViewEffect], and [ViewState].
     * This can be used when a [BaseViewModel] doesn't need to specify all of these types.
     *
     * The [Stub.initial] instance can be used as an initial state.
     */
    class Stub : ViewAction, ViewEffect, ViewState {
        companion object {
            val initial = Stub()
        }
    }
}
