package com.example.coingeckotask.data.usecase

import com.example.coingeckotask.data.models.response.CryptoData
import com.example.coingeckotask.data.models.response.PriceEntry
import com.example.coingeckotask.data.models.response.SupportedCurrency
import com.example.coingeckotask.data.repositories.CoinRepository
import com.example.coingeckotask.utils.State
import com.example.coingeckotask.utils.tryCatch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class CoinUseCase @Inject constructor(private val repository: CoinRepository) {

    suspend fun getSupportedCurrencyList(): State<SupportedCurrency>? {
        return tryCatch {
            val response = repository.callSupportedCurrency()
            State.success(response)
        }.getOrElse {
            State.error(it.message ?: "Unknown error")
        }
    }

    suspend fun getCoinPrice(coinID: String?, vsCurrency: String?): State<CryptoData>? {
        return tryCatch {
            val response = repository.callCoinPriceByID(coinID, vsCurrency)
            State.success(response)
        }.getOrElse {
            State.error(it.message ?: "Unknown error")
        }
    }

    suspend fun getCoinHistoricDataFromCache(): List<PriceEntry> {
        return repository.getCachedHistoricData().prices
    }

    suspend fun getCoinHistoricData(coinID: String?, vsCurrency: String?, fromDate: Long?, toDate: Long?): State<List<PriceEntry>>? {
        return tryCatch {
            val response = repository.callCoinHistoricData(coinID, vsCurrency, fromDate, toDate)
            val dailyData = filterToDailyValues(response.prices)
            State.success(dailyData)
        }.getOrElse {
            State.error(it.message ?: "Unknown error")
        }
    }

    private suspend fun filterToDailyValues(prices: List<PriceEntry>): List<PriceEntry> {
        val dailyData = mutableListOf<PriceEntry>()
        var currentDate: Date? = null
        val sortedPrices = prices.sortedByDescending { it.timestamp }
        for (priceEntry in sortedPrices) {
            val entryDate = Date(priceEntry.timestamp)

            if (currentDate == null || !isSameDay(entryDate, currentDate)) {
                dailyData.add(priceEntry)
                currentDate = entryDate
            }
        }

        cacheHistoricData(dailyData)
        return dailyData
    }

    private suspend fun cacheHistoricData(dailyData: List<PriceEntry>) {
        withContext(Dispatchers.IO) {
            repository.clearCache()
            repository.saveCacheData(dailyData)
        }
    }

    private fun isSameDay(entryDate: Date, currentDate: Date): Boolean {
        val cal1 = Calendar.getInstance()
        val cal2 = Calendar.getInstance()
        cal1.time = entryDate
        cal2.time = currentDate
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
    }
}
