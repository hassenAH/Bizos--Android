package devsec.app.RhinhoRealEstates

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import devsec.app.rhinhorealestates.api.RestApiService
import devsec.app.rhinhorealestates.api.RetrofitInstance
import devsec.app.rhinhorealestates.data.models.Appointement

import devsec.app.rhinhorealestates.data.Adapter.appointementAdapter
import devsec.app.rhinhorealestates.data.models.Case
import devsec.app.rhinhorealestates.utils.session.SessionPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Appointement : Fragment() {

    lateinit var id : String
    lateinit var sessionPref: SessionPref
    private lateinit var rvdate: RecyclerView
    private lateinit var apAdapter: appointementAdapter

    lateinit var user : HashMap<String, String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_appointement, container, false)
        rvdate = view.findViewById(R.id.Appointment_recycler_view)

        rvdate.layoutManager = LinearLayoutManager(requireContext())
        apAdapter = appointementAdapter(listOf()) // create an empty adapter
        rvdate.adapter = apAdapter
        // Inflate the layout for this fragment
        return view
        // Inflate the layout for this fragment

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionPref = SessionPref(requireContext())
        user = sessionPref.getUserPref()
        id = user.get(SessionPref.USER_ID).toString()
        val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
        retIn.getdatesbyAvocat(id).enqueue(object : Callback<List<Appointement>> {
            override fun onResponse(call: Call<List<Appointement>>, response: Response<List<Appointement>>) {
                if (response.isSuccessful) {
                    val dates = response.body();
                    if (dates != null) {
                        apAdapter.appointements = dates // update the adapter with the retrieved cars
                        apAdapter.notifyDataSetChanged() // notify the adapter that the data has changed
                    }
                } else {
                    Log.e(TAG, "Failed to get Appointement: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Appointement>>, t: Throwable) {
                Log.e(TAG, "Failed to get Appointement", t)
            }
        })
    }

    companion object {
        private const val TAG = "AppointementFragment"
    }

}