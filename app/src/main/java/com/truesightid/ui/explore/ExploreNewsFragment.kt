package com.truesightid.ui.explore

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.inyongtisto.myhelper.extension.toastInfo
import com.truesightid.data.source.local.entity.ClaimEntity
import com.truesightid.data.source.remote.request.ClaimRequest
import com.truesightid.databinding.FragmentExploreBinding
import com.truesightid.ui.ViewModelFactory
import com.truesightid.ui.adapter.ExploreAdapter
import com.truesightid.ui.add_claim.AddClaimActivity
import com.truesightid.utils.Prefs
import com.truesightid.utils.Resource
import com.truesightid.utils.Status

class ExploreNewsFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: ExploreNewsViewModel
    private lateinit var exploreAdapter: ExploreAdapter
    private lateinit var requestAllClaims: ClaimRequest

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
            if (Prefs.isLogin) {
                requestAllClaims = ClaimRequest(Prefs.getUser()?.apiKey as String, "")
                val factory = ViewModelFactory.getInstance(
                    requireContext()
                )
                viewModel = ViewModelProvider(this, factory)[ExploreNewsViewModel::class.java]

                exploreAdapter = ExploreAdapter(object : ExploreAdapter.ItemClaimClickListener {
                    override fun onClaimUpvote(claim_id: Int) {
                        viewModel.upvoteClaimById(Prefs.getUser()?.apiKey as String, claim_id)
                    }

                    override fun onClaimDownvote(claim_id: Int) {
                        viewModel.downvoteClaimById(Prefs.getUser()?.apiKey as String, claim_id)
                    }

                }, Prefs)

                with(binding.rvClaimer) {
                    layoutManager = LinearLayoutManager(context)
                    adapter = exploreAdapter
                    setHasFixedSize(true)
                }

                binding.refreshLayout.setOnRefreshListener {
                    viewModel.getClaims(requestAllClaims).observe(viewLifecycleOwner, claimObserver)
                    Toast.makeText(context, "Refreshing", Toast.LENGTH_LONG).show()
                    binding.refreshLayout.isRefreshing = false
                }

                viewModel.getClaims(requestAllClaims)
                    .observe(viewLifecycleOwner, claimObserver)
            }

            binding.fab.setOnClickListener {
                val intent = Intent(activity, AddClaimActivity::class.java)
                startActivity(intent)
            }

            initSearch()

        }


    }

    private fun initSearch() {
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    val request = ClaimRequest(Prefs.getUser()?.apiKey as String, query)
                    viewModel.getClaims(request).observe(viewLifecycleOwner, claimObserver)
                    toastInfo("Submit $request")
                } else {
                    toastInfo("You can find claims via Search Bar")
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private val claimObserver = Observer<Resource<PagedList<ClaimEntity>>> { claims ->
        if (claims != null) {
            when (claims.status) {
                Status.LOADING -> showLoading(true)
                Status.SUCCESS -> {
                    showLoading(false)
                    exploreAdapter.submitList(claims.data)
                    exploreAdapter.notifyDataSetChanged()
                    Toast.makeText(context, "Claims populated", Toast.LENGTH_SHORT).show()
                }
                Status.ERROR -> {
                    showLoading(false)
                    Toast.makeText(context, "Error: Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) = if (isLoading) {
        binding.progressBar.visibility = View.VISIBLE
    } else {
        binding.progressBar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}