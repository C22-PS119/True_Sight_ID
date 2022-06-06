package com.truesightid.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.truesightid.R
import com.truesightid.databinding.ItemEvidenceImageBinding

class AddClaimAdapter : RecyclerView.Adapter<AddClaimAdapter.AddClaimViewHolder>() {
    private val images = ArrayList<Uri>()
    fun setImages(images: Uri) {
        this.images.add(images)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddClaimViewHolder {
        val itemRowEvidenceBinding =
            ItemEvidenceImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddClaimViewHolder(itemRowEvidenceBinding)
    }

    override fun onBindViewHolder(holder: AddClaimViewHolder, position: Int) {
        val items = images[position]
        with(holder) {
            Glide.with(itemView.context)
                .load(items)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error)
                )
                .into(binding.ivEvidence)
        }
    }

    override fun getItemCount(): Int = images.size

    class AddClaimViewHolder(val binding: ItemEvidenceImageBinding) :
        RecyclerView.ViewHolder(binding.root)
}