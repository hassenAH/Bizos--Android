package devsec.app.rhinhorealestates.data.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import devsec.app.RhinhoRealEstates.R
import devsec.app.rhinhorealestates.data.models.Case

class CaseAdapter(var Cases: List<Case>): RecyclerView.Adapter<CaseAdapter.CaseViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_case_item, parent, false)
        return CaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CaseViewHolder, position: Int) {
        val case= Cases[position]
        holder.bind(case)
    }

    override fun getItemCount() = Cases.size

    inner class CaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.title)
        private val description: TextView = itemView.findViewById(R.id.description)
        private val Prix: TextView = itemView.findViewById(R.id.Prix)

        private val nameUser: TextView = itemView.findViewById(R.id.nameUser)

        private val LastnameUser: TextView = itemView.findViewById(R.id.LastnameUser)





        fun bind(case: Case) {
            if(!case.traite) {
                title.text = case.title
                description.text = case.description
                nameUser.text = case.nameUser
                LastnameUser.text = case.LastnameUser
                Prix.text = case.Prix.toString() + "$"
            }



        }
    }
}
