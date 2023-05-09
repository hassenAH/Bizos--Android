package devsec.app.RhinhoRealEstates

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import devsec.app.rhinhorealestates.api.RestApiService
import devsec.app.rhinhorealestates.api.RetrofitInstance
import devsec.app.rhinhorealestates.data.Adapter.CaseAdapter
import devsec.app.rhinhorealestates.data.Adapter.PackAdapter
import devsec.app.rhinhorealestates.data.models.Case
import devsec.app.rhinhorealestates.data.models.Pack
import devsec.app.rhinhorealestates.utils.session.SessionPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class packs : Fragment() {
    lateinit var id : String
    lateinit var sessionPref: SessionPref
    private lateinit var rvPack: RecyclerView
    private lateinit var PackAdapter: PackAdapter
    lateinit var add_Pack_button : Button
    lateinit var user : HashMap<String, String>

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentmanager = requireFragmentManager()

        val view = inflater.inflate(R.layout.fragment_packs, container, false)
        rvPack = view.findViewById(R.id.PackAdpter_recycler_view)

        rvPack.layoutManager = LinearLayoutManager(requireContext())
        PackAdapter = PackAdapter(listOf(), fragmentmanager) // create an empty adapter
        rvPack.adapter = PackAdapter
        // Inflate the layout for this fragment
        return view


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_Pack_button = view.findViewById(R.id.add_Pack)
        add_Pack_button.setOnClickListener {
            val fragment = Add_Pack()
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragments_container, fragment)
            fragmentTransaction?.commit()
        }
        sessionPref = SessionPref(requireContext())
        user = sessionPref.getUserPref()
        id = user.get(SessionPref.USER_ID).toString()
        val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
        retIn.getPackbyAvocat(id).enqueue(object : Callback<List<Pack>> {
            override fun onResponse(call: Call<List<Pack>>, response: Response<List<Pack>>) {
                if (response.isSuccessful) {
                    val packs = response.body();
                    if (packs != null) {
                        PackAdapter.Packs = packs // update the adapter with the retrieved cars
                        PackAdapter.notifyDataSetChanged() // notify the adapter that the data has changed
                    }
                } else {
                    Log.e(TAG, "Failed to get Cases: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Pack>>, t: Throwable) {
                Log.e(TAG, "Failed to get Cases", t)
            }
        })
    }

    companion object {
        private const val TAG = "CaseFragment"
    }

}