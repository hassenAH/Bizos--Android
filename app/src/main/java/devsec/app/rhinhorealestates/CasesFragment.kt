package devsec.app.RhinhoRealEstates

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import devsec.app.rhinhorealestates.api.RestApiService
import devsec.app.rhinhorealestates.api.RetrofitInstance
import devsec.app.rhinhorealestates.data.Adapter.CaseAdapter

import devsec.app.rhinhorealestates.data.models.Case
import devsec.app.rhinhorealestates.data.models.New
import devsec.app.rhinhorealestates.utils.session.SessionPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CasesFragment : Fragment() {

    lateinit var id : String
    lateinit var sessionPref: SessionPref
    private lateinit var rvCases: RecyclerView
    private lateinit var caseAdapter: CaseAdapter
    lateinit var add_car_button : Button
    lateinit var user : HashMap<String, String>
        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
            val view = inflater.inflate(R.layout.fragment_cases, container, false)
            rvCases = view.findViewById(R.id.CaseAdapter_recycler_view)
            val fragmentmanager = requireFragmentManager()
            rvCases.layoutManager = LinearLayoutManager(requireContext())
            caseAdapter = CaseAdapter(listOf(),fragmentmanager) // create an empty adapter
            rvCases.adapter = caseAdapter
        // Inflate the layout for this fragment
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_car_button = view.findViewById(R.id.add_car_button)
        add_car_button.setOnClickListener {
            val fragment = AddCaseFragment()
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragments_container, fragment)
            fragmentTransaction?.commit()
        }
        sessionPref = SessionPref(requireContext())
        user = sessionPref.getUserPref()
        id = user.get(SessionPref.USER_ID).toString()
        val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
        retIn.getCasesbyAvocat(id).enqueue(object : Callback<List<Case>> {
            override fun onResponse(call: Call<List<Case>>, response: Response<List<Case>>) {
                if (response.isSuccessful) {
                    val cases = response.body();
                    if (cases != null) {
                        caseAdapter.Cases = cases // update the adapter with the retrieved cars
                        caseAdapter.notifyDataSetChanged() // notify the adapter that the data has changed
                    }
                } else {
                    Log.e(TAG, "Failed to get Cases: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Case>>, t: Throwable) {
                Log.e(TAG, "Failed to get Cases", t)
            }
        })
    }

    companion object {
        private const val TAG = "CaseFragment"
    }

}