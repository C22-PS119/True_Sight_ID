package com.truesightid.ui.resetpassword

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.truesightid.R
import com.truesightid.data.source.remote.StatusResponse
import com.truesightid.data.source.remote.request.ResetPasswordRequest
import com.truesightid.data.source.remote.request.SetPasswordRequest
import com.truesightid.databinding.ActivityResetPasswordBinding
import com.truesightid.ui.ViewModelFactory
import com.truesightid.ui.login.LoginActivity
import com.truesightid.utils.Prefs
import com.truesightid.utils.extension.*

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[ResetPasswordViewModel::class.java]

        var reset_key: String? = null
        val extras = intent.extras
        if(extras != null){
            reset_key = extras.getString("reset_key")
            if (reset_key == null)
                toastError(resources.getString(R.string.something_wrong_while_getting_user_info))
        }

        binding.ibBackLogin.setOnClickListener {
            val intent = Intent(this@ResetPasswordActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        binding.btnSubmit.setOnClickListener {

            if (binding.tvNewPassword.text.isNullOrBlank() or binding.tvReTypePassword.text.isNullOrBlank()) {
                toastError(resources.getString(R.string.please_fill_all_blank))
                return@setOnClickListener
            }else if (binding.tvNewPassword.text.toString() != binding.tvReTypePassword.text.toString()) {
                toastError(resources.getString(R.string.password_not_match))
                return@setOnClickListener
            }

            val userPassword = ResetPasswordRequest(
                new_password = binding.tvNewPassword.text.toString(),
                reset_key = reset_key.toString()
            )
            showLoading()
            viewModel.resetPassword(userPassword).observe(this) { response ->
                when (response.status) {
                    StatusResponse.SUCCESS -> {
                        toastInfo(resources.getString(R.string.password_changed))
                        dismisLoading()
                        showSuccessDialog {
                            pushActivity(LoginActivity::class.java)
                        }
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

    private fun showSuccessDialog(onConfirmClickListener: () -> Unit) {
        val dialog = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
        dialog.titleText = resources.getString(R.string.change_password_success)
        dialog.contentText = resources.getString(R.string.password_has_been_updated)
        dialog.confirmText = getString(R.string.dialog_ok)
        dialog.setConfirmClickListener {
            it.dismiss()
            onConfirmClickListener()
        }
        dialog.setCancelable(false)
        dialog.show()
    }
}