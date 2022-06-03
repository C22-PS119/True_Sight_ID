package com.truesightid.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.truesightid.databinding.ActivityResetPasswordBinding
import com.truesightid.ui.login.LoginActivity

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ibBackLogin.setOnClickListener {
            val intent = Intent(this@ResetPasswordActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        binding.btnSubmit.setOnClickListener {
            Toast.makeText(this, "on developed", Toast.LENGTH_SHORT).show()
        }
    }
}