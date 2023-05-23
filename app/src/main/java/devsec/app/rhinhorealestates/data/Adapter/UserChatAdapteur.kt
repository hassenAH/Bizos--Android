package devsec.app.rhinhorealestates.data.Adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import devsec.app.RhinhoRealEstates.ChangePassword
import devsec.app.RhinhoRealEstates.PackDetailsFragment
import devsec.app.RhinhoRealEstates.R
import devsec.app.RhinhoRealEstates.chatUsers
import devsec.app.rhinhorealestates.api.RetrofitInstance
import devsec.app.rhinhorealestates.data.models.Pack
import devsec.app.rhinhorealestates.data.models.User
import devsec.app.rhinhorealestates.data.models.UserTest

class UserChatAdapteur(var users: List<UserTest>,private val fragment: chatUsers): RecyclerView.Adapter<UserChatAdapteur.UserViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_chat_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user= users[position]

        holder.itemView.setOnClickListener {
            fragment.startActivityFromAdapter(user)
        }
        holder.bind(user)
    }

    override fun getItemCount() = users.size

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.name)
        private val email: TextView = itemView.findViewById(R.id.email)

        val imageView: ImageView = itemView.findViewById(R.id.imageuser)




        fun bind(user: UserTest) {
            name.text = user.first_name
            email.text = user.email

            val url = RetrofitInstance.BASE_IMG+user.image

            Picasso.get().load(url).into(imageView)


        }

    }
}