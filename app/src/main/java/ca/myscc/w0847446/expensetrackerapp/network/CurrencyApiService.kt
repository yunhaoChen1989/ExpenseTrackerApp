package ca.myscc.w0847446.expensetrackerapp.network

import ca.myscc.w0847446.expensetrackerapp.data.CurrencyInfo
import retrofit2.http.GET

interface CurrencyApiService {
    @GET("cad.json")
    suspend fun getCurrencyList(): CurrencyInfo
}