package com.truesightid.ui.login

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.truesightid.R
import com.truesightid.data.source.local.entity.UserEntity
import com.truesightid.data.source.remote.StatusResponse
import com.truesightid.data.source.remote.request.LoginRequest
import com.truesightid.data.source.remote.response.Data
import com.truesightid.databinding.ActivityLoginBinding
import com.truesightid.ui.ViewModelFactory
import com.truesightid.ui.forgotpassword.ForgotPasswordActivity
import com.truesightid.ui.main.MainActivity
import com.truesightid.ui.signup.SignupActivity
import com.truesightid.utils.Prefs
import com.truesightid.utils.StringSeparatorUtils
import com.truesightid.utils.extension.*
import com.truesightid.utils.translateServerRespond

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]


        binding.btnLogin.setOnClickListener {
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
        val request = LoginRequest(
            binding.edtEmail.editText?.text.toString(),
            binding.edtPassword.editText?.text.toString()
        )
        showLoading()
        viewModel.login(request).observe(this) { user ->
            when (user.status) {
                StatusResponse.SUCCESS -> {
                    alertDialog.dismiss()
                    Prefs.isLogin = true
                    val response = user.body
                    val responseData =
                        Gson().fromJson<Data>((response.data as LinkedTreeMap<*, *>).toJson())
                    val userData = responseData.user
                    if (userData != null) {
                        Prefs.setUser(
                            UserEntity(
                                userData.id ?: -1,
                                responseData.apiKey.toString(),
                                userData.username.toString(),
                                userData.fullName.toString(),
                                userData.avatar.toString(),
                                userData.email.toString(),
                                userData.password.toString(),
                                StringSeparatorUtils.separateBookmarkResponse(userData.bookmarks),
                                StringSeparatorUtils.separateVoteResponse(userData.votes)
                            )
                        )
                    }
                    pushActivity(MainActivity::class.java)
                }
                StatusResponse.ERROR -> {
                    alertDialog.dismiss()
                    toastError(translateServerRespond(user.message.toString(), baseContext))
                }
                StatusResponse.EMPTY -> {
                    alertDialog.dismiss()
                    toastInfo("Empty Response")
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