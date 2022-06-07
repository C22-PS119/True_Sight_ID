package com.truesightid.ui.editprofile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.truesightid.R
import com.truesightid.data.source.local.entity.UserEntity
import com.truesightid.data.source.remote.StatusResponse
import com.truesightid.data.source.remote.request.EditProfileRequest
import com.truesightid.data.source.remote.request.EditProfileWithAvatarRequest
import com.truesightid.data.source.remote.request.GetProfileRequest
import com.truesightid.databinding.ActivityEditProfileBinding
import com.truesightid.ui.ViewModelFactory
import com.truesightid.ui.main.MainActivity
import com.truesightid.utils.Prefs
import com.truesightid.utils.VotesSeparator
import com.truesightid.utils.extension.toastError
import com.truesightid.utils.extension.toastInfo
import com.truesightid.utils.extension.toastWarning
import com.truesightid.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        if (Prefs.user != null) {
            binding.tvName.setText(Prefs.getUser()?.fullname)
            binding.tvEmail.setText(Prefs.getUser()?.email)
            Thread(Runnable {
                Glide.get(binding.root.context).clearDiskCache()
            }).start()
            Glide.get(binding.root.context).clearMemory()
            Glide.with(binding.root.context)
                .load(Prefs.getUser()?.avatar)
                .timeout(5000)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error)
                )
                .into(binding.ivProfile)
        }


        binding.btnSaveProfile.setOnClickListener {
            ChangeProfile(viewModel)
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
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
            titleNewPassword.visibility = View.GONE
            newPasswordForm.visibility = View.GONE
            tvNewPassword.visibility = View.GONE
            titleReTypeNewPassword.visibility = View.GONE
            reTypePasswordForm.visibility = View.GONE
            tvReTypePassword.visibility = View.GONE
        }
    }

    fun ChangeProfile(viewModel: SetProfileViewModel) {
        if (avatar != null) {
            val userProfile = EditProfileWithAvatarRequest(
                apiKey = Prefs.getUser()?.apiKey as String,
                full_name = binding.tvName.text.toString().toRequestBody(),
                email = binding.tvEmail.text.toString().toRequestBody(),
                avatar = avatar
            )

            viewModel.setProfileWithAvatar(userProfile).observe(this) { response ->
                when (response.status) {
                    StatusResponse.SUCCESS -> toastInfo("Success: ${response.body}")
                    StatusResponse.EMPTY -> toastWarning("Empty: ${response.body}")
                    StatusResponse.ERROR -> toastError("Error: ${response.body}")
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
                    StatusResponse.SUCCESS -> toastInfo("Success: ${response.body}")
                    StatusResponse.EMPTY -> toastWarning("Empty: ${response.body}")
                    StatusResponse.ERROR -> toastError("Error: ${response.body}")
                }
            }
        }

        val getUserProfile = GetProfileRequest(
            apiKey = Prefs.getUser()?.apiKey as String,
            id = Prefs.getUser()?.id ?: -1
        )
        viewModel.getUserProfile(getUserProfile).observe(this) { response ->
            when (response.status) {
                StatusResponse.SUCCESS -> {
                    val userData = response.body.data
                    if (userData != null) {
                        Prefs.setUser(
                            UserEntity(
                                userData.id ?: -1,
                                Prefs.getUser()?.apiKey as String,
                                userData.username.toString(),
                                userData.fullName.toString(),
                                userData.avatar.toString(),
                                userData.email.toString(),
                                userData.password.toString(),
                                VotesSeparator.separate(userData.votes)
                            )
                        )
                    }
                }
                StatusResponse.EMPTY -> toastWarning("Empty: ${response.body}")
                StatusResponse.ERROR -> toastError("Error: ${response.body}")
            }
        }
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
                RequestBody.create("image/*".toMediaTypeOrNull(), file)
            )
            avatar = filePart
        }
    }
}