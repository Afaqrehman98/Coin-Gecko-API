package com.example.coingeckotask.ui.bitcoin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.coingeckotask.databinding.FragmentBitCoinRatesBinding
import com.example.coingeckotask.ui.base.BaseFragment
import com.example.coingeckotask.ui.viewmodels.CoinViewModel
import com.example.coingeckotask.utils.EventObserver
import com.example.coingeckotask.utils.State
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
    }

    override fun observeAPICall() {

        mViewModel.supportedCurrencyLiveData.observe(viewLifecycleOwner, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                    mViewBinding.apply {
                        requireContext().showToast("Loading")
                    }
                }

                is State.Success -> {
                    if (state.data.isNotEmpty()) {
                        requireContext().showToast(state.data[0])
                        Log.e(
                            BitCoinRatesFragment::class.java.simpleName,
                            "${state.data[10]}"
                        )
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

}