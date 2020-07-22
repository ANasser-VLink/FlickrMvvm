package com.subwilven.basemodel.project_base.utils

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.subwilven.basemodel.project_base.BaseApplication
import java.io.Serializable

public object PrefManager {

    const val LANGUAGE = "language"
    const val FIRST_LAUNCH = "first_launch"
    const val USER = "user"
    const val DYNAMIC_LINK_EXIST = "dynamic_link_exist"
    const val DATABASE_VERSION = "database_version"

    var instance: SharedPreferences? = null
        private set
        get() {
            return if (field == null) {
                PreferenceManager.getDefaultSharedPreferences(BaseApplication.instance?.applicationContext)
            } else {
                field
            }
        }


}

public inline fun SharedPreferences.editPref(operation: (SharedPreferences.Editor) -> Unit) {
    val editor = this.edit()
    operation(editor)
    editor.apply()
}


public operator fun SharedPreferences.set(key: String?, value: Any) {
    when (value) {
        is String -> editPref { it.putString(key, value) }
        is Int -> editPref { it.putInt(key, value) }
        is Boolean -> editPref { it.putBoolean(key, value) }
        is Float -> editPref { it.putFloat(key, value) }
        is Long -> editPref { it.putLong(key, value) }
        is Serializable -> editPref { it.putString(key, Gson().toJson(value)) }
        else -> throw UnsupportedOperationException("Not Yet Implemented")
    }
}

public inline operator fun <reified T : Any> SharedPreferences.get(key: String?, defaultValue: T?): T? {
    return when (defaultValue) {
        is String -> getString(key, defaultValue as? String) as? T
        is Int -> getInt(key, defaultValue as? Int ?: -1) as? T
        is Boolean -> getBoolean(key, defaultValue as? Boolean ?: false) as? T
        is Float -> getFloat(key, defaultValue as? Float ?: 0.0f) as? T
        is Long -> getLong(key, defaultValue as? Long ?: 0L) as? T
        is Serializable -> Gson().fromJson(getString(key, defaultValue as? String ?),T::class.java)
        else -> throw java.lang.UnsupportedOperationException("Not Yet Implemented T ")
    }
}

public inline operator fun <reified T : Any> SharedPreferences.get(key: String?, defaultValue: Class<T>?): T? {
    return when (T::class) {
        Serializable::class -> {
            Gson().fromJson(getString(key, ""), defaultValue)
        }

        else -> defaultValue as? T
    }
}
