package br.com.nglauber.twvacationplanner.local

import br.com.nglauber.twvacationplanner.data.repository.Cache
import io.reactivex.Completable
import io.reactivex.Observable

// TODO implement a better cache mechanism
class MemoryCache<T>: Cache<T> {

    private val cachedData = mutableListOf<T>()

    override fun saveToCache(vararg data: T): Completable {
        return Completable.create { emitter ->
            try {
                cachedData.clear()
                cachedData.addAll(data)
                emitter.onComplete()
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    override fun getFromCache(): Observable<List<T>> =
        Observable.create { emitter ->
            if (!cachedData.isEmpty()){
                emitter.onNext(cachedData)
            }
            emitter.onComplete()
        }
}