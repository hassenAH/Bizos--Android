package devsec.app.rhinhorealestates.data.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import devsec.app.rhinhorealestates.data.models.Appointement
import devsec.app.RhinhoRealEstates.R

import java.text.SimpleDateFormat


class appointementAdapter(var appointements: List<Appointement>): RecyclerView.Adapter<appointementAdapter.AppointementViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointementViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_appointement_item, parent, false)
        return AppointementViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppointementViewHolder, position: Int) {
        val app= appointements[position]
        holder.bind(app)
    }

    override fun getItemCount() = appointements.size

    inner class AppointementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        private val nameAndLastName: TextView = itemView.findViewById(R.id.nameAndLastName)
        private val Date: TextView = itemView.findViewById(R.id.Date)

        private val Cat: TextView = itemView.findViewById(R.id.categ)






        fun bind(ap: Appointement) {



                nameAndLastName.text = ap.Username.toString();

            val formatter = SimpleDateFormat("yyyy-MM-dd")
            Date.text= formatter.format(ap.Date)
            Cat.text= ap.categorie



        }
    }
}
