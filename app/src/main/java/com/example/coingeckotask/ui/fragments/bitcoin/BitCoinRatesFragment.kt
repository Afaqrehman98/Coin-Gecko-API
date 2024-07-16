package com.example.coingeckotask.ui.fragments.bitcoin

import CryptoPriceAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.example.coingeckotask.data.models.response.PriceEntry
import com.example.coingeckotask.databinding.FragmentBitCoinRatesBinding
import com.example.coingeckotask.ui.base.BaseFragment
import com.example.coingeckotask.ui.viewmodels.CoinViewModel
import com.example.coingeckotask.utils.Constants.DEFAULT_COIN_ID
import com.example.coingeckotask.utils.Constants.UPDATE_INTERVAL
import com.example.coingeckotask.utils.EventObserver
import com.example.coingeckotask.utils.State
import com.example.coingeckotask.utils.convertUnixToDateTime
import com.example.coingeckotask.utils.getEndingDateTimestampInSeconds
import com.example.coingeckotask.utils.getStartingDateTimestampInSeconds
import com.example.coingeckotask.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class BitCoinRatesFragment : BaseFragment<CoinViewModel, FragmentBitCoinRatesBinding>() {
    override val mViewModel: CoinViewModel by viewModels()
    private lateinit var handler: Handler
    private lateinit var updateRunnable: Runnable

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBitCoinRatesBinding {
        return FragmentBitCoinRatesBinding.inflate(inflater, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.getSupportedCurrencyList()
    }

    override fun observeAPICall() {
        mViewModel.getCoinHistoricDataFromCache()
        observeSupportedCurrencies()
        observeCoinPrice()
        observeHistoricData()
    }

    private fun observeCoinPrice() {
        mViewModel.coinPriceLiveData.observe(viewLifecycleOwner, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                    Timber.e("Loading")
                }

                is State.Success -> {
                    mViewBinding.apply {
                        tvCurrentPrice.text =
                            "Current Price: ${state.data.bitcoin.currencies.entries.firstOrNull()?.value}"
                        tvLastUpdated.text =
                            "Updated at: ${state.data.bitcoin.lastUpdatedAt.convertUnixToDateTime()}"
                    }
                }

                is State.Error -> {
                    requireContext().showToast(state.message)
                }

                else -> {
                    Timber.e("Unexpected state: $state")
                }
            }

        })
    }

    private fun observeSupportedCurrencies() {
        mViewModel.supportedCurrencyLiveData.observe(viewLifecycleOwner, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                    Timber.e("Loading")
                }

                is State.Success -> {
                    setupSpinner(state.data)
                }

                is State.Error -> {
                    requireContext().showToast(state.message)
                }

                else -> {
                    Timber.e("Unexpected state: $state")
                }
            }

        })
    }

    private fun observeHistoricData() {
        mViewModel.historicCoinLiveData.observe(viewLifecycleOwner, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                    Timber.e("Loading")
                }

                is State.Success -> {
                    if (state.data.isNotEmpty()) {
                        setHistoricAdapter(state.data)
                    } else {
                        requireContext().showToast("Something went wrong")
                    }
                }

                is State.Error -> {
                    requireContext().showToast(state.message)
                }

                else -> {
                    Timber.e("Unexpected state: $state")
                }
            }
        })
    }


    private fun setHistoricAdapter(historicData: List<PriceEntry>) {
        val cryptoPriceAdapter = CryptoPriceAdapter()
        mViewBinding.rvHistoricCoin.adapter = cryptoPriceAdapter
        cryptoPriceAdapter.submitList(historicData)

    }

    private fun setupSpinner(currencies: List<String>) {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            currencies

        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mViewBinding.spinnerCurrencies.adapter = adapter

        mViewBinding.spinnerCurrencies.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (view != null) {
                        val selectedItem = parent?.getItemAtPosition(position).toString()
                        mViewModel.getCoinHistoricData(
                            DEFAULT_COIN_ID, selectedItem, getStartingDateTimestampInSeconds(),
                            getEndingDateTimestampInSeconds()
                        )
                        mViewModel.getCoinPrice(DEFAULT_COIN_ID, selectedItem)
                        continuousApiCall(selectedItem)
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
    }

    private fun continuousApiCall(selectedCurrency: String) {
        if (!::handler.isInitialized) {
            handler = Handler(Looper.getMainLooper())
        }

        updateRunnable = object : Runnable {
            override fun run() {
                mViewModel.getCoinPrice(DEFAULT_COIN_ID, selectedCurrency)
                handler.postDelayed(this, UPDATE_INTERVAL)
            }
        }

        handler.postDelayed(updateRunnable, UPDATE_INTERVAL)
    }


    override fun onResume() {
        super.onResume()
        if (::handler.isInitialized) {
            handler.post(updateRunnable)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateRunnable)

    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(updateRunnable)
    }

}