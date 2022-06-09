package com.truesightid.ui.explore

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import androidx.recyclerview.widget.RecyclerView
import com.truesightid.R
import com.truesightid.data.source.local.entity.ClaimEntity
import com.truesightid.data.source.remote.request.AddRemoveBookmarkRequest
import com.truesightid.data.source.remote.request.ClaimRequest
import com.truesightid.databinding.FragmentExploreBinding
import com.truesightid.ui.ViewModelFactory
import com.truesightid.ui.adapter.ExploreAdapter
import com.truesightid.ui.add_claim.AddClaimActivity
import com.truesightid.utils.InputPreprocessUtils.searchQueryFilter
import com.truesightid.utils.Prefs
import com.truesightid.utils.Resource
import com.truesightid.utils.Status
import com.truesightid.utils.UserAction
import com.truesightid.utils.extension.toastInfo
import com.truesightid.utils.extension.toastSuccess

class ExploreNewsFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: ExploreNewsViewModel
    private lateinit var exploreAdapter: ExploreAdapter
    private lateinit var requestAllClaims: ClaimRequest
    private lateinit var alertDialog: AlertDialog

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

                    override fun onBookmarkAdded(claim_id: Int) {
                        viewModel.addBookmarkById(
                            AddRemoveBookmarkRequest(
                                Prefs.getUser()?.apiKey as String,
                                claim_id
                            )
                        )
                        UserAction.applyUserBookmarks(claim_id, true)
                        toastInfo("Added to Bookmark")
                    }

                    override fun onBookmarkRemoved(claim_id: Int) {
                        viewModel.removeBookmarkById(
                            AddRemoveBookmarkRequest(
                                Prefs.getUser()?.apiKey as String,
                                claim_id
                            )
                        )
                        UserAction.applyUserBookmarks(claim_id, false)
                        toastInfo("Removed from Bookmark")
                    }

                }, Prefs)

                with(binding.rvClaimer) {
                    layoutManager = LinearLayoutManager(context)
                    adapter = exploreAdapter
                    setHasFixedSize(true)
                }

                binding.refreshLayout.setOnRefreshListener {
                    viewModel.getClaims(requestAllClaims).observe(viewLifecycleOwner, claimObserver)
                    toastSuccess("Page Refreshed")
                    binding.refreshLayout.isRefreshing = false
                }

                viewModel.getClaims(requestAllClaims)
                    .observe(viewLifecycleOwner, claimObserver)
            }

            binding.fab.setOnClickListener {
                val intent = Intent(activity, AddClaimActivity::class.java)
                startActivity(intent)
            }

            binding.rvClaimer.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    // if the recycler view is scrolled
                    // above hide the FAB
                    if (dy > 10 && binding.fab.isShown) {
                        binding.fab.hide()
                    }

                    // if the recycler view is
                    // scrolled above show the FAB
                    if (dy < -10 && !binding.fab.isShown) {
                        binding.fab.show()
                    }

                    // of the recycler view is at the first
                    // item always show the FAB
                    if (!recyclerView.canScrollVertically(-1)) {
                        binding.fab.show()
                    }
                }
            })

            initSearch()

        }
    }

    private fun initSearch() {
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    val request =
                        ClaimRequest(Prefs.getUser()?.apiKey as String, searchQueryFilter(query))
                    viewModel.getClaims(request).observe(viewLifecycleOwner, claimObserver)
                    toastInfo("Result of ${request.keyword}")
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
        showLoading()
        if (claims != null) {
            when (claims.status) {
                Status.LOADING -> alertDialog.dismiss()
                Status.SUCCESS -> {
                    alertDialog.dismiss()
                    if (claims.data?.isNotEmpty() as Boolean) {
                        showIllustrator(false)
                    } else {
                        showIllustrator(true)
                    }
                    exploreAdapter.submitList(claims.data)
                    exploreAdapter.notifyDataSetChanged()
                }
                Status.ERROR -> {
                    alertDialog.dismiss()
                    Toast.makeText(context, "Error: Somethings went wrong", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun showIllustrator(isIllustrator: Boolean) {
        if (isIllustrator) {
            binding.ivIllustrator.visibility = View.VISIBLE
            binding.tvIllustrator1.visibility = View.VISIBLE
            binding.tvIllustrator2.visibility = View.VISIBLE
        } else {
            binding.ivIllustrator.visibility = View.GONE
            binding.tvIllustrator1.visibility = View.GONE
            binding.tvIllustrator2.visibility = View.GONE
        }
    }

    private fun showLoading() {
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.view_loading, null)
        alertDialog = AlertDialog.Builder(requireContext()).create()
        alertDialog.setView(layout)
        alertDialog.setCancelable(false)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}