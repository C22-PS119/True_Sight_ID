package com.truesightid.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.truesightid.R
import com.truesightid.data.source.local.entity.ClaimEntity
import com.truesightid.databinding.ItemRowClaimsBinding
import com.truesightid.ui.activity.DetailClaimActivity

class ExploreAdapter(private val callback: ItemClaimClickListener) :
    PagedListAdapter<ClaimEntity, ExploreAdapter.ExploreViewHolder>(DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ClaimEntity>() {
            override fun areItemsTheSame(oldItem: ClaimEntity, newItem: ClaimEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ClaimEntity, newItem: ClaimEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var upvotePressed = false
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExploreViewHolder {
        val itemRowClaimsBinding =
            ItemRowClaimsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExploreViewHolder(itemRowClaimsBinding)
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onBindViewHolder(holder: ExploreViewHolder, position: Int) {
        val items = getItem(position)

        if (items != null) {
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
                binding.tvDate.text = items.date.toString()
                binding.tvClaim.text = items.fake.toString()

                if (items.fake == 1) {
                    binding.tvClaim.text = itemView.context.getString(R.string.fake_status)
                    binding.tvClaim.background = itemView.context.getDrawable(R.drawable.fake_claim)
                } else {
                    binding.tvClaim.text = itemView.context.getString(R.string.fact_status)
                    binding.tvClaim.background = itemView.context.getDrawable(R.drawable.fact_claim)
                }



                binding.ibUpvote.setOnClickListener {
                    if (!upvotePressed) {
                        binding.ibUpvote.background =
                            itemView.context.getDrawable(R.drawable.ic_upvote_pressed)
                        upvotePressed = true
                        binding.tvVoteCount.text = (items.upvote + 1).toString()
                        callback.onClaimUpvote(items.id)
                        return@setOnClickListener
                    } else {
                        binding.ibUpvote.background =
                            itemView.context.getDrawable(R.drawable.ic_upvote)
                        upvotePressed = false
                        binding.tvVoteCount.text = (items.upvote).toString()
                        callback.onClaimDownvote(items.id)
                        return@setOnClickListener
                    }
                }
                binding.tvVoteCount.text = items.upvote.toString()
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailClaimActivity::class.java)
                    intent.putExtra(DetailClaimActivity.EXTRA_CLAIM, items)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    class ExploreViewHolder(val binding: ItemRowClaimsBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface ItemClaimClickListener {
        fun onClaimUpvote(claim_id: Int)
        fun onClaimDownvote(claim_id: Int)
    }

}