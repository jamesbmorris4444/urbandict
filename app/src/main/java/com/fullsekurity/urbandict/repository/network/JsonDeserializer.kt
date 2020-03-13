package com.fullsekurity.urbandict.repository.network

import android.util.Log
import com.fullsekurity.urbandict.repository.storage.Meaning
import com.fullsekurity.urbandict.utils.Constants
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

internal class MeaningsJsonDeserializer : JsonDeserializer<Any> {

    private val TAG = MeaningsJsonDeserializer::class.java.simpleName

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Any? {
        var meanings: ArrayList<Meaning>? = null
        try {
            val jsonObject = json.asJsonObject
            val meaningsJsonArray = jsonObject.getAsJsonArray(Constants.MOVIES_ARRAY_DATA_TAG)
            meanings = ArrayList(meaningsJsonArray.size())
            for (i in 0 until meaningsJsonArray.size()) {
                val dematerialized = context.deserialize<Any>(meaningsJsonArray.get(i), Meaning::class.java)
                meanings?.add(dematerialized as Meaning)
            }
        } catch (e: JsonParseException) {
            Log.e(TAG, String.format("Could not deserialize Meaning element: %s", json.toString()))
        }

        return meanings
    }

}