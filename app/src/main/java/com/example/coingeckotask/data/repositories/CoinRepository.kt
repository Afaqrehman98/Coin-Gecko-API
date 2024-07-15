package com.example.coingeckotask.data.repositories

import com.example.coingeckotask.data.database.dao.PriceEntryDao
import com.example.coingeckotask.data.models.response.HistoricCoinDataResponse
import com.example.coingeckotask.data.models.response.PriceEntry
import com.example.coingeckotask.data.models.response.SupportedCurrency
import com.example.coingeckotask.data.network.ApiInterface
import com.example.coingeckotask.data.network.SafeApiRequest
import javax.inject.Inject

class CoinRepository @Inject constructor(
    private val api: ApiInterface,
    private val priceEntryDao: PriceEntryDao
) : SafeApiRequest() {

    suspend fun callSupportedCurrency(
    ): SupportedCurrency = apiRequest {
        api.callSupportedCurrency()
    }

    suspend fun getCachedHistoricData(): HistoricCoinDataResponse =
        HistoricCoinDataResponse(priceEntryDao.getPriceHistory())


    suspend fun callCoinHistoricData(
        coinID: String?, vsCurrency: String?, fromDate: Long?, toDate: Long?
    ): HistoricCoinDataResponse = apiRequest {
        api.callCoinHistoricData(coinID, vsCurrency, fromDate, toDate)
    }

    suspend fun saveCacheData(historyData: List<PriceEntry>) {
        priceEntryDao.insertPriceHistory(historyData)
    }

    suspend fun clearCache() {
        priceEntryDao.clearTable()
    }


}