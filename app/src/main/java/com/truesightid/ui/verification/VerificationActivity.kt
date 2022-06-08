package com.truesightid.ui.verification

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.truesightid.databinding.ActivityVerificationBinding
import com.truesightid.ui.resetpassword.ResetPasswordActivity

class VerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVerify.setOnClickListener {
            Toast.makeText(this, "on developed [must connect to such as API or something like that", Toast.LENGTH_LONG).show()
            val intent = Intent(this@VerificationActivity, ResetPasswordActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }
}