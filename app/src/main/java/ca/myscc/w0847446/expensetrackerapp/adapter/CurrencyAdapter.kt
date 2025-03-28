package ca.myscc.w0847446.expensetrackerapp.adapter

import android.icu.util.Currency
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter


class CurrencyAdapter : TypeAdapter<Currency>() {
    override fun write(out: JsonWriter, value: Currency?) {
        out.value(value?.currencyCode)
    }

    override fun read(`in`: JsonReader): Currency {
        return Currency.getInstance(`in`.nextString())
    }
}