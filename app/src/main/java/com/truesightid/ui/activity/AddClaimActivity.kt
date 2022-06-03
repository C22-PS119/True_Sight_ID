package com.truesightid.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.truesightid.R
import com.truesightid.databinding.ActivityAddClaimBinding
import com.truesightid.ui.main.MainActivity

class AddClaimActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddClaimBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }

    override fun onClick(v: View) {
        if (v.id == R.id.fact_claim) {
            with(binding) {
                select.animate().x(0F).duration = 100
                select.setBackgroundResource(R.drawable.fact_claim)
                factClaim.setTextColor(Color.WHITE)
                fakeClaim.setTextColor(ContextCompat.getColor(this@AddClaimActivity, R.color.gray_dark))
            }
        } else if (v.id == R.id.fake_claim) {
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