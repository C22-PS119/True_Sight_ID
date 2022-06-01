package com.truesightid.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.truesightid.R
import com.truesightid.data.source.local.entity.ClaimEntity
import com.truesightid.databinding.ItemRowClaimsBinding
import com.truesightid.ui.activity.DetailClaimActivity

class ExploreAdapter : RecyclerView.Adapter<ExploreAdapter.ExploreViewHolder>() {
    private var listExplores = ArrayList<ClaimEntity>()

    fun setClaims(list: List<ClaimEntity>) {
        listExplores.addAll(list)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExploreViewHolder {
        val itemRowClaimsBinding =
            ItemRowClaimsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExploreViewHolder(itemRowClaimsBinding)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ExploreViewHolder, position: Int) {
        val items = listExplores[position]

        with(holder) {
            val claimer = "Claim by ${items.claimer}:"
            binding.tvClaimer.text = claimer
            Glide.with(itemView.context)
                .load(items.image)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error)
                )
                .into(binding.imgClaimer)
            binding.tvTitleClaim.text = items.title
            binding.tvDate.text = items.date
            binding.tvClaim.text = items.fake.toString()

            if (items.fake) {
                binding.tvClaim.text = itemView.context.getString(R.string.fake_status)
                binding.tvClaim.background = itemView.context.getDrawable(R.drawable.fake_claim)
            } else {
                binding.tvClaim.text = itemView.context.getString(R.string.fact_status)
                binding.tvClaim.background = itemView.context.getDrawable(R.drawable.fact_claim)
            }

//            val voteCount = "${items.voteCount} K" // buat nanti jika ribuan votes
            binding.tvVoteCount.text = items.voteCount.toString()
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailClaimActivity::class.java)
                intent.putExtra(DetailClaimActivity.EXTRA_CLAIM, items)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = listExplores.size

    class ExploreViewHolder(val binding: ItemRowClaimsBinding) :
        RecyclerView.ViewHolder(binding.root)

}