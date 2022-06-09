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
import com.truesightid.ui.detailclaim.DetailClaimActivity
import com.truesightid.utils.DateUtils
import com.truesightid.utils.Prefs
import com.truesightid.utils.UserAction
import com.truesightid.utils.extension.getTotalWithUnit

class MyBookmarksAdapter(
    private val callback: ItemClaimClickListener,
    private val pref: Prefs
) : RecyclerView.Adapter<MyBookmarksAdapter.MyBookmarkViewHolder>() {
    private val myClaimsList = ArrayList<ClaimEntity>()

    fun setData(data: List<ClaimEntity>?) {
        myClaimsList.clear()
        if (data != null) {
            myClaimsList.addAll(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyBookmarkViewHolder {
        val itemRowClaimsBinding =
            ItemRowClaimsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyBookmarkViewHolder(itemRowClaimsBinding)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: MyBookmarkViewHolder, position: Int) {
        val items = myClaimsList[position]
        with(holder) {
            val claimer = "Claim by ${items.claimer}:"
            binding.tvClaimer.text = claimer
            Glide.with(itemView.context)
                .load(items.image)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error)
                )
                .into(binding.ivClaimer)
            binding.tvTitleClaim.text = items.title
            binding.tvDate.text = DateUtils.getDateTime(items.date.toLong())
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
                binding.tvVoteCount.tag = votes.getValue(items.id)
                when (votes.getValue(items.id)) {
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
            }else{
                binding.tvVoteCount.tag = 0
                binding.ibUpvote.background =
                    itemView.context.getDrawable(R.drawable.ic_upvote)
                binding.ibDownvote.background =
                    itemView.context.getDrawable(R.drawable.ic_downvote)
            }

            binding.ibUpvote.setOnClickListener {
                when (binding.tvVoteCount.tag as Int) {
                    0 -> {
                        binding.ibUpvote.background =
                            itemView.context.getDrawable(R.drawable.ic_upvote_pressed)
                        binding.ibDownvote.background =
                            itemView.context.getDrawable(R.drawable.ic_downvote)
                        items.upvote++
                        binding.tvVoteCount.tag = 1
                        UserAction.applyUserVotes(items.id, 1)
                        callback.onClaimUpvote(items.id)
                    }
                    1 -> {
                        binding.ibUpvote.background =
                            itemView.context.getDrawable(R.drawable.ic_upvote_pressed)
                        binding.ibDownvote.background =
                            itemView.context.getDrawable(R.drawable.ic_downvote)
                        binding.tvVoteCount.tag = 1
                        UserAction.applyUserVotes(items.id, 1)
                    }
                    -1 -> {
                        binding.ibUpvote.background =
                            itemView.context.getDrawable(R.drawable.ic_upvote)
                        binding.ibDownvote.background =
                            itemView.context.getDrawable(R.drawable.ic_downvote)
                        items.upvote++
                        binding.tvVoteCount.tag = 0
                        UserAction.applyUserVotes(items.id, 0)
                        callback.onClaimUpvote(items.id)
                    }
                }
                binding.tvVoteCount.text = getTotalWithUnit(items.upvote - items.downvote)
                return@setOnClickListener
            }

            binding.ibDownvote.setOnClickListener {
                when (binding.tvVoteCount.tag as Int) {
                    0 -> {
                        binding.ibUpvote.background =
                            itemView.context.getDrawable(R.drawable.ic_upvote)
                        binding.ibDownvote.background =
                            itemView.context.getDrawable(R.drawable.ic_downvote_pressed)
                        items.downvote++
                        binding.tvVoteCount.tag = -1
                        UserAction.applyUserVotes(items.id, -1)
                        callback.onClaimDownvote(items.id)
                    }
                    1 -> {
                        binding.ibUpvote.background =
                            itemView.context.getDrawable(R.drawable.ic_upvote)
                        binding.ibDownvote.background =
                            itemView.context.getDrawable(R.drawable.ic_downvote)
                        items.downvote++
                        binding.tvVoteCount.tag = 0
                        UserAction.applyUserVotes(items.id, 0)
                        callback.onClaimDownvote(items.id)
                    }
                    -1 -> {
                        binding.ibUpvote.background =
                            itemView.context.getDrawable(R.drawable.ic_upvote)
                        binding.ibDownvote.background =
                            itemView.context.getDrawable(R.drawable.ic_downvote_pressed)
                        binding.tvVoteCount.tag = -1
                        UserAction.applyUserVotes(items.id, -1)
                    }
                }
                binding.tvVoteCount.text = getTotalWithUnit(items.upvote - items.downvote)
                return@setOnClickListener
            }

            binding.ibBookmark.background =
                itemView.context.getDrawable(R.drawable.ic_bookmark_cardview_pressed)
            binding.ibBookmark.setOnClickListener {
                callback.onRemoveClaimtoBookmark(items.id)
            }

            binding.tvVoteCount.text = getTotalWithUnit(items.upvote - items.downvote)
            binding.ibShare.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Let's join us to discuss the claims from ${items.claimer} regarding ${items.title} in the True Sight ID application."
                )
                intent.type = "text/plain"
                itemView.context.startActivity(Intent.createChooser(intent, "Send to"))
            }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailClaimActivity::class.java)
                intent.putExtra(DetailClaimActivity.EXTRA_CLAIM, items)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = myClaimsList.size


    class MyBookmarkViewHolder(val binding: ItemRowClaimsBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface ItemClaimClickListener {
        fun onClaimUpvote(claim_id: Int)
        fun onClaimDownvote(claim_id: Int)
        fun onRemoveClaimtoBookmark(claim_id: Int)
    }
}