package com.truesightid.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.truesightid.R
import java.util.*

class DetailImagesAdapter(val context: Context) : PagerAdapter() {
    private val images = ArrayList<String>()
    fun setImages(images: List<String>) {
        this.images.clear()
        this.images.addAll(images)

    }

    override fun getCount(): Int = images.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as ConstraintLayout
    }

    @SuppressLint("SetTextI18n")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val items = images.get(position)
        // on below line we are initializing
        // our layout inflater.
        val mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // on below line we are inflating our custom
        // layout file which we have created.
        val itemView: View = mLayoutInflater.inflate(R.layout.item_row_images, container, false)

        // on below line we are initializing
        // our image and text view with the id.
        val imageView: ImageView = itemView.findViewById<View>(R.id.iv_image) as ImageView
        val textView: TextView = itemView.findViewById(R.id.tv_image_number) as TextView

        // on below line we are setting
        // image resource for image view.
        Glide.with(itemView.context)
            .load(items)
            .apply(
                RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error)
            )
            .into(imageView)

        textView.text = "${position+1}/${count}"
        // on below line we are setting
        // image resource for image view.

        // on the below line we are adding this
        // item view to the container.
        Objects.requireNonNull(container).addView(itemView)

        // on below line we are simply
        // returning our item view.
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }
}