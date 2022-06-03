package com.truesightid.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.truesightid.R
import com.truesightid.data.source.local.entity.ClaimEntity
import com.truesightid.databinding.ActivityClaimDetailBinding


class DetailClaimActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClaimDetailBinding

    companion object {
        const val EXTRA_CLAIM = "extra_claim"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClaimDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup back button
        binding.ibBackDetail.setOnClickListener {
            finish()
        }

        // Get intent extras
        val extras = intent.extras
        if (extras != null) {
            val items = intent.getParcelableExtra<ClaimEntity>(EXTRA_CLAIM)

            if (items != null) {
                setupView(items)
            }
        }
    }

    private fun setupView(items: ClaimEntity) {
        with(binding) {
            tvTitleDetail.text = items.title
            tvDescription.text = items.description

            Glide.with(applicationContext)
                .load(items.image)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error)
                )
                .into(ivImageDetail)

            tvClaimerDetail.text = getString(R.string.claimed_by, items.claimer)
            tvDateDetail.text = items.date.toString()

            if (items.fake == 1) {
                tvClaim.text = getString(R.string.fake_status)
                tvClaim.background =
                    AppCompatResources.getDrawable(applicationContext, R.drawable.fake_claim)
            } else {
                tvClaim.text = getString(R.string.fact_status)
                tvClaim.background =
                    AppCompatResources.getDrawable(applicationContext, R.drawable.fact_claim)
            }
        }
    }

}