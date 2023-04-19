package devsec.app.rhinhorealestates.ui.main.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.JsonObject
import devsec.app.RhinhoRealEstates.R
import devsec.app.RhinhoRealEstates.databinding.ActivityLoginBinding
import devsec.app.rhinhorealestates.api.RestApiService
import devsec.app.rhinhorealestates.api.RetrofitInstance
import devsec.app.rhinhorealestates.data.api.UserRequest
import devsec.app.rhinhorealestates.utils.services.LoadingDialog
import devsec.app.rhinhorealestates.utils.session.SessionPref
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    lateinit var restApiService: RestApiService
    lateinit var sessionPref: SessionPref

    lateinit var usernameLayout: TextInputLayout
    lateinit var passwordLayout: TextInputLayout
    lateinit var usernameEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var loadingDialog: LoadingDialog
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingDialog = LoadingDialog(this)
        //our login session manager
        sessionPref = SessionPref(this)
        if (sessionPref.isLoggedIn()) {
            val intent = Intent(this, MainMenuActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
        usernameLayout = findViewById(R.id.LoginInputLayout)
        passwordLayout = findViewById(R.id.PasswordInputLayout)
        usernameEditText = findViewById(R.id.loginEditText)
        passwordEditText = findViewById(R.id.passwordEditText)

        val registerBtn = findViewById<TextView>(R.id.RegisterTV)
        val forgetBtn = findViewById<TextView>(R.id.Forget)

        registerBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        forgetBtn.setOnClickListener {
            val intent = Intent(this, ForgetPasswordActivity::class.java)
            startActivity(intent)
        }

        val loginBtn = findViewById<Button>(R.id.LoginBtn)
        loginBtn.setOnClickListener() {
            if (validateLogin(usernameEditText, passwordEditText,passwordLayout)) {
                login()

            }else{
                val toast = Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT)
                toast.show()
            }

        }



    }





    private fun validateLogin(username: EditText, password: EditText,passwordlayout : TextInputLayout): Boolean {
        if(username.text.trim().isEmpty() || password.text.trim().isEmpty()){

            if (password.text.isEmpty()) {
                password.error = "Password is required"
                password.requestFocus()

            }
            // made it revesed so it desplays correctly you ll see it in the app
            if (username.text.isEmpty()) {
                username.error = "Username is required"
                username.requestFocus()

            }

            return false

        }

        return true
    }

    private fun login() {
        loadingDialog.startLoadingDialog()
        val username = binding.loginEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        val userRequest = UserRequest(username,password)
            val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)

            retIn.loginUser(userRequest).enqueue(object : Callback<ResponseBody> {

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    loadingDialog.dismissDialog()
                    Toast.makeText(
                        this@LoginActivity,
                        "Login Failed",
                        Toast.LENGTH_SHORT
                    ).show()


                }
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.code() == 200) {
                        loadingDialog.dismissDialog()
                        // creating a session
                        val gson = Gson()
                        val jsonSTRING = response.body()?.string()
                        val jsonObject = gson.fromJson(jsonSTRING, JsonObject::class.java)
                        val user = jsonObject.get("user").asJsonObject
                        val id_user = user.get("_id").asString
                        val first_name = user.get("first_name").asString
                        val last_name = user.get("last_name").asString
                        val email_user = user.get("email").asString

                        sessionPref.createRegisterSession(id_user, first_name,first_name,email_user, "")
                        Toast.makeText(this@LoginActivity, "Welcome!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@LoginActivity, MainMenuActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else if(response.code() == 401){
                        loadingDialog.dismissDialog()
                        Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        loadingDialog.dismissDialog()
                        Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }