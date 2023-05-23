package devsec.app.rhinhorealestates.data.Adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import devsec.app.RhinhoRealEstates.AvocatListActivity
import devsec.app.RhinhoRealEstates.AvocatProfile
import devsec.app.RhinhoRealEstates.PackDetailsFragment
import devsec.app.RhinhoRealEstates.R
import devsec.app.rhinhorealestates.api.RetrofitInstance
import devsec.app.rhinhorealestates.data.models.Pack

import devsec.app.rhinhorealestates.data.models.User
import devsec.app.rhinhorealestates.data.models.UserTest

class UsersAdapter (var users: List<UserTest>): RecyclerView.Adapter<UsersAdapter.UsersViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_avocat, parent, false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user= users[position]
        holder.bind(user)


        holder.itemView.setOnClickListener {

            val intent = Intent(it.context, AvocatProfile::class.java).apply {

                putExtra("user", user)
            }
            intent.putExtra("idAvocat",user._id)
            it.context.startActivity(intent)
        }


    }

    override fun getItemCount() = users.size

    inner class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val firstname: TextView = itemView.findViewById(R.id.firstName1)

        val imageView: ImageView = itemView.findViewById(R.id.imagelistAvocat)
        val experience: TextView = itemView.findViewById(R.id.experiance)
        val categorie: TextView = itemView.findViewById(R.id.categorieUser)




        fun bind(user: UserTest) {
            Log.d("testing ", "bind: ${user.specialite}")
            firstname.text =user.first_name + " "+user.last_name
            categorie.text = user.specialite
            experience.text = "experience " +user.experience.toString() + " "+"ans"

            val url = RetrofitInstance.BASE_IMG+user.image

            Picasso.get().load(url).into(imageView)


        }
    }

}
