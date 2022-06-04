package com.truesightid.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.truesightid.R
import com.truesightid.data.source.local.entity.ClaimEntity
import com.truesightid.databinding.ItemRowClaimsBinding
import com.truesightid.utils.Prefs

class ExploreAdapter(private val callback: ItemClaimClickListener, private val pref: Prefs) :
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
    private var downvotePressed = false
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


                val user = pref.getUser()
                val votes = user?.votes as HashMap<Int, Int>
                if (votes.containsKey(items.id)) {
                    when (votes.getValue(items.id)) {
                        0 -> {
                            binding.ibUpvote.background =
                                itemView.context.getDrawable(R.drawable.ic_upvote)
                            binding.ibDownvote.background =
                                itemView.context.getDrawable(R.drawable.ic_downvote)
                        }
                        1 -> {
                            binding.ibUpvote.background =
                                itemView.context.getDrawable(R.drawable.ic_upvote_pressed)
                            binding.ibDownvote.background =
                                itemView.context.getDrawable(R.drawable.ic_downvote)
                        }
                        -1 -> {
                            binding.ibUpvote.background =
                                itemView.context.getDrawable(R.drawable.ic_upvote)
                            binding.ibDownvote.background =
                                itemView.context.getDrawable(R.drawable.ic_downvote_pressed)
                        }
                    }
                }

                binding.ibUpvote.setOnClickListener {
                    when (binding.tvVoteCount.tag as Int) {
                        0 -> {
                            binding.ibUpvote.background =
                                itemView.context.getDrawable(R.drawable.ic_upvote_pressed)
                            binding.ibDownvote.background =
                                itemView.context.getDrawable(R.drawable.ic_downvote)
                            items.upvote++;
                            binding.tvVoteCount.tag = 1
                            callback.onClaimUpvote(items.id)
                        }
                        1 -> {
                            binding.ibUpvote.background =
                                itemView.context.getDrawable(R.drawable.ic_upvote_pressed)
                            binding.ibDownvote.background =
                                itemView.context.getDrawable(R.drawable.ic_downvote)
                            binding.tvVoteCount.tag = 1
                        }
                        -1 -> {
                            binding.ibUpvote.background =
                                itemView.context.getDrawable(R.drawable.ic_upvote)
                            binding.ibDownvote.background =
                                itemView.context.getDrawable(R.drawable.ic_downvote)
                            items.upvote++;
                            binding.tvVoteCount.tag = 0
                            callback.onClaimUpvote(items.id)
                        }
                    }
                    binding.tvVoteCount.text = (items.upvote - items.downvote).toString()
                    return@setOnClickListener
                }

                binding.ibDownvote.setOnClickListener {
                    when (binding.tvVoteCount.tag as Int) {
                        0 -> {
                            binding.ibUpvote.background =
                                itemView.context.getDrawable(R.drawable.ic_upvote)
                            binding.ibDownvote.background =
                                itemView.context.getDrawable(R.drawable.ic_downvote_pressed)
                            items.downvote++;
                            binding.tvVoteCount.tag = -1
                            callback.onClaimDownvote(items.id)
                        }
                        1 -> {
                            binding.ibUpvote.background =
                                itemView.context.getDrawable(R.drawable.ic_upvote)
                            binding.ibDownvote.background =
                                itemView.context.getDrawable(R.drawable.ic_downvote)
                            items.downvote++;
                            binding.tvVoteCount.tag = 0
                            callback.onClaimDownvote(items.id)
                        }
                        -1 -> {
                            binding.ibUpvote.background =
                                itemView.context.getDrawable(R.drawable.ic_upvote)
                            binding.ibDownvote.background =
                                itemView.context.getDrawable(R.drawable.ic_downvote_pressed)
                            binding.tvVoteCount.tag = -1
                        }
                    }
                    binding.tvVoteCount.text = (items.upvote - items.downvote).toString()
                    return@setOnClickListener
                }

                binding.tvVoteCount.text = (items.upvote - items.downvote).toString()
                if (votes.containsKey(items.id)) {
                    binding.tvVoteCount.tag = votes.getValue(items.id)
                }else{
                    binding.tvVoteCount.tag = 0
                }
                itemView.setOnClickListener {
//                    val intent = Intent(itemView.context, DetailClaimActivity::class.java)
//                    intent.putExtra(DetailClaimActivity.EXTRA_CLAIM, items)
//                    itemView.context.startActivity(intent)
                    Toast.makeText(holder.itemView.context, "$user", Toast.LENGTH_SHORT)
                        .show()
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