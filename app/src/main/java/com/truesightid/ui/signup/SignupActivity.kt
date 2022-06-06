package com.truesightid.ui.signup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.truesightid.data.source.remote.ApiResponse
import com.truesightid.data.source.remote.StatusResponse
import com.truesightid.data.source.remote.request.RegistrationRequest
import com.truesightid.data.source.remote.response.RegistrationResponse
import com.truesightid.databinding.ActivitySignupBinding
import com.truesightid.ui.ViewModelFactory
import com.truesightid.ui.login.LoginActivity
import com.truesightid.utils.extension.*

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var request: RegistrationRequest

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

            request = RegistrationRequest(
                binding.edtUsername.editText?.text.toString(),
                binding.edtUsername.editText?.text.toString(),
                binding.edtEmail.editText?.text.toString(),
                binding.edtPassword.editText?.text.toString()

            )
            viewModel.signUpRequest(request).observe(this, signupObserver)
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
                        dismisLoading()
                        toastError(register.message.toString())
                    }
                    StatusResponse.SUCCESS -> {
                        dismisLoading()
                        showSuccessDialog(
                            "Hi ${request.username} your registration is successful, it's time to login"
                        ) { pushActivity(LoginActivity::class.java) }
                    }
                    else -> {}
                }
            }
        }
    }
}