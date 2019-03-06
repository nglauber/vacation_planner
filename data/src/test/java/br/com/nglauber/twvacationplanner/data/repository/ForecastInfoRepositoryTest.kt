package br.com.nglauber.twvacationplanner.data.repository

import br.com.nglauber.tests.DataMockFactory
import br.com.nglauber.twvacationplanner.data.model.ForecastInfo
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ForecastInfoRepositoryTest {
    private lateinit var remote: Remote
    private lateinit var repository: ForecastInfoRepository

    @Before
    fun setup() {
        remote = mock()
        repository = ForecastInfoRepository(remote)
    }

    @Test
    fun getForecastInfoCompletes() {
        val list = DataMockFactory.makeForecastInfoList(2)
        stubRemote(Observable.just(list))
        repository.getAnnualForecastInfoByCity("", 0)
            .test()
            .assertComplete()
            .assertNoErrors()
    }

    @Test
    fun getForecastInfoReturnsData() {
        val listRemote = DataMockFactory.makeForecastInfoList(2)
        stubRemote(Observable.just(listRemote))
        repository.getAnnualForecastInfoByCity("", 0)
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue { result ->
                result.sortedBy { it.date } == listRemote.sortedBy { it.date }
            }
    }

    private fun stubRemote(observable: Observable<List<ForecastInfo>>) {
        whenever(remote.getAnnualForecastInfoByCity(any(), any())).thenReturn(observable)
    }
}