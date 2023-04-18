package devsec.app.rhinhorealestates.ui.main.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat.recreate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import devsec.app.RhinhoRealEstates.R
import devsec.app.rhinhorealestates.api.RestApiService
import devsec.app.rhinhorealestates.api.RetrofitInstance

import devsec.app.rhinhorealestates.ui.main.view.MainMenuActivity

import devsec.app.rhinhorealestates.utils.services.LoadingDialog
import devsec.app.rhinhorealestates.utils.session.SessionPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalTime

class HomeFragment : Fragment() {
    //************* Recommended food *************//


    private lateinit var username: TextView
    lateinit var usernameString : String
    lateinit var sessionPref: SessionPref

    lateinit var user : HashMap<String, String>

    //***************** isBIO *******************//

    //***************** Loading Dialog *************//
    private lateinit var loadingDialog: LoadingDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navDrawerButton = view.findViewById<Button>(R.id.menu)


        sessionPref = SessionPref(activity?.applicationContext!!)
        user = sessionPref.getUserPref()
        usernameString = user.get(SessionPref.USER_NAME).toString()

        val drawerLayout = (activity as MainMenuActivity).drawerLayout
        navDrawerButton.setOnClickListener {
            drawerLayout.open()
        }


        //******************************** Init Bio Recette List **********************************************//


        fun reloadFragment() {
            activity?.recreate()
        }

        fun dailyFood(): String {
            val current = LocalTime.now()
            val morning = LocalTime.of(6, 0)
            val afternoon = LocalTime.of(12, 0)
            val evening = LocalTime.of(18, 0)
            if (current.isAfter(morning) && current.isBefore(afternoon)) {
                return "Breakfast"
            } else if (current.isAfter(afternoon) && current.isBefore(evening)) {
                return "Lunch"
            }
            return "Dinner"

        }
    }
}

