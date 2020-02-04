package com.example.mobileappdev2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
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
            itemView.mCardDescription.text = postModel.description
            val viewPager = itemView.findViewById<ViewPager>(R.id.mCardImageList)
            val adapter = ImageAdapter(itemView.context,postModel.images)
            viewPager.adapter = adapter
            itemView.setOnClickListener {
                listener.onLandMarkClick(postModel)
            }
        }
    }

}
