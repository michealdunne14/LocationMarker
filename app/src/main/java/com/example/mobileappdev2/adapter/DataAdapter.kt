package com.example.mobileappdev2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileappdev2.R
import com.example.mobileappdev2.models.Country
import kotlinx.android.synthetic.main.country_list.view.*


interface CountryListener{
    fun onCountryClick(string: String)
}

class DataAdapter(
    private val mCountries: ArrayList<Country>,
    private val listener: CountryListener
): RecyclerView.Adapter<DataAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.country_list,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mCountries.size
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val landmark = mCountries[holder.adapterPosition]
        holder.bind(landmark.countryName,listener)
    }



    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(
            countries: String,
            listener: CountryListener
        ) {
            itemView.mCountryName.text = countries
            itemView.mCountryName.setOnClickListener {
                listener.onCountryClick(countries)
            }
        }
    }

}