package com.truesightid.ui.resetpassword

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.truesightid.R
import com.truesightid.data.source.remote.StatusResponse
import com.truesightid.data.source.remote.request.ResetPasswordRequest
import com.truesightid.databinding.ActivityResetPasswordBinding
import com.truesightid.ui.ViewModelFactory
import com.truesightid.ui.login.LoginActivity
import com.truesightid.utils.extension.pushActivity
import com.truesightid.utils.extension.toastError
import com.truesightid.utils.extension.toastInfo
import com.truesightid.utils.extension.toastWarning
import com.truesightid.utils.translateServerRespond

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordBinding
    private lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[ResetPasswordViewModel::class.java]

        var resetKey: String? = null
        val extras = intent.extras
        if(extras != null){
            resetKey = extras.getString("reset_key")
            if (resetKey == null)
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
                reset_key = resetKey.toString()
            )
            showLoading()
            viewModel.resetPassword(userPassword).observe(this) { response ->
                when (response.status) {
                    StatusResponse.SUCCESS -> {
                        toastInfo(resources.getString(R.string.password_changed))
                        alertDialog.dismiss()
                        showSuccessDialog {
                            pushActivity(LoginActivity::class.java)
                        }
                    }
                    StatusResponse.EMPTY -> {
                        toastWarning("Empty: ${response.body}")
                        alertDialog.dismiss()
                    }
                    StatusResponse.ERROR -> {
                        toastError(translateServerRespond(response.message.toString(), baseContext))
                        alertDialog.dismiss()
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