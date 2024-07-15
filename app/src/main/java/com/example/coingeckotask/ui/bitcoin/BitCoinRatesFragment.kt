package com.example.coingeckotask.ui.bitcoin

import CryptoPriceAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.example.coingeckotask.R
import com.example.coingeckotask.data.models.response.PriceEntry
import com.example.coingeckotask.databinding.FragmentBitCoinRatesBinding
import com.example.coingeckotask.ui.base.BaseFragment
import com.example.coingeckotask.ui.viewmodels.CoinViewModel
import com.example.coingeckotask.utils.Constants.DEFAULT_COIN_ID
import com.example.coingeckotask.utils.Constants.DEFAULT_VS_CURRENCY
import com.example.coingeckotask.utils.EventObserver
import com.example.coingeckotask.utils.State
import com.example.coingeckotask.utils.getEndingDateTimestampInSeconds
import com.example.coingeckotask.utils.getStartingDateTimestampInSeconds
import com.example.coingeckotask.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

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
        mViewModel.getCoinHistoricData(
            DEFAULT_COIN_ID,
            DEFAULT_VS_CURRENCY,
            getStartingDateTimestampInSeconds(),
            getEndingDateTimestampInSeconds()
        )
    }

    override fun observeAPICall() {
        observeSupportedCurrency()
        observeHistoricData()
    }

    private fun observeHistoricData() {
        mViewModel.historicCoinLiveData.observe(viewLifecycleOwner, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                    requireContext().showToast("Loading")
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

    private fun observeSupportedCurrency() {
        mViewModel.supportedCurrencyLiveData.observe(viewLifecycleOwner, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                    mViewBinding.apply {
                        requireContext().showToast("Loading")
                    }
                }

                is State.Success -> {
                    if (state.data.isNotEmpty()) {
                        setupSpinner(state.data)
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
            R.layout.spinner_item,
            currencies
        )
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        mViewBinding.spinnerCurrencies.adapter = adapter
    }

}