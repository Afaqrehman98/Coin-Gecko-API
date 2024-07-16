package com.example.coingeckotask.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coingeckotask.data.models.response.CryptoData
import com.example.coingeckotask.data.models.response.PriceEntry
import com.example.coingeckotask.data.models.response.SupportedCurrency
import com.example.coingeckotask.data.usecase.CoinUseCase
import com.example.coingeckotask.utils.Event
import com.example.coingeckotask.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinViewModel @Inject constructor(
    private val coinUseCase: CoinUseCase
) : ViewModel() {

    private val _supportedCurrencyLiveData =
        MutableLiveData<Event<State<SupportedCurrency>?>>()
    val supportedCurrencyLiveData: LiveData<Event<State<SupportedCurrency>?>>
        get() = _supportedCurrencyLiveData

    private val _historicCoinLiveData = MutableLiveData<Event<State<List<PriceEntry>>?>>()
    val historicCoinLiveData: LiveData<Event<State<List<PriceEntry>>?>>
        get() = _historicCoinLiveData

    private val _coinPriceLiveData =
        MutableLiveData<Event<State<CryptoData>?>>()
    val coinPriceLiveData: LiveData<Event<State<CryptoData>?>>
        get() = _coinPriceLiveData

    fun getSupportedCurrencyList() {
        _supportedCurrencyLiveData.postValue(Event(State.loading()))
        viewModelScope.launch {
            val result = coinUseCase.getSupportedCurrencyList()
            _supportedCurrencyLiveData.postValue(Event(result))
        }
    }

    fun getCoinPrice(coinID: String?, vsCurrency: String?) {
        _coinPriceLiveData.postValue(Event(State.loading()))
        viewModelScope.launch {
            val result = coinUseCase.getCoinPrice(coinID, vsCurrency)
            _coinPriceLiveData.postValue(Event(result))
        }
    }

    fun getCoinHistoricDataFromCache() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = coinUseCase.getCoinHistoricDataFromCache()
            _historicCoinLiveData.postValue(Event(State.success(result)))
        }
    }

    fun getCoinHistoricData(coinID: String?, vsCurrency: String?, fromDate: Long?, toDate: Long?) {
        _historicCoinLiveData.postValue(Event(State.loading()))
        viewModelScope.launch {
            val result = coinUseCase.getCoinHistoricData(coinID, vsCurrency, fromDate, toDate)
            _historicCoinLiveData.postValue(Event(result))
        }
    }
}
