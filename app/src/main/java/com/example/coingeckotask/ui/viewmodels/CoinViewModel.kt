package com.example.coingeckotask.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coingeckotask.data.models.response.SupportedCurrency
import com.example.coingeckotask.data.repositories.CoinRepository
import com.example.coingeckotask.utils.Event
import com.example.coingeckotask.utils.State
import com.example.coingeckotask.utils.tryCatch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CoinViewModel @Inject constructor(private val repository: CoinRepository) :
    ViewModel() {

    private val _supportedCurrencyLiveData =
        MutableLiveData<Event<State<SupportedCurrency>>>()
    val supportedCurrencyLiveData: LiveData<Event<State<SupportedCurrency>>>
        get() = _supportedCurrencyLiveData

    private lateinit var supportedCurrencyResponse: SupportedCurrency

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
}