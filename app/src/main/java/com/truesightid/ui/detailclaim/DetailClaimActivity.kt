package com.truesightid.ui.detailclaim

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.truesightid.R
import com.truesightid.data.source.local.entity.ClaimEntity
import com.truesightid.databinding.ActivityClaimDetailBinding
import com.truesightid.ui.adapter.DetailImagesAdapter
import com.truesightid.utils.DateUtils


class DetailClaimActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClaimDetailBinding
    private lateinit var imagesAdapter: DetailImagesAdapter

    companion object {
        const val EXTRA_CLAIM = "extra_claim"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClaimDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imagesAdapter = DetailImagesAdapter(this)
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

        // Set adapter
        binding.vpImages.adapter = imagesAdapter

    }

    private fun setupView(items: ClaimEntity) {
        with(binding) {
            tvTitleDetail.text = items.title
            tvDescription.text = items.description

            imagesAdapter.setImages(items.image)


            tvClaimerDetail.text = getString(R.string.claimed_by, items.claimer)
            tvDateDetail.text = DateUtils.getDateTime(items.date.toLong())

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