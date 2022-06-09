package com.truesightid.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.truesightid.R
import com.truesightid.data.source.local.entity.UserEntity
import com.truesightid.data.source.remote.StatusResponse
import com.truesightid.data.source.remote.request.GetProfileRequest
import com.truesightid.databinding.FragmentProfileBinding
import com.truesightid.ui.ViewModelFactory
import com.truesightid.ui.editprofile.EditProfileActivity
import com.truesightid.ui.login.LoginActivity
import com.truesightid.ui.mybookmark.MyBookmarkActivity
import com.truesightid.ui.myclaim.MyClaimActivity
import com.truesightid.utils.Prefs
import com.truesightid.utils.StringSeparatorUtils
import com.truesightid.utils.extension.pushActivity
import com.truesightid.utils.extension.toastError
import com.truesightid.utils.extension.toastWarning
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]

        binding.btnEditProfile.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.rlMyClaims.setOnClickListener {
            val i = Intent(context, MyClaimActivity::class.java)
            startActivity(i)
        }

        binding.rlLogout.setOnClickListener {
            pushActivity(LoginActivity::class.java)
            Prefs.isLogin = false
            Prefs.setUser(null)
            Prefs.isLogin = false
        }

        binding.rlBookmark.setOnClickListener {
            val i = Intent(context, MyBookmarkActivity::class.java)
            startActivity(i)
        }

        val getUserProfile = GetProfileRequest(
            apiKey = Prefs.getUser()?.apiKey as String,
            id = Prefs.getUser()?.id ?: -1
        )

        viewModel.getUserProfile(getUserProfile).observe(viewLifecycleOwner) { response ->
            when (response.status) {
                StatusResponse.SUCCESS -> {
                    val userData = response.body.data
                    if (userData != null) {
                        binding.tvName.text = userData.username
                        binding.tvEmail.text = userData.email
                        Glide.get(binding.root.context).clearMemory()
                        GlobalScope.launch(Dispatchers.IO) {
                            Glide.get(binding.root.context).clearDiskCache()
                        }.invokeOnCompletion {
                            GlobalScope.launch(Dispatchers.Main) {
                                Glide.with(requireContext())
                                    .load(userData.avatar.toString())
                                    .centerInside()
                                    .timeout(3000)
                                    .apply(
                                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                                            .error(R.drawable.ic_error)
                                    )
                                    .into(binding.ivProfile)
                            }
                        }
                        Prefs.setUser(
                            UserEntity(
                                userData.id ?: -1,
                                Prefs.getUser()?.apiKey as String,
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
                }
                StatusResponse.EMPTY -> toastWarning("Empty: ${response.body}")
                StatusResponse.ERROR -> toastError("Error: ${response.body}")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}