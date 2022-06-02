package com.truesightid.ui.explore

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.truesightid.databinding.FragmentExploreBinding
import com.truesightid.ui.ViewModelFactory
import com.truesightid.ui.addclaim.AddClaimActivity
import com.truesightid.ui.adapter.ExploreAdapter

class ExploreNewsFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: ExploreNewsViewModel
    private lateinit var exploreAdapter: ExploreAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {

            val factory = ViewModelFactory.getInstance(requireContext())
            viewModel = ViewModelProvider(this, factory)[ExploreNewsViewModel::class.java]

            exploreAdapter = ExploreAdapter()

            viewModel.getClaims().observe(viewLifecycleOwner) { claims ->
                exploreAdapter.setClaims(claims)
                exploreAdapter.notifyDataSetChanged()
            }

            with(binding.rvClaimer) {
                layoutManager = LinearLayoutManager(context)
                adapter = exploreAdapter
                setHasFixedSize(true)
            }

        }

        binding.fab.setOnClickListener {
            val intent = Intent(activity, AddClaimActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}