package com.enfotrix.luckydoller


import android.content.Context
import android.content.SharedPreferences

import com.enfotrix.luckydoller.Models.ModelUser
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class SharedPrefManager(context: Context) {





    private val sharedPref: SharedPreferences = context.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)

    private val editor: SharedPreferences.Editor = sharedPref.edit()



    fun saveUser(user: ModelUser) {

        editor.putString("User", Gson().toJson(user))
        editor.commit()


    }



    fun putToken(docID: String) {
        editor.putString("docID", docID)
        editor.commit()
    }


    fun setLogin(isLoggedIn: Boolean = false) {
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.commit()
    }

    fun isLoggedIn(): Boolean {
        var isLoggedIn:Boolean=false
        if(sharedPref.getBoolean("isLoggedIn", false).equals(null)) isLoggedIn= false
        else if(sharedPref.getBoolean("isLoggedIn", false)==true) isLoggedIn =true
        return isLoggedIn
    }

    fun saveLoginAuth(user: ModelUser,token:String, loggedIn: Boolean){
        saveUser(user)
        putToken(token)
        setLogin(loggedIn)
    }

    fun getUser(): ModelUser {

        val json = sharedPref.getString("User", "") ?: ""
        return Gson().fromJson(json, ModelUser::class.java)

    }


    fun getToken(): String {
        return sharedPref.getString("docID", "")!!
    }


    fun clear() {
        editor.clear()
        editor.commit()
    }



    fun logOut(isLoggedOut: Boolean = false) {
        editor.putBoolean("isLoggedIn", isLoggedOut)
        sharedPref.edit().clear().apply()           ///Clear all the previous preferences on logout time
        editor.commit()
    }

    fun checkLogin(): Boolean {
        return sharedPref.getBoolean("isLoggedIn", false)
    }

    fun setAccountNumber(accountNumber: String) {
        editor.putString("accountNumber", accountNumber)
        editor.commit()
    }

    fun getAccountNumber(): String {
        return sharedPref.getString("accountNumber", "")!!
    }

    fun setTotalAmount(totalAmount: String) {
        editor.putString("totalAmount", totalAmount)
        editor.commit()
    }

    fun getTotalAmount(): String {
        return sharedPref.getString("totalAmount", "")!!
    }

}