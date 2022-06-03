package com.truesightid.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.inyongtisto.myhelper.extension.*
import com.truesightid.data.source.remote.request.LoginRequest
import com.truesightid.databinding.ActivityLoginBinding
import com.truesightid.ui.ViewModelFactory
import com.truesightid.ui.activity.ForgotPasswordActivity
import com.truesightid.ui.activity.MainActivity
import com.truesightid.ui.activity.SignupActivity
import com.truesightid.utils.Status

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        binding.btnLogin.setOnClickListener {
            Toast.makeText(this@LoginActivity, "On Developed", Toast.LENGTH_SHORT).show()
            login()
        }

        binding.tvForgotPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        binding.register.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

    }

    private fun login() {
        val request =
            LoginRequest(binding.tvEmail.text.toString(), binding.tvPassword.text.toString())

        viewModel.login(request).observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismisLoading()
//                    showToast("Selamat datang " + it.data?.username)
                    showToast("Selamat datang Admin")
                    pushActivity(MainActivity::class.java)
                }
                Status.ERROR -> {
                    dismisLoading()
                    toastError(it.message ?: "Error")
                }
                Status.LOADING -> {
                    showLoading()
                }
            }
        }
    }

}