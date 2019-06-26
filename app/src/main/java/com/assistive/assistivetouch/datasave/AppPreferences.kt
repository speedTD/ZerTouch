package com.assistive.assistivetouch.datasave

import android.content.Context
import android.content.SharedPreferences

object AppPreferences {
    private const val NAME = "Touchshared"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences
    // list of app specific preferences
    private val IS_FIRST_RUN_PREF = Pair("is_first_run", false)
    private val IS_FIRST_INTRO_PREF = Pair("is_first_intro", false)
    private val IS_COLOR_BACKGROUND = Pair("color_back_ground",0)
    private val IS_ICON = Pair("icon_touch","")
    private val IS_ICONNUMBER = Pair("iconnumber_touch",1)
    private val IS_ADMIN = Pair("admin",false)
    private val IS_ACBILITY = Pair("accsibilitypermistion",false)
    private val IS_AC_CONECTED = Pair("accsibilityconnected",false)
    private val IS_NOTI = Pair("noti_grand",false)
    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }
    /**
     * SharedPreferences extension function, so we won't need to call edit() and apply()
     * ourselves on every SharedPreferences operation.
     */
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }
    var firstRun: Boolean

        get() = preferences.getBoolean(IS_FIRST_RUN_PREF.first, IS_FIRST_RUN_PREF.second)

        set(value) = preferences.edit {
            it.putBoolean(IS_FIRST_RUN_PREF.first, value)
        }

    var colorbackground: Int

        get() = preferences.getInt(IS_COLOR_BACKGROUND.first, IS_COLOR_BACKGROUND.second)

        set(value) = preferences.edit {
            it.putInt(IS_COLOR_BACKGROUND.first, value)
        }



    var isadmin: Boolean

        get() = preferences.getBoolean(IS_ADMIN.first, IS_ADMIN.second)

        set(value) = preferences.edit {
            it.putBoolean(IS_ADMIN.first, value)
        }
    var icon: String

        get() = preferences.getString(IS_ICON.first, IS_ICON.second)

        set(value) = preferences.edit {
            it.putString(IS_ICON.first, value)
        }
    var iconnumber: Int

        get() = preferences.getInt(IS_ICONNUMBER.first, IS_ICONNUMBER.second)

        set(value) = preferences.edit {
            it.putInt(IS_ICONNUMBER.first, value)
        }
    var isAccesbisilitypermistion: Boolean

        get() = preferences.getBoolean(IS_ACBILITY.first, IS_ACBILITY.second)

        set(value) = preferences.edit {
            it.putBoolean(IS_ACBILITY.first, value)
        }
    var isAccesbisilityconnected: Boolean

        get() = preferences.getBoolean(IS_AC_CONECTED.first, IS_AC_CONECTED.second)

        set(value) = preferences.edit {
            it.putBoolean(IS_AC_CONECTED.first, value)
        }
    var fistintro: Boolean

        get() = preferences.getBoolean(IS_FIRST_INTRO_PREF.first, IS_FIRST_INTRO_PREF.second)

        set(value) = preferences.edit {
            it.putBoolean(IS_FIRST_INTRO_PREF.first, value)
        }
    var noti: Boolean

        get() = preferences.getBoolean(IS_NOTI.first, IS_NOTI.second)

        set(value) = preferences.edit {
            it.putBoolean(IS_NOTI.first, value)
        }



}