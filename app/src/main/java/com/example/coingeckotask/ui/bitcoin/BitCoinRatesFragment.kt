package com.example.coingeckotask.ui.bitcoin

import CryptoPriceAdapter
import android.os.Bundle
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
                        tvCurrentPrice.text = "Current Price: ${state.data.bitcoin.usd}"
                        tvLastUpdated.text =
                            "Updated at: ${state.data.bitcoin.lastUpdatedAt.convertUnixToDateTime()}"
                    }
                }

                is State.Error -> {
                    requireContext().showToast(state.message)
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
                    val selectedItem = parent?.getItemAtPosition(position).toString()
                    mViewModel.getCoinHistoricData(
                        DEFAULT_COIN_ID, selectedItem, getStartingDateTimestampInSeconds(),
                        getEndingDateTimestampInSeconds()
                    )
                    mViewModel.getCoinPrice(DEFAULT_COIN_ID, selectedItem)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
    }


}