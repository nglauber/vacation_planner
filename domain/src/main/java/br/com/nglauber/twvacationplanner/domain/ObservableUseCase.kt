package br.com.nglauber.twvacationplanner.domain

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

abstract class ObservableUseCase<T, in Params> constructor(
    private val postExecutionThread: PostExecutionThread
) {
    private val disposables = CompositeDisposable()

    abstract fun buildUseCaseObservable(params: Params? = null): Observable<T>

    open fun execute(
        params: Params? = null,
        onNext: (T) -> Unit,
        onError: (e: Throwable) -> Unit,
        onComplete: (() -> Unit)? = null) {
        val observable = this.buildUseCaseObservable(params)
            .subscribeOn(Schedulers.io())
            .observeOn(postExecutionThread.scheduler)
        addDisposable(observable.subscribe(
            { t: T ->
                onNext.invoke(t)
            },
            { error ->
                onError.invoke(error)
            },
            {
                onComplete?.invoke()
            }
        ))
    }

    fun dispose() {
        disposables.clear()
    }

    private fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }
}