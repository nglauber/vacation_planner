package br.com.nglauber.twvacationplanner.presentation

data class ViewEvent <T>(
    val state: Int
) {
    var data: T? = null
        private set
    var error: Throwable? = null
        private set

    constructor(state: Int, data: T): this(state) {
        this.data = data
    }

    constructor(state: Int, error: Throwable): this(state) {
        this.error = error
    }

    companion object {
        const val STATE_UNDEFINED = 0
        const val STATE_LOADING = 1
        const val STATE_SUCCESS = 2
        const val STATE_ERROR = 3
    }
}