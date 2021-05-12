package test.app.calllog.data

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import test.app.calllog.data.local.ContactDBHelper
import test.app.calllog.data.local.KeyValueStorage
import test.app.calllog.data.local.KeyValueStorageImpl
import test.app.calllog.data.rest.CallLogApi
import timber.log.Timber

val dataModule = module {

    single {
        KeyValueStorageImpl(
            androidContext().getSharedPreferences(
                KeyValueStorage.PREF_FILE_NAME,
                Context.MODE_PRIVATE
            )
        )
    } bind (KeyValueStorage::class)

    single {
        ContactDBHelper(androidContext())
    }

    factory {
        CallLogRepository(get(), get())
    }

    factory {
        createReviewsApi(get())
    }

}

private fun interceptor(): HttpLoggingInterceptor {
    val interceptor = HttpLoggingInterceptor()
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    return interceptor
}


//private fun retrofitClient(keyValueStorage: KeyValueStorage): Retrofit {
//
//    val baseUrl = "http://${
//        keyValueStorage.getServerIp()
//    }:${keyValueStorage.getServerPort()}/"
//
//    return Retrofit.Builder().addCallAdapterFactory(CoroutineCallAdapterFactory())
//        .addConverterFactory(
//            MoshiConverterFactory.create()
//        ).client(OkHttpClient.Builder().addInterceptor(interceptor()).build())
//        .baseUrl(baseUrl).build()
//}


private fun createReviewsApi(keyValueStorage: KeyValueStorage): CallLogApi {

    val baseUrl = "http://${
        keyValueStorage.getServerIp()
    }:${keyValueStorage.getServerPort()}/"


    Timber.d("baseUrl is : $baseUrl")

    val retrofit = Retrofit.Builder().addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(
            MoshiConverterFactory.create()
        ).client(OkHttpClient.Builder().addInterceptor(interceptor()).build())
        .baseUrl(baseUrl).build()



    return retrofit.create(CallLogApi::class.java)
}