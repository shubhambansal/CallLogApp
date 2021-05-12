package test.app.calllog.screens

import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import test.app.calllog.screens.log.CallLogViewModel
import test.app.calllog.screens.splash.SplashViewModel

val viewModelModule = module {


    viewModel {
        SplashViewModel(get())
    }

    viewModel {
        CallLogViewModel(get())
    }
}