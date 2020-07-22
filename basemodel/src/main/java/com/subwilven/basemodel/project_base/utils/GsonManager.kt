package com.subwilven.basemodel.project_base.utils

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*


public object GsonManager {

    //TODO need to be tested
    //you should ovveride toString in the POJO to store only the values you waint
    fun <T> convertListToString(items: List<T>): String {
        Objects.requireNonNull(items)

        val gson = Gson()
        val objStrings = ArrayList<String>()
        for (item in items) {
            objStrings.add(gson.toJson(item))
        }
        return TextUtils.join("‚‗‚", objStrings)
    }

    //TODO need to be tested
    inline fun <reified T> convertStringToList(string: String): List<T> {
        Objects.requireNonNull(string)

        val gson = Gson()

        // retrieve the stored string and split it into array
        val objStrings = ArrayList(Arrays.asList(*TextUtils.split(string, "‚‗‚")))

        val items = ArrayList<T>()

        for (jObjString in objStrings) {
            val value = gson.fromJson(jObjString, T::class.java)
            items.add(value)
        }
        return items
    }

}
