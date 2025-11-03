package com.example.appgamezone_008v_grupo14.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class UserRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("local_users_prefs", Context.MODE_PRIVATE)

    fun saveUser(user: User) {
        prefs.edit()
            .putString(KEY_FULLNAME, user.fullName)
            .putString(KEY_EMAIL, user.email)
            .putString(KEY_PASSWORD, user.password)
            .putString(KEY_PHONE, user.phone ?: "")
            .putStringSet(KEY_GENRES, user.genres.toSet())
            .apply()
        Log.d(TAG, "saveUser -> ${user.email}")
    }

    fun getSavedUser(): User? {
        val email = prefs.getString(KEY_EMAIL, null) ?: run {
            Log.d(TAG, "getSavedUser -> null")
            return null
        }
        val fullName = prefs.getString(KEY_FULLNAME, null) ?: return null
        val password = prefs.getString(KEY_PASSWORD, null) ?: return null
        val phone = prefs.getString(KEY_PHONE, null)
        val genresSet = prefs.getStringSet(KEY_GENRES, emptySet()) ?: emptySet()

        val u = User(
            fullName = fullName,
            email = email,
            password = password,
            phone = phone,
            genres = genresSet.toList()
        )
        Log.d(TAG, "getSavedUser -> ${u.email}")
        return u
    }

    fun isEmailAlreadyRegistered(email: String): Boolean {
        val saved = getSavedUser() ?: return false
        val exists = saved.email.equals(email, ignoreCase = true)
        Log.d(TAG, "isEmailAlreadyRegistered(${email}) -> $exists")
        return exists
    }

    fun validateCredentials(email: String, password: String): Boolean {
        val saved = getSavedUser() ?: run {
            Log.d(TAG, "validateCredentials(no user) -> false")
            return false
        }
        val ok = saved.email.equals(email, ignoreCase = true) && saved.password == password
        Log.d(TAG, "validateCredentials(${email}) -> $ok")
        return ok
    }

    fun clearUser() {
        prefs.edit().clear().apply()
        Log.d(TAG, "clearUser -> done")
    }

    companion object {
        private const val TAG = "USER_REPO"
        private const val KEY_FULLNAME = "user_fullname"
        private const val KEY_EMAIL = "user_email"
        private const val KEY_PASSWORD = "user_password"
        private const val KEY_PHONE = "user_phone"
        private const val KEY_GENRES = "user_genres"
    }
}
