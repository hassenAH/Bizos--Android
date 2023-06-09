package devsec.app.rhinhorealestates.ui.main.view


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.Menu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import devsec.app.RhinhoRealEstates.*
import devsec.app.RhinhoRealEstates.databinding.ActivityMainMenuBinding
import devsec.app.rhinhorealestates.api.RestApiService
import devsec.app.rhinhorealestates.api.RetrofitInstance
import devsec.app.rhinhorealestates.utils.services.ConnectivityObserver
import devsec.app.rhinhorealestates.utils.services.LoadingDialog
import devsec.app.rhinhorealestates.utils.services.NetworkConnectivityObserver
import devsec.app.rhinhorealestates.utils.session.SessionPref
import kotlinx.coroutines.flow.onEach
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainMenuBinding
    private lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    private lateinit var connectivityObserver: ConnectivityObserver

    private lateinit var sideNav: Menu


    private lateinit var session : SessionPref
    private lateinit var loadingDialog: LoadingDialog
    lateinit var user : HashMap<String, String>

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        connectivityObserver = NetworkConnectivityObserver(applicationContext)
        connectivityObserver.observe().onEach {
            when (it) {
                ConnectivityObserver.Status.AVAILABLE -> Snackbar.make(binding.root, "Internet Connected", Snackbar.LENGTH_SHORT)
                    .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()
                ConnectivityObserver.Status.UNAVAILABLE -> Snackbar.make(binding.root, "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
                    .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).show()
            }
        }

        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingDialog = LoadingDialog (this)
        session = SessionPref(this.applicationContext)
        user = session.getUserPref()
        // Drawer
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.nav_view)
        if (session.getUserPref().get(SessionPref.USER_ROLE).toString()=="Avocat"){
        navigationView.menu.clear() // Clear the current menu
        navigationView.inflateMenu(R.menu.side_nav_menu)}
        else
        {
            navigationView.menu.clear() // Clear the current menu
            navigationView.inflateMenu(R.menu.side_nav_menu_user)
        }
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

//        val deleteitem = navigationView.menu.findItem(R.id.nav_delete_profile)
//        deleteitem.actionView

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {


                R.id.nav_edit_profile -> {
                    val intent = Intent(this, EditProfileActivity::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawer(navigationView)
                }

                R.id.nav_MyAppointment -> replaceFragment(Appointement())
                R.id.nav_myPacks -> replaceFragment(packs())
                R.id.nav_MyCases -> replaceFragment(CasesFragment())
                R.id.nav_changepass -> {
                    val intent = Intent(this, ChangePassword::class.java)
                    startActivity(intent)
                    drawerLayout.closeDrawer(navigationView)
                }

                R.id.nav_logout -> {
                    session = SessionPref(this)
                    session.logoutUser()
                    val intent = Intent(this, LoginActivity::class.java)
                    drawerLayout.closeDrawer(navigationView)
                    startActivity(intent)
                    finish()
                }
                R.id.nav_delete_profile -> {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Delete Account")
                    builder.setMessage("Are you sure you want to delete your account?")
                    builder.setPositiveButton("Yes") { dialog, which ->
                        loadingDialog.startLoadingDialog()
                        deleteAccount()
                        Toast.makeText(applicationContext, "Account deleted", Toast.LENGTH_SHORT).show()
                    }
                    builder.setNegativeButton("No") { dialog, which ->
                        Toast.makeText(applicationContext, "Account not deleted", Toast.LENGTH_SHORT).show()
                    }
                    builder.show()


                }
            }
            true
        }

        val image = user.get(SessionPref.USER_IMAGE).toString()
        val headerView = navigationView.getHeaderView(0)
        val imageV= headerView.findViewById<ImageView>(R.id.navImage)
        val url = RetrofitInstance.BASE_IMG+image

        Picasso.get().load(url).into(imageV)
        val navDrawerButton = findViewById<Button>(R.id.menu)
        navDrawerButton.setOnClickListener {
            drawerLayout.open()
        }
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    if (session.getUserPref().get(SessionPref.USER_ROLE).toString()=="Avocat"){

                        val profileFragment = NewsFragment()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragments_container, profileFragment)
                            .addToBackStack(null)
                            .commit()
                        drawerLayout.closeDrawer(navigationView)
                    }else{
                        val userHome = user_home()
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragments_container, userHome)
                            .addToBackStack(null)
                            .commit()
                        drawerLayout.closeDrawer(navigationView)
                    }

                    true
                }
R.id.message ->{
    val chatFragment = chatUsers()
    supportFragmentManager.beginTransaction()
        .replace(R.id.fragments_container, chatFragment)
        .addToBackStack(null)
        .commit()
    drawerLayout.closeDrawer(navigationView)
    true
}

                R.id.profile -> {

                    val profileFragment = ProfileHomeFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragments_container, profileFragment)
                        .addToBackStack(null)
                        .commit()
                    drawerLayout.closeDrawer(navigationView)
                    true
                }
                else -> false
            }
        }


        // bottom navigation bar
if (
    (session.getUserPref().get(SessionPref.USER_ROLE).toString()=="Avocat")
){
    supportFragmentManager.beginTransaction().replace(R.id.fragments_container, NewsFragment())
        .commit()
}else{
    supportFragmentManager.beginTransaction().replace(R.id.fragments_container, user_home())
        .commit()
}

        }



    private fun deleteAccount() {
        val apiService = RetrofitInstance.getRetrofitInstance().create(
            RestApiService::class.java)
        val call = apiService.deleteUser(session.getUserPref().get(SessionPref.USER_ID).toString())
        call.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    loadingDialog.dismissDialog()
                    Toast.makeText( applicationContext, "Account deleted", Toast.LENGTH_SHORT).show()
                    session.logoutUser()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                loadingDialog.dismissDialog()
                Toast.makeText(applicationContext, "Error deleting account", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragments_container, fragment)
        fragmentTransaction.commit()
        drawerLayout.closeDrawer(navigationView)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}