package com.truesightid.ui.editprofile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.truesightid.R
import com.truesightid.data.source.remote.StatusResponse
import com.truesightid.data.source.remote.request.EditProfileRequest
import com.truesightid.data.source.remote.request.EditProfileWithAvatarRequest
import com.truesightid.data.source.remote.request.SetPasswordRequest
import com.truesightid.databinding.ActivityEditProfileBinding
import com.truesightid.ui.ViewModelFactory
import com.truesightid.ui.main.MainActivity
import com.truesightid.utils.Prefs
import com.truesightid.utils.extension.*
import com.truesightid.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private var avatar: MultipartBody.Part? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[SetProfileViewModel::class.java]

        // Setup back button
        binding.btnCancel.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("shouldProfile", true)
            startActivity(intent)
        }

        binding.tvName.setText(Prefs.getUser()?.fullname)
        binding.tvEmail.setText(Prefs.getUser()?.email)
        Glide.with(binding.root.context)
            .load(Prefs.getUser()?.avatar)
            .centerInside()
            .apply(
                RequestOptions.placeholderOf(R.drawable.ic_loading)
                    .error(R.drawable.ic_error)
            )
            .into(binding.ivProfile)

        binding.btnSaveProfile.setOnClickListener {
            changeProfile(viewModel)
        }

        binding.btnChoose.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            val chooser = Intent.createChooser(intent, "Choose a Picture")
            launcherIntentGallery.launch(chooser)
        }

        binding.tvChangePassword.setOnClickListener {
            changePasswordPressed()
        }

        binding.tvCancelChanges.setOnClickListener {
            cancelChangesPressed()
        }
    }

    fun changePasswordPressed() {
        with(binding) {
            tvChangePassword.visibility = View.INVISIBLE
            tvCancelChanges.visibility = View.VISIBLE
            titleCurrentPassword.setText(R.string.current_password)
            tvCurrentPassword.isEnabled = true
            tvCurrentPassword.setText(R.string.empty)
            titleNewPassword.visibility = View.VISIBLE
            newPasswordForm.visibility = View.VISIBLE
            tvNewPassword.visibility = View.VISIBLE
            tvNewPassword.setText(R.string.empty)
            titleReTypeNewPassword.visibility = View.VISIBLE
            reTypePasswordForm.visibility = View.VISIBLE
            tvReTypePassword.visibility = View.VISIBLE
            tvReTypePassword.setText(R.string.empty)
        }
    }

    fun cancelChangesPressed() {
        with(binding) {
            tvChangePassword.visibility = View.VISIBLE
            tvCancelChanges.visibility = View.INVISIBLE
            titleCurrentPassword.setText(R.string.password)
            tvCurrentPassword.isEnabled = false
            tvCurrentPassword.setText("********")
            titleNewPassword.visibility = View.GONE
            newPasswordForm.visibility = View.GONE
            tvNewPassword.visibility = View.GONE
            titleReTypeNewPassword.visibility = View.GONE
            reTypePasswordForm.visibility = View.GONE
            tvReTypePassword.visibility = View.GONE
        }
    }

    private fun showSuccessDialog(onConfirmClickListener: () -> Unit) {
        val dialog = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
        dialog.titleText = "Edit Profile Success"
        dialog.contentText =
            "Your profile has been updated, go back to profile page to see the changes"
        dialog.confirmText = getString(R.string.dialog_ok)
        dialog.setConfirmClickListener {
            it.dismiss()
            onConfirmClickListener()
        }
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun changeProfile(viewModel: SetProfileViewModel) {
        try{
            showLoading()
        }catch (ex: Exception){}
        if (avatar != null) {
            val userProfile = EditProfileWithAvatarRequest(
                apiKey = Prefs.getUser()?.apiKey as String,
                full_name = binding.tvName.text.toString().toRequestBody(),
                email = binding.tvEmail.text.toString().toRequestBody(),
                avatar = avatar
            )

            viewModel.setProfileWithAvatar(userProfile).observe(this) { response ->
                when (response.status) {
                    StatusResponse.SUCCESS -> {
                        if (binding.tvCurrentPassword.isEnabled){
                            changePassword(viewModel)
                        }else{
                            toastInfo("Success: ${response.body}")
                            dismisLoading()
                            showSuccessDialog() {
                                backToMainActivity()
                            }
                        }
                    }
                    StatusResponse.EMPTY -> {
                        toastWarning("Empty: ${response.body}")
                        dismisLoading()
                    }
                    StatusResponse.ERROR -> {
                        toastError("Error: ${response.body}")
                        dismisLoading()
                    }
                }
            }
        } else {
            val userProfile = EditProfileRequest(
                apiKey = Prefs.getUser()?.apiKey as String,
                full_name = binding.tvName.text.toString().toRequestBody(),
                email = binding.tvEmail.text.toString().toRequestBody()
            )

            viewModel.setProfile(userProfile).observe(this) { response ->
                when (response.status) {
                    StatusResponse.SUCCESS -> {
                        if (binding.tvCurrentPassword.isEnabled){
                            changePassword(viewModel)
                        }else{
                            dismisLoading()
                            showSuccessDialog() {
                                backToMainActivity()
                            }
                        }
                    }
                    StatusResponse.EMPTY -> {
                        toastWarning("Empty: ${response.body}")
                        dismisLoading()
                    }
                    StatusResponse.ERROR -> {
                        toastError("Error: ${response.body}")
                        dismisLoading()
                    }
                }
            }
        }
    }

    private fun changePassword(viewModel: SetProfileViewModel){
        val userPassword = SetPasswordRequest(
            apiKey = Prefs.getUser()?.apiKey as String,
            new_password = binding.tvNewPassword.text.toString(),
            current_password = binding.tvCurrentPassword.text.toString()
        )

        if (binding.tvNewPassword.text.toString().isNullOrEmpty() or binding.tvReTypePassword.toString().isNullOrEmpty() or binding.tvCurrentPassword.toString().isNullOrEmpty()){
            toastError("Please fill all blanks")
            dismisLoading()
        }else if (binding.tvNewPassword.text.toString() == binding.tvReTypePassword.text.toString()){
            viewModel.setPassword(userPassword).observe(this) { response ->
                when (response.status) {
                    StatusResponse.SUCCESS -> {
                        dismisLoading()
                        showSuccessDialog() {
                            backToMainActivity()
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
        }else{
            toastError("Error: Password not match")
            dismisLoading()
        }
    }

    private fun backToMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.putExtra("shouldProfile", true)
        startActivity(intent)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val file = uriToFile(selectedImg, this)
            Glide.with(binding.root.context)
                .load(selectedImg)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error)
                )
                .into(binding.ivProfile)
            val filePart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "avatar",
                file.getName(),
                file.asRequestBody("image/*".toMediaTypeOrNull())
            )
            avatar = filePart
        }
    }
}