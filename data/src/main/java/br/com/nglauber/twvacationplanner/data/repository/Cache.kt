package br.com.nglauber.twvacationplanner.data.repository

import io.reactivex.Completable
import io.reactivex.Observable

interface Cache<T> {
    fun saveToCache(vararg data: T): Completable

    fun getFromCache(): Observable<List<T>>
}
