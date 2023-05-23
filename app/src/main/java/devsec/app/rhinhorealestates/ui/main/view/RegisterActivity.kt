package devsec.app.rhinhorealestates.ui.main.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import devsec.app.RhinhoRealEstates.R
import devsec.app.RhinhoRealEstates.databinding.ActivityRegisterBinding
import devsec.app.rhinhorealestates.api.RestApiService
import devsec.app.rhinhorealestates.api.RetrofitInstance
import devsec.app.rhinhorealestates.data.models.User
import devsec.app.rhinhorealestates.utils.services.LoadingDialog
import devsec.app.rhinhorealestates.utils.session.SessionPref

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback


class RegisterActivity : AppCompatActivity() {
    val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }

    lateinit var registerbtn : Button
    lateinit var sessionPref: SessionPref
    lateinit var loadingDialog: LoadingDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        loadingDialog = LoadingDialog(this)
        sessionPref = SessionPref(this)
        if (sessionPref.isLoggedIn()) {
            val intent = Intent(this, MainMenuActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        registerbtn = findViewById<Button>(R.id.RegisterBTN)

        registerbtn.setOnClickListener {
            val first_name = findViewById<EditText>(R.id.first_name)
            val last_name = findViewById<EditText>(R.id.last_name)
            val password = findViewById<EditText>(R.id.passwordInputEditText)
            val verifPass = findViewById<EditText>(R.id.passwordInputEditText2)
            val email = findViewById<EditText>(R.id.emailEditText)

            if (validateRegister(first_name,last_name, password, verifPass, email)) {
                register(first_name.text.toString().trim(),last_name.text.toString().trim(),  password.text.toString().trim(), email.text.toString().trim())}
        }



    }

    private fun register(first_name: String,last_name: String, password: String, email: String) {
        loadingDialog.startLoadingDialog()

        val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
        val id : String = ""
        val registerInfo = User(id, first_name, last_name,email, password,"")

        retIn.registerUser(registerInfo).enqueue(object :
            Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                loadingDialog.dismissDialog()
                Toast.makeText(
                    this@RegisterActivity,
                    t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            override fun onResponse(
                call: Call<ResponseBody>,
                response: retrofit2.Response<ResponseBody>

            ) {
                loadingDialog.dismissDialog()
                if (response.code() == 200) {
                    val gson = Gson()
                    val jsonSTRING = response.body()?.string()
                    val jsonObject = gson.fromJson(jsonSTRING, JsonObject::class.java)
                    Toast.makeText(
                        this@RegisterActivity,
                        jsonObject.get("message").asString,
                        Toast.LENGTH_SHORT
                    ).show()
                    val user = jsonObject.get("user")?.asJsonObject
                    if (user != null) {
                        val id_user = user.get("_id").asString

                        val email_user = user.get("email").asString
                        val first_name = user.get("first_name").asString
                        val last_name = user.get("last_name").asString
                        sessionPref.createRegisterSession(id_user, "User",first_name,last_name,email_user,"image.png")

                        val intent = Intent(this@RegisterActivity, MainMenuActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }else{
                    Toast.makeText(
                        this@RegisterActivity,
                        "Register failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    public fun validateRegister(first_name: EditText,last_name: EditText, password: EditText, verifPass: EditText, email: EditText): Boolean {
        if (first_name.text.trim().isEmpty() ||last_name.text.trim().isEmpty() || password.text.trim().isEmpty() || verifPass.text.trim().isEmpty() || email.text.trim().isEmpty()) {



            if (email.text.isEmpty()) {
                email.error = "Email is required"
                email.requestFocus()

            }


            if (verifPass.text.isEmpty()) {
                verifPass.error = "Password does not match"
                verifPass.requestFocus()

            }

            if (password.text.isEmpty()) {
                password.error = "Password is required"
                password.requestFocus()

            }
            if (first_name.text.isEmpty()) {
                first_name.error = "first name  is required"
                first_name.requestFocus()

            }

            if (last_name.text.isEmpty()) {
                last_name.error = "last name is required"
                last_name.requestFocus()

            }

            return false
        }

        //Patterns // Regex // Length
        if (password.text.length < 6){
            password.error = "Password must be at least 6 characters"
            password.requestFocus()
            return false
        }

        if (password.text.toString() != verifPass.text.toString()){
            verifPass.error = "Password does not match"
            verifPass.requestFocus()
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.text).matches()){
            email.error = "Email unvalid"
            email.requestFocus()
            return false
        }



        return true
    }


}