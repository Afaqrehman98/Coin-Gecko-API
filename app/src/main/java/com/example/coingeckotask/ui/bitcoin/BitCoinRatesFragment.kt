package com.example.coingeckotask.ui.bitcoin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.coingeckotask.databinding.FragmentBitCoinRatesBinding
import com.example.coingeckotask.databinding.FragmentSplashBinding
import com.example.coingeckotask.ui.base.BaseFragment
import com.example.coingeckotask.ui.viewmodels.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BitCoinRatesFragment : BaseFragment<SplashViewModel, FragmentBitCoinRatesBinding>() {
    override val mViewModel: SplashViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBitCoinRatesBinding {
        return FragmentBitCoinRatesBinding.inflate(inflater, container, false)
    }

}