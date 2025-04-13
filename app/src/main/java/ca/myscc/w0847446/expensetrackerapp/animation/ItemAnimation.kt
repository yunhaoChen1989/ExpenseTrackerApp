package ca.myscc.w0847446.expensetrackerapp.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

class ItemAnimation: DefaultItemAnimator() {
    //when user add an item,this method will be called
    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        holder.itemView.alpha = 0f
        holder.itemView.translationX = holder.itemView.width.toFloat()

        val animatorSet = android.animation.AnimatorSet()
        //slide on when added item
        animatorSet.playTogether(
            ObjectAnimator.ofFloat(holder.itemView, "alpha", 0f, 1f),
            ObjectAnimator.ofFloat(holder.itemView, "translationX", holder.itemView.width.toFloat(), 0f)
        )
        animatorSet.duration = 300

        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                //let recycle view know that animation finished
                dispatchAddFinished(holder)
            }
        })
        animatorSet.start()

        return false
    }

    //when user delete an item,this method will be called
    override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
        val animatorSet = android.animation.AnimatorSet()
        //slide out when user delete
        animatorSet.playTogether(
            ObjectAnimator.ofFloat(holder.itemView, "alpha", 1f, 0f),
            ObjectAnimator.ofFloat(holder.itemView, "translationX", 0f, -holder.itemView.width.toFloat())
        )
        animatorSet.duration = 300

        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                //let recycle view know that animation finished
                dispatchRemoveFinished(holder)
            }
        })
        animatorSet.start()

        return false
    }

    //reset when animation ended
    override fun endAnimation(item: RecyclerView.ViewHolder) {
        super.endAnimation(item)
        item.itemView.alpha = 1f;
        item.itemView.translationX = 0f;
    }
}