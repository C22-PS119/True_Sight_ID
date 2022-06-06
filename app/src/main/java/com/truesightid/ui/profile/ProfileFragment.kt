package com.truesightid.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.truesightid.R
import com.truesightid.databinding.FragmentProfileBinding
import com.truesightid.ui.editprofile.EditProfileActivity
import com.truesightid.ui.login.LoginActivity
import com.truesightid.utils.Prefs

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ProfileViewModel>()

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

        binding.btnEditProfile.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.rlLogout.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        if (Prefs.user != null){
            binding.tvName.text = Prefs.getUser()?.username
            binding.tvEmail.text = Prefs.getUser()?.email
            Glide.with(view.context)
                .load(Prefs.getUser()?.avatar)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error)
                )
                .into(binding.ivProfile)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}