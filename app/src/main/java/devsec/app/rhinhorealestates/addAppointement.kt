package devsec.app.RhinhoRealEstates

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import devsec.app.rhinhorealestates.api.RestApiService
import devsec.app.rhinhorealestates.api.RetrofitInstance
import devsec.app.rhinhorealestates.utils.session.SessionPref
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import java.text.SimpleDateFormat


import java.util.*



class addAppointement : Fragment() {

    lateinit var user : HashMap<String, String>
    private lateinit var session : SessionPref

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_appointement, container, false)

        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)





        val argument1 = arguments?.getString("user")
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Inflate the popup layout
            val popupView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_form, null)
            val descriptionEditText = popupView.findViewById<Spinner>(R.id.descedittext)
            val models  = arrayOf("Social Right","Commercial And Business Law","Criminal law & Criminal procedure","droit civil","Civil Right")
            val adapter = ArrayAdapter(requireContext(),
                android.R.layout.simple_list_item_1, models)
            descriptionEditText.adapter = adapter

            descriptionEditText.setOnItemSelectedListener(object  : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem  = models[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            })
           // val models  = arrayOf("Social Right","Commercial And Business Law","Criminal law & Criminal procedure","droit civil","Civil Right")


            // Find the EditText views in the popup layout


            // Find the Save button in the popup layout and set its click listener
            val saveButton = popupView.findViewById<Button>(R.id.submitBtn)
            val width = 1000 // Specify the desired width in pixels
            val height = 1000
            val popupWindow = PopupWindow(popupView, width, height, true)
            saveButton.setOnClickListener {
                // Get the selected date, title, and description
                val selectedDate = "$year-${month + 1}-$dayOfMonth"
                val description = descriptionEditText.selectedItem.toString().trim()
                val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date: Date = format.parse(selectedDate)
                // Log the data to the console
                Log.d("MainActivity", "Selected date: $selectedDate, Description: $description")
                session = SessionPref(requireContext())
                val id = session.getUserPref().get(SessionPref.USER_ID).toString()
                val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
                val info = devsec.app.rhinhorealestates.data.models.Appointement(id,"",description,date,argument1!!)
                retIn.addAppointement(info).enqueue(object :
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


                           /* val fragment = packs()
                            val fragmentTransaction = fragmentManager?.beginTransaction()
                            fragmentTransaction?.replace(R.id.fragments_container, fragment)
                            fragmentTransaction?.commit()*/

                        }else{
                            Toast.makeText(
                                requireContext(),
                                "add failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })

                // Dismiss the popup window
                popupWindow.dismiss()
            }

            // Create a PopupWindow with the inflated layout and display it
            popupWindow.showAtLocation(calendarView, Gravity.CENTER, 200, 500)
        }

        return view
    }

    companion object {
        fun newInstance(argument1: String): addAppointement {
            val fragment = addAppointement()
            val args = Bundle()
            args.putString("user", argument1)
            fragment.arguments = args
            return fragment
        }
    }


}