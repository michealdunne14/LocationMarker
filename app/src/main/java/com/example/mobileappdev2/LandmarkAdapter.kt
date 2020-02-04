package com.example.mobileappdev2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_list.view.*

interface LandmarkListener{
    fun onLandMarkClick(postModel: PostModel)
}

class LandmarkAdapter constructor(private var landmarks: ArrayList<PostModel>,
                                  private val listener: LandmarkListener) : RecyclerView.Adapter<LandmarkAdapter.MainHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LandmarkAdapter.MainHolder {
        return MainHolder(  LayoutInflater.from(parent.context).inflate(
            R.layout.card_list,
            parent,
            false))
    }

    override fun getItemCount(): Int = landmarks.size

    override fun onBindViewHolder(holder: LandmarkAdapter.MainHolder, position: Int) {
        val landmark = landmarks[holder.adapterPosition]
        holder.bind(landmark,listener)
    }

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(postModel: PostModel,listener: LandmarkListener) {
            itemView.mCardName.text = postModel.title
        }
    }

}
