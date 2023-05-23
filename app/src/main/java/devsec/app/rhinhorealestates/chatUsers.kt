package devsec.app.RhinhoRealEstates

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import devsec.app.rhinhorealestates.api.RestApiService
import devsec.app.rhinhorealestates.api.RetrofitInstance
import devsec.app.rhinhorealestates.data.Adapter.UserChatAdapteur
import devsec.app.rhinhorealestates.data.models.New
import devsec.app.rhinhorealestates.data.models.User
import devsec.app.rhinhorealestates.data.models.UserTest
import devsec.app.rhinhorealestates.ui.main.view.ChatActivity
import devsec.app.rhinhorealestates.utils.session.SessionPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class chatUsers : Fragment() {




    private lateinit var rvUser: RecyclerView
    private lateinit var userAdapter: UserChatAdapteur
    private lateinit var session : SessionPref
    lateinit var SearchEditText: EditText


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat_users, container, false)
        SearchEditText = view.findViewById(R.id.Searchinput)
        rvUser = view.findViewById(R.id.chatUsers_recycler_view)

        // rvnews.layoutManager = LinearLayoutManager(requireContext())
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rvUser.layoutManager = layoutManager
        userAdapter = UserChatAdapteur(listOf(),this) // create an empty adapter
        rvUser.adapter = userAdapter


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SearchEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH) {
                // User has finished typing, perform action
                val searchText = SearchEditText.text.toString()
                // do something with searchText

                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        session = SessionPref(requireContext())
        if (session.getUserPref().get(SessionPref.USER_ROLE).toString()!="Avocat"){
            val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
            retIn.getAvocat().enqueue(object : Callback<List<UserTest>> {
                @SuppressLint("SuspiciousIndentation")
                override fun onResponse(call: Call<List<UserTest>>, response: Response<List<UserTest>>) {
                    if (response.isSuccessful) {
                        val news = response.body();
                        if (news != null) {
                            userAdapter.users = news // update the adapter with the retrieved cars
                            userAdapter.notifyDataSetChanged() // notify the adapter that the data has changed
                        }
                    }
                }

                override fun onFailure(call: Call<List<UserTest>>, t: Throwable) {

                }
            })
        }
        else
        {
            val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
            retIn.getClient().enqueue(object : Callback<List<UserTest>> {
                @SuppressLint("SuspiciousIndentation")
                override fun onResponse(call: Call<List<UserTest>>, response: Response<List<UserTest>>) {
                    if (response.isSuccessful) {
                        val news = response.body();
                        if (news != null) {
                            userAdapter.users = news // update the adapter with the retrieved cars
                            userAdapter.notifyDataSetChanged() // notify the adapter that the data has changed
                        }
                    }
                }

                override fun onFailure(call: Call<List<UserTest>>, t: Throwable) {

                }
            })
        }


    }


    fun startActivityFromAdapter(user: UserTest){
        val intent = Intent(requireContext(), ChatActivity::class.java)
        intent.putExtra("user", user._id)

        startActivity(intent)
    }
}