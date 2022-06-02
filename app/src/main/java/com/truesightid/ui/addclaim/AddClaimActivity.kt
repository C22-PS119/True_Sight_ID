package com.truesightid.ui.addclaim

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.truesightid.R
import com.truesightid.data.source.local.entity.ClaimEntity
import com.truesightid.databinding.ActivityAddClaimBinding
import com.truesightid.ui.ViewModelFactory
import com.truesightid.ui.main.MainActivity

class AddClaimActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddClaimBinding
    private var fake: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[AddClaimViewModel::class.java]
        binding = ActivityAddClaimBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.factClaim.setOnClickListener(this)
        binding.fakeClaim.setOnClickListener(this)

        // Setup back button
        binding.ibBackDetail.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        binding.btnSubmit.setOnClickListener { submitClaim(viewModel) }
    }

    private fun submitClaim(viewModel: AddClaimViewModel) {
        val title = binding.edtTitle.text.toString()
        val description = binding.edtDescription.text.toString()
//        val url = binding.edtUrl.text.toString()
        val itemClaimEntity = ClaimEntity(
            title = title,
            description = description,
            claimer = "Wahyu",
            date = "22 Maret 2022",
            downvote = 23,
            upvote = 25,
            voteCount = 2,
            image = 0,
            id = null,
            fake = if (fake) 1 else 0
        )

        viewModel.addClaim(itemClaimEntity)

        // Add intent to show toast in HomeActivity
        Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(this)
        }
        finish()
    }

    override fun onClick(v: View) {
        if (v.id == R.id.fact_claim) {
            fake = false
            with(binding) {
                select.animate().x(0F).duration = 100
                select.setBackgroundResource(R.drawable.fact_claim)
                factClaim.setTextColor(Color.WHITE)
                fakeClaim.setTextColor(ContextCompat.getColor(this@AddClaimActivity, R.color.gray_dark))
            }
        } else if (v.id == R.id.fake_claim) {
            fake = true
            with(binding) {
                val size = fakeClaim.width.toFloat()
                select.animate().x(size).duration = 100
                select.setBackgroundResource(R.drawable.fake_claim)
                factClaim.setTextColor(ContextCompat.getColor(this@AddClaimActivity, R.color.gray_dark))
                fakeClaim.setTextColor(Color.WHITE)
            }
        }
    }
}