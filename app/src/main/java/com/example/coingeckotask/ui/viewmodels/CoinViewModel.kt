package com.example.coingeckotask.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coingeckotask.data.models.response.HistoricCoinDataResponse
import com.example.coingeckotask.data.models.response.PriceEntry
import com.example.coingeckotask.data.models.response.SupportedCurrency
import com.example.coingeckotask.data.repositories.CoinRepository
import com.example.coingeckotask.utils.Event
import com.example.coingeckotask.utils.State
import com.example.coingeckotask.utils.tryCatch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CoinViewModel @Inject constructor(private val repository: CoinRepository) :
    ViewModel() {

    private val _supportedCurrencyLiveData =
        MutableLiveData<Event<State<SupportedCurrency>>>()
    val supportedCurrencyLiveData: LiveData<Event<State<SupportedCurrency>>>
        get() = _supportedCurrencyLiveData

    private lateinit var supportedCurrencyResponse: SupportedCurrency


    private val _historicCoinLiveData = MutableLiveData<Event<State<List<PriceEntry>>>>()
    val historicCoinLiveData: LiveData<Event<State<List<PriceEntry>>>>
        get() = _historicCoinLiveData

    private lateinit var historicCoinDataResponse: HistoricCoinDataResponse




    fun getSupportedCurrencyList() {
        _supportedCurrencyLiveData.postValue(Event(State.loading()))
        viewModelScope.launch(Dispatchers.IO) {
            val result = tryCatch {
                supportedCurrencyResponse =
                    repository.callSupportedCurrency()
            }
            if (result.isSuccess) {
                withContext(Dispatchers.Main) {
                    _supportedCurrencyLiveData.postValue(
                        Event(
                            State.success(
                                supportedCurrencyResponse
                            )
                        )
                    )
                }
            } else {
                withContext(Dispatchers.Main) {
                    _supportedCurrencyLiveData.postValue(
                        Event(
                            State.error(
                                result.exceptionOrNull()?.message ?: ""
                            )
                        )
                    )
                }
            }
        }
    }



    fun getCoinHistoricData(coinID: String?, vsCurrency: String?, fromDate: Long?, toDate: Long?) {
        viewModelScope.launch(Dispatchers.IO) {
            _historicCoinLiveData.postValue(Event(State.loading()))
            val result = tryCatch {
                historicCoinDataResponse = repository.callCoinHistoricData(coinID, vsCurrency, fromDate, toDate)
            }
            if (result.isSuccess) {
                val dailyData = filterToDailyValues(historicCoinDataResponse.prices)
                withContext(Dispatchers.Main) {
                    _historicCoinLiveData.postValue(Event(State.success(dailyData)))
                }
            } else {
                withContext(Dispatchers.Main) {
                    _supportedCurrencyLiveData.postValue(
                        Event(
                            State.error(
                                result.exceptionOrNull()?.message ?: ""
                            )
                        )
                    )
                }
            }
        }
    }

    private fun filterToDailyValues(prices: List<PriceEntry>): List<PriceEntry> {
        val dailyData = mutableListOf<PriceEntry>()
        var currentDate: Date? = null
        val sortedPrices = prices.sortedByDescending { it.timestamp }
        for (priceEntry in sortedPrices) {
            val entryDate = Date(priceEntry.timestamp)

            if (currentDate == null || !isSameDay(entryDate, currentDate)) {
                dailyData.add(priceEntry)
                currentDate = entryDate
            }
        }

        return dailyData
    }


    private fun isSameDay(entryDate: Date, currentDate: Date): Boolean {
        val cal1 = Calendar.getInstance()
        val cal2 = Calendar.getInstance()
        cal1.time = entryDate
        cal2.time = currentDate
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
    }
}