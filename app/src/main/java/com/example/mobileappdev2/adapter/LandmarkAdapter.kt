package com.example.mobileappdev2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.mobileappdev2.R
import com.example.mobileappdev2.animation.Bounce
import com.example.mobileappdev2.base.BasePresenter
import com.example.mobileappdev2.models.PostModel
import kotlinx.android.synthetic.main.card_list.view.*

interface LandmarkListener{
    fun onLandMarkClick(postModel: PostModel)
}

class LandmarkAdapter(
    private var landmarks: List<PostModel>,
    private val listener: LandmarkListener,
    private val basePresenter: BasePresenter
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
        holder.bind(landmark,listener,basePresenter)
    }

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(
            postModel: PostModel,
            listener: LandmarkListener,
            presenter: BasePresenter
        ) {
            itemView.mCardImageList.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    itemView.mCardName.text = postModel.locations[position].title
                    itemView.mCardLocation.text = "${postModel.locations[position].latitude}  ${postModel.locations[position].longitude}"
                }

            })

            itemView.mCardName.text = postModel.locations[0].title
            itemView.mCardLocation.text = "${postModel.locations[0].latitude}  ${postModel.locations[0].longitude}"
            itemView.mCardDescription.text = postModel.description
            itemView.mCardCountry.text = "Country Visited:${postModel.country}"
            itemView.mCardDate.text = "Date Visited: ${postModel.datevisted}"

            var visitedCheck = postModel.postLiked
            var favouriteCheck = postModel.favourite
            val viewPager = itemView.findViewById<ViewPager>(R.id.mCardImageList)
            val adapter = ImageAdapter(itemView.context, postModel.images)
            viewPager.adapter = adapter
            itemView.setOnClickListener {
                listener.onLandMarkClick(postModel)
            }

            if (visitedCheck){
                itemView.mCardLikeButton.setImageResource(R.drawable.baseline_thumb_up_black_36)
            }else{
                itemView.mCardLikeButton.setImageResource(R.drawable.outline_thumb_up_black_36)
            }

            if (favouriteCheck){
                itemView.mCardFavouriteButton.setImageResource(R.drawable.baseline_star_black_36)
            }else{
                itemView.mCardFavouriteButton.setImageResource(R.drawable.baseline_star_border_black_36)
            }

            itemView.mCardFavouriteButton.setOnClickListener {
                favouriteCheck = !favouriteCheck
//              Trigger animation when liking a post.
                if (favouriteCheck) {
                    val myAnim = AnimationUtils.loadAnimation(itemView.context,
                        R.anim.bounce
                    )
                    val interpolator = Bounce(0.2, 20.0)
                    myAnim.interpolator = interpolator
                    itemView.mCardFavouriteButton.startAnimation(myAnim)
                    itemView.mCardFavouriteButton.setImageResource(R.drawable.baseline_star_black_36)
                    postModel.favourite = true
                }else{
                    val myAnim = AnimationUtils.loadAnimation(itemView.context, R.anim.bounce)
                    val interpolator = Bounce(0.2, 20.0)
                    myAnim.interpolator = interpolator
                    itemView.mCardFavouriteButton.startAnimation(myAnim)
                    itemView.mCardFavouriteButton.setImageResource(R.drawable.baseline_star_border_black_36)
                    postModel.favourite = false
                }
                presenter.updateFavourite(postModel.copy())
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
                presenter.updateLike(postModel.copy())
            }
        }
    }

}
