package devsec.app.rhinhorealestates.utils.session

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import devsec.app.rhinhorealestates.ui.main.view.LoginActivity

class SessionPref {
    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var context: Context
    var PRIVATE_MODE = 0

    constructor(context: Context) {
        this.context = context
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }


    companion object {
        const val PREF_NAME = "login_pref"
        const val IS_LOGGED_IN = "is_logged_in"
        const val USER_ID = "user_id"
        const val USER_firstname = "firstname"
        const val USER_lastname= "lastname"
        const val USER_NAME = "user_name"
        const val USER_EMAIL = "user_email"
        const val USER_PASSWORD = "user_password"
        const val USER_PHONE = "user_phone"
        const val USER_EXPERIENCE = "user_experience"
        const val USER_ROLE = "user_role"
        const val USER_SPECIALITE = "user_specialite"
        const val USER_ADDRESS = "user_address"
        const val USER_IMAGE = "user_image"
    }

    fun createLoginSession(username: String, password: String) {
        editor.putBoolean(IS_LOGGED_IN, true)
        editor.putString(USER_NAME, username)
        editor.putString(USER_PASSWORD, password)
        editor.commit()
    }

    fun createRegisterSession(
        id: String,
        role: String,
        firstname: String,
        lastname: String,
        email: String


    )
    {
        editor.putBoolean(IS_LOGGED_IN, true)
        editor.putString(USER_ID, id)
        editor.putString(USER_firstname, firstname)
        editor.putString(USER_lastname, lastname)
        editor.putString(USER_EMAIL, email)
        editor.putString(USER_ROLE, role)
      //  editor.putString(USER_IMAGE, image)

        editor.commit()
    }

    fun updateProfileSession(id: String, username: String, email: String,password: String)
    {
        editor.putString(USER_ID, id)
        editor.putString(USER_NAME, username)
        editor.putString(USER_EMAIL, email)
        editor.putString(USER_PASSWORD, password)


        editor.commit()
    }


    fun checkLogin() {
        if (!this.isLoggedIn()) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    fun getUserPref(): HashMap<String, String> {
        val user = HashMap<String, String>()
        user.put(USER_ID, pref.getString(USER_ID, null)!!)
        user.put(USER_NAME, pref.getString(USER_NAME, "username")!!)
        user.put(USER_EMAIL, pref.getString(USER_EMAIL, "email")!!)
        user.put(USER_SPECIALITE, pref.getString(USER_ROLE, "specialite")!!)
        user.put(USER_EXPERIENCE, pref.getString(USER_EXPERIENCE, "experience")!!)
        user.put(USER_ROLE, pref.getString(USER_ROLE, "role")!!)
        user.put(USER_firstname, pref.getString(USER_firstname, "firstname")!!)
        user.put(USER_lastname, pref.getString(USER_lastname,"lastname")!!)
        user.put(USER_PHONE, pref.getString(USER_PHONE, "phone")!!)
        user.put(USER_IMAGE, pref.getString(USER_IMAGE, null)!!)
        return user

    }

    fun setUserPrefImage(username: String)
    {
        editor.putString(USER_IMAGE, username)


        editor.commit()
    }

    fun logoutUser() {
        editor.clear()
        editor.commit()
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
    fun isLoggedIn(): Boolean {
        return pref.getBoolean(IS_LOGGED_IN, false)
    }

    fun getId(): String {
        return pref.getString(USER_ID, "id")!!
    }


}