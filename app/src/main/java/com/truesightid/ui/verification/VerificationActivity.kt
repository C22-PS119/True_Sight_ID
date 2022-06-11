package com.truesightid.ui.verification

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.truesightid.R
import com.truesightid.data.source.remote.StatusResponse
import com.truesightid.data.source.remote.request.ConfirmEmailVerificationRequest
import com.truesightid.data.source.remote.request.SendEmailVerificationRequest
import com.truesightid.databinding.ActivityVerificationBinding
import com.truesightid.ui.ViewModelFactory
import com.truesightid.ui.forgotpassword.ForgotPasswordActivity
import com.truesightid.ui.resetpassword.ResetPasswordActivity
import com.truesightid.utils.extension.toastError
import com.truesightid.utils.extension.toastInfo
import com.truesightid.utils.extension.toastWarning
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerificationBinding
    private lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[VerificationViewModel::class.java]

        var userId = -1
        var email: String? = null

        val extras = intent.extras
        if(extras != null){
            userId = extras.getInt("user_id")
            email = extras.getString("email")
            if ((userId == -1) or (email == null))
                toastError(resources.getString(R.string.something_wrong_while_getting_user_info))
        }

        binding.tvEmail.text = email ?: "*****@***"

        binding.ibBackLogin.setOnClickListener {
            val intent = Intent(this@VerificationActivity, ForgotPasswordActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        binding.edtOne.setOnKeyListener { v, keyCode, event ->
            if ((keyCode in 7..16) and (event.action == KeyEvent.ACTION_UP)) {
                binding.edtOne.setText(event.number.toString())
                binding.edtTwo.requestFocus()
            }
            false
        }
        binding.edtTwo.setOnKeyListener { v, keyCode, event ->
            if ((keyCode in 7..16) and (event.action == KeyEvent.ACTION_UP)) {
                binding.edtTwo.setText(event.number.toString())
                binding.edtThree.requestFocus()
            }
            false
        }
        binding.edtThree.setOnKeyListener { v, keyCode, event ->
            if ((keyCode in 7..16) and (event.action == KeyEvent.ACTION_UP)) {
                binding.edtThree.setText(event.number.toString())
                binding.edtFour.requestFocus()
            }
            false
        }
        binding.edtFour.setOnKeyListener { v, keyCode, event ->
            if ((keyCode in 7..16) and (event.action == KeyEvent.ACTION_UP)) {
                binding.edtFour.setText(event.number.toString())
            }
            false
        }

        binding.tvResend.setOnClickListener {
            sendCountdown()
            val userProfile = SendEmailVerificationRequest(
                email = binding.tvEmail.text.toString(),
            )
            showLoading()
            viewModel.sendEmailVerification(userProfile).observe(this) { response ->
                when (response.status) {
                    StatusResponse.SUCCESS -> {
                        toastInfo(resources.getString(R.string.verification_has_been_sent))
                        alertDialog.dismiss()
                    }
                    StatusResponse.EMPTY -> {
                        toastWarning("Empty: ${response.body}")
                        alertDialog.dismiss()
                    }
                    StatusResponse.ERROR -> {
                        toastError("Error: ${response.message}")
                        alertDialog.dismiss()
                    }
                }
            }

        }

        binding.btnVerify.setOnClickListener {
            val confirmRequest = ConfirmEmailVerificationRequest(
                user_id = userId,
                verification_code = binding.edtOne.text.toString() + binding.edtTwo.text.toString() + binding.edtThree.text.toString() + binding.edtFour.text.toString()
            )

            showLoading()
            viewModel.confirmVerificationCode(confirmRequest).observe(this) { response ->
                when (response.status) {
                    StatusResponse.SUCCESS -> {
                        val intent = Intent(this@VerificationActivity, ResetPasswordActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        intent.putExtra("reset_key", response.body.data)
                        startActivity(intent)
                    }
                    StatusResponse.EMPTY -> {
                        toastWarning("Empty: ${response.body}")
                        alertDialog.dismiss()
                    }
                    StatusResponse.ERROR -> {
                        toastError("Error: ${response.message}")
                        alertDialog.dismiss()
                    }
                }
            }
        }
        sendCountdown()
    }

    private fun sendCountdown() {
        binding.tvResend.isEnabled = false
        GlobalScope.launch(Dispatchers.Main) {
            var s = 30
            while (s > 0){
                val text = "Resend (${s}s)"
                binding.tvResend.text = text
                delay(1000)
                s--
            }
        }.invokeOnCompletion {
            val text = "Resend"
            binding.tvResend.text = text
            binding.tvResend.isEnabled = true
        }
    }

    private fun showLoading() {
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.view_loading, null)
        alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setView(layout)
        alertDialog.setCancelable(false)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }
}