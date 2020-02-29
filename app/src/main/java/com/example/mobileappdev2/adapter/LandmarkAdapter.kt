package com.example.mobileappdev2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.mobileappdev2.R
import com.example.mobileappdev2.animation.Bounce
import com.example.mobileappdev2.firebase.FireStore
import com.example.mobileappdev2.models.PostModel
import com.example.mobileappdev2.room.MemoryStoreRoom
import kotlinx.android.synthetic.main.card_list.view.*
import org.jetbrains.anko.doAsync

interface LandmarkListener{
    fun onLandMarkClick(postModel: PostModel)
}

class LandmarkAdapter(
    private var landmarks: List<PostModel>,
    private val listener: LandmarkListener,
    private val memoryStore: FireStore
) : RecyclerView.Adapter<LandmarkAdapter.MainHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_list,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = landmarks.size

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val landmark = landmarks[holder.adapterPosition]
        holder.bind(landmark,listener,memoryStore)
    }

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(
            postModel: PostModel,
            listener: LandmarkListener,
            memoryStore: FireStore
        ) {
            itemView.mCardName.text = postModel.title
            itemView.mCardDescription.text = postModel.description
            itemView.mCardCountry.text = "Country Visited:${postModel.country}"
            itemView.mCardDate.text = "Date Visited: ${postModel.datevisted}"
            var visitedCheck = postModel.postLiked
            val viewPager = itemView.findViewById<ViewPager>(R.id.mCardImageList)
            val adapter = ImageAdapter(
                itemView.context,
                postModel.images
            )
            viewPager.adapter = adapter
            itemView.setOnClickListener {
                listener.onLandMarkClick(postModel)
            }

            if (visitedCheck){
                itemView.mCardLikeButton.setImageResource(R.drawable.baseline_thumb_up_black_36)
            }else{
                itemView.mCardLikeButton.setImageResource(R.drawable.outline_thumb_up_black_36)
            }

            itemView.mCardLikeButton.setOnClickListener {
                visitedCheck = !visitedCheck
//              Trigger animation when liking a post.
                if (visitedCheck) {
                    val myAnim = AnimationUtils.loadAnimation(itemView.context,
                        R.anim.bounce
                    )
                    val interpolator = Bounce(0.2, 20.0)
                    myAnim.interpolator = interpolator
                    itemView.mCardLikeButton.startAnimation(myAnim)
                    itemView.mCardLikeButton.setImageResource(R.drawable.baseline_thumb_up_black_36)
                    postModel.postLiked = true

                }else{
                    val myAnim = AnimationUtils.loadAnimation(itemView.context,
                        R.anim.bounce
                    )
                    val interpolator = Bounce(0.2, 20.0)
                    myAnim.interpolator = interpolator
                    itemView.mCardLikeButton.startAnimation(myAnim)
                    itemView.mCardLikeButton.setImageResource(R.drawable.outline_thumb_up_black_36)
                    postModel.postLiked = false
                }
                doAsync {
                    memoryStore.update(postModel.copy())
                }
            }
        }
    }

}
