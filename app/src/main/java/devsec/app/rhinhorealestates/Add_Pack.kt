package devsec.app.RhinhoRealEstates

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import devsec.app.rhinhorealestates.api.RestApiService
import devsec.app.rhinhorealestates.api.RetrofitInstance
import devsec.app.rhinhorealestates.data.models.Case
import devsec.app.rhinhorealestates.data.models.Pack
import devsec.app.rhinhorealestates.utils.session.SessionPref
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback


class Add_Pack : Fragment() {


    lateinit var add : Button
    lateinit var user : HashMap<String, String>
    lateinit var id : String
    lateinit var sessionPref: SessionPref
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add__pack, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        add = view.findViewById(R.id.AddPack)
        sessionPref = SessionPref(requireContext())
        user = sessionPref.getUserPref()
        id = user.get(SessionPref.USER_ID).toString()
        add.setOnClickListener {
            val name = view.findViewById<EditText>(R.id.nameEditText)

            val title = view.findViewById<EditText>(R.id.TitleEditText)
            val description = view.findViewById<EditText>(R.id.DescriptionEditText)
            val price = view.findViewById<EditText>(R.id.PriceEditText)
            Toast.makeText(
                requireContext(),
                "add failed",
                Toast.LENGTH_SHORT
            ).show()
            if (validatePack(name,title,description,price)) {
                addPack(name.text.toString().trim(),title.text.toString().trim(),description.text.toString().trim(),price.text.toString().trim().toDouble(),id)
            }
        }


    }




    private fun addPack(name: String, title: String, description: String, price : Number,idAvocat:String) {


        val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)

        val registerInfo = Pack(title,idAvocat,description,name,price)

        retIn.addPack(registerInfo).enqueue(object :
            Callback<ResponseBody> {

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                Toast.makeText(
                    requireContext(),
                    t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            override fun onResponse(
                call: Call<ResponseBody>,
                response: retrofit2.Response<ResponseBody>

            ) {

                if (response.code() == 200) {
                    val gson = Gson()
                    val jsonSTRING = response.body()?.string()
                    val jsonObject = gson.fromJson(jsonSTRING, JsonObject::class.java)
                    Toast.makeText(
                        requireContext(),
                        jsonObject.get("message").asString,
                        Toast.LENGTH_SHORT
                    ).show()


                    val fragment = packs()
                    val fragmentTransaction = fragmentManager?.beginTransaction()
                    fragmentTransaction?.replace(R.id.fragments_container, fragment)
                    fragmentTransaction?.commit()

                }else{
                    Toast.makeText(
                        requireContext(),
                        "add failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
    public fun validatePack(name: EditText, title: EditText, description: EditText, price: EditText): Boolean {
        if (name.text.trim().isEmpty()  || title.text.trim().isEmpty() || description.text.trim().isEmpty() || price.text.trim().isEmpty()) {



            if (name.text.isEmpty()) {
                name.error = "last_name is required"
                name.requestFocus()

            }




            if (title.text.isEmpty()) {
                title.error = "title is required"
                title.requestFocus()

            }

            if (description.text.isEmpty()) {
                description.error = "description is required"
                description.requestFocus()

            }

            if (price.text.isEmpty()) {
                price.error = "price is required"
                price.requestFocus()

            }
            return false
        }




        return true
    }

}