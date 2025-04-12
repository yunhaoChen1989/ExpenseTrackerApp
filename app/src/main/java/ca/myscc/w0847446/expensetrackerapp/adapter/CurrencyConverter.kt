package ca.myscc.w0847446.expensetrackerapp.adapter

import android.icu.util.Currency
import androidx.room.TypeConverter

class CurrencyConverter {
    @TypeConverter
    fun fromCurrency(currency: Currency?): String? {
        return currency?.currencyCode
    }

    @TypeConverter
    fun toCurrency(currencyCode: String?): Currency? {
        return currencyCode?.let { Currency.getInstance(it) }
    }
}