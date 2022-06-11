package com.truesightid.ui.signup

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.truesightid.R
import com.truesightid.data.source.remote.ApiResponse
import com.truesightid.data.source.remote.StatusResponse
import com.truesightid.data.source.remote.request.RegistrationRequest
import com.truesightid.data.source.remote.response.RegistrationResponse
import com.truesightid.databinding.ActivitySignupBinding
import com.truesightid.ui.ViewModelFactory
import com.truesightid.ui.login.LoginActivity
import com.truesightid.utils.extension.pushActivity
import com.truesightid.utils.extension.showSuccessDialog
import com.truesightid.utils.extension.toastError
import com.truesightid.utils.translateServerRespond

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var request: RegistrationRequest
    private lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[SignupViewModel::class.java]
      
        binding.ibBackLogin.setOnClickListener {
            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        binding.btnSignup.setOnClickListener {

            if (binding.edtPassword.editText?.text.toString() == binding.edtPasswordConfirmation.editText?.text.toString()){
                request = RegistrationRequest(
                    binding.edtUsername.editText?.text.toString(),
                    binding.edtUsername.editText?.text.toString(),
                    binding.edtEmail.editText?.text.toString(),
                    binding.edtPassword.editText?.text.toString()

                )
                viewModel.signUpRequest(request).observe(this, signupObserver)
            }else{
                toastError(getString(R.string.password_not_match))
            }
        }

        binding.login.setOnClickListener {
            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }
    private val signupObserver = Observer<ApiResponse<RegistrationResponse>> { register ->
        showLoading()
        run {
            if (register != null) {
                when (register.status) {
                    StatusResponse.ERROR -> {
                        alertDialog.dismiss()
                        toastError(translateServerRespond(register.message.toString(), baseContext))
                    }
                    StatusResponse.SUCCESS -> {
                        alertDialog.dismiss()
                        showSuccessDialog(
                            resources.getString(R.string.registration_is_successful, request.username)
                        ) { pushActivity(LoginActivity::class.java) }
                    }
                    else -> {
                        alertDialog.dismiss()
                    }
                }
            }
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