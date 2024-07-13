package com.example.coingeckotask.ui.splash

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.coingeckotask.R
import com.example.coingeckotask.databinding.FragmentSplashBinding
import com.example.coingeckotask.ui.base.BaseFragment
import com.example.coingeckotask.ui.viewmodels.SplashState
import com.example.coingeckotask.ui.viewmodels.SplashViewModel
import com.example.coingeckotask.utils.navigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment<SplashViewModel, FragmentSplashBinding>() {

    override val mViewModel: SplashViewModel by viewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSplashBinding {
        return FragmentSplashBinding.inflate(inflater, container, false)
    }

    override fun setupUI() {
        mViewBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            tvAppVersion.text = getString(R.string.label_app_version, "1")
        }
    }

    override fun initializeObserver() {
        mViewModel.splashStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is SplashState.SplashScreen -> {
//                    navigate(SplashFragmentDirections.actionSplashFragmentToBitCoinRatesFragment())
                }
            }
        }
    }

}