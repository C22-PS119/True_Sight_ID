package com.truesightid.ui.detailclaim

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClaimDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imagesAdapter =
            DetailImagesAdapter(this, object : DetailImagesAdapter.DetailImagesCallback {
                override fun onNextArrow(position: Int) {
                    binding.vpImages.currentItem = position
                }

                override fun onPrevArrow(position: Int) {
                    binding.vpImages.currentItem = position
                }

            })
        // Setup back button
        binding.ibBackDetail.setOnClickListener {
            finish()
        }

        // Setup view more button
        binding.tvViewMore.setOnClickListener {
            showAllText()
        }

        // Setup view less button
        binding.tvViewLess.setOnClickListener {
            hideText()
        }

        // Setup comment
        binding.tvMyComment.setOnClickListener {
            binding.tvMyComment.visibility = View.GONE
            binding.etComment.visibility = View.VISIBLE
            binding.etComment.requestFocus()
            showSoftKeyboard(binding.etComment)
        }

        binding.etComment.setOnTouchListener(OnTouchListener { _, event ->
            val drawableEnd = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding.etComment.right - binding.etComment.compoundDrawables[drawableEnd].bounds.width()
                ) {
                    Toast.makeText(this@DetailClaimActivity, "OnDeveloped", Toast.LENGTH_SHORT)
                        .show()
                    return@OnTouchListener true
                }
            }
            false
        })

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

    private fun showSoftKeyboard(etComment: EditText) {
        if (etComment.requestFocus()) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(etComment, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun showAllText() {
        binding.tvViewMore.visibility = View.INVISIBLE
        binding.tvViewLess.visibility = View.VISIBLE
        binding.tvDescription.maxLines = 10000
        binding.tvDescription.ellipsize = null
    }

    private fun hideText() {
        binding.tvViewMore.visibility = View.VISIBLE
        binding.tvViewLess.visibility = View.INVISIBLE
        binding.tvDescription.maxLines = 6
        binding.tvDescription.ellipsize = TextUtils.TruncateAt.END
    }

    private fun setupView(items: ClaimEntity) {
        with(binding) {
            tvTitleDetail.text = items.title
            tvDescription.text = items.description

            imagesAdapter.setImages(items.image)

            var url = StringBuilder()
            items.url.forEachIndexed { index, s ->
                if (index == 0 && items.url.count() == 1) {
                    url = StringBuilder().append(s)
                } else if (index == 0 && items.url.count() > 1) {
                    url = StringBuilder().append("$s\n")
                } else if (index > 0 && items.url.count() > 1 && index != items.url.count()) {
                    url.append("$s\n")
                } else if (index > 0 && items.url.count() > 1 && index == items.url.count()) {
                    url.append(s)
                }
            }

            tvSourceDetail.text = url

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

            binding.ibShare.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Let's join us to discuss the claims from ${items.claimer} regarding ${items.title} in the True Sight ID application."
                )
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, "Send to"))
            }
        }
    }

}