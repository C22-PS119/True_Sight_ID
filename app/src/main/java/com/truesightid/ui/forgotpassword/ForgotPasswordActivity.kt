package com.truesightid.ui.forgotpassword

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.truesightid.R
import com.truesightid.data.source.remote.StatusResponse
import com.truesightid.data.source.remote.request.EditProfileWithAvatarRequest
import com.truesightid.data.source.remote.request.SendEmailVerificationRequest
import com.truesightid.databinding.ActivityForgotPasswordBinding
import com.truesightid.ui.ViewModelFactory
import com.truesightid.ui.editprofile.SetProfileViewModel
import com.truesightid.ui.login.LoginActivity
import com.truesightid.ui.verification.VerificationActivity
import com.truesightid.utils.Prefs
import com.truesightid.utils.extension.*
import okhttp3.RequestBody.Companion.toRequestBody

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[ForgotPasswordViewModel::class.java]

        binding.ibBackLogin.setOnClickListener {
            val intent = Intent(this@ForgotPasswordActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        binding.btnSubmit.setOnClickListener {
            val userProfile = SendEmailVerificationRequest(
                email = binding.tvEmail.text.toString(),
            )

            showLoading()
            viewModel.sendEmailVerification(userProfile).observe(this) { response ->
                when (response.status) {
                    StatusResponse.SUCCESS -> {
                        dismisLoading()
                        toastInfo(resources.getString(R.string.verification_has_been_sent))
                        val intent = Intent(this@ForgotPasswordActivity, VerificationActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        intent.putExtra("user_id", response.body.data)
                        intent.putExtra("email", binding.tvEmail.text.toString())
                        startActivity(intent)
                    }
                    StatusResponse.EMPTY -> {
                        toastWarning("Empty: ${response.body}")
                        dismisLoading()
                    }
                    StatusResponse.ERROR -> {
                        toastError("Error: ${response.message}")
                        dismisLoading()
                    }
                }
            }
        }
    }
}