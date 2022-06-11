package com.truesightid.ui.explore

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import com.truesightid.utils.*
import com.truesightid.utils.InputPreprocessUtils.searchQueryFilter
import com.truesightid.utils.extension.toastInfo
import com.truesightid.utils.extension.toastSuccess
import java.sql.Timestamp
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ExploreNewsFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: ExploreNewsViewModel
    private lateinit var exploreAdapter: ExploreAdapter
    private lateinit var requestAllClaims: ClaimRequest
    private lateinit var alertDialog: AlertDialog
    private lateinit var filterDialog: AlertDialog

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
            binding.ibFilter.setOnClickListener {
                val request = ClaimRequest(Prefs.getUser()?.apiKey as String, searchQueryFilter(binding.searchBar.query.toString()))
                showLoading()
                showFilter { sortBy, type, dateOpt, dateStart, dateEnd ->
                    viewModel.getClaimsWithFilter(request, FilterSearch(sortBy, type, dateOpt, dateStart, dateEnd))
                        .observe(viewLifecycleOwner, claimObserver)
                    toastInfo("Result of ${request.keyword}")
                }
                alertDialog.dismiss()
            }

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
                        toastInfo(resources.getString(R.string.added_to_bookmark))
                    }

                    override fun onBookmarkRemoved(claim_id: Int) {
                        viewModel.removeBookmarkById(
                            AddRemoveBookmarkRequest(
                                Prefs.getUser()?.apiKey as String,
                                claim_id
                            )
                        )
                        UserAction.applyUserBookmarks(claim_id, false)
                        toastInfo(resources.getString(R.string.removed_from_bookmark))
                    }

                }, Prefs)

                with(binding.rvClaimer) {
                    layoutManager = LinearLayoutManager(context)
                    adapter = exploreAdapter
                    setHasFixedSize(true)
                }

                binding.refreshLayout.setOnRefreshListener {
                    viewModel.getClaims(requestAllClaims).observe(viewLifecycleOwner, claimObserver)
                    toastSuccess(resources.getString(R.string.page_refreshed))
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
                    toastInfo(resources.getString(R.string.result_of, request.keyword))
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
                    Toast.makeText(context, resources.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT)
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

    private fun showFilter(onApply: (sortBy: Int, type: Int, dateOpt: Int, dateStart: Long?, dateEnd: Long?) -> Unit) {
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.view_filter, null)
        val rbAnytime = layout.findViewById<RadioButton>(R.id.rb_anytime)
        val rbSpecific = layout.findViewById<RadioButton>(R.id.rb_specific_date)
        val datefrom = layout.findViewById<EditText>(R.id.date_from)
        val dateto = layout.findViewById<EditText>(R.id.date_to)
        val ddSortBy = layout.findViewById<Spinner>(R.id.dd_sort_by)
        val ddType = layout.findViewById<Spinner>(R.id.dd_type)
        var dateStart :Long? = null
        var dateEnd: Long? = null
        datefrom.isEnabled = false
        dateto.isEnabled = false


        layout.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            filterDialog.dismiss()
        }
        layout.findViewById<Button>(R.id.btn_apply).setOnClickListener {
            onApply(ddSortBy.selectedItemPosition,ddType.selectedItemPosition,if (rbAnytime.isChecked) 0 else 1,dateStart,dateEnd)
            filterDialog.dismiss()
        }
        rbAnytime.setOnClickListener {
            if (rbAnytime.isChecked) {
                datefrom.isEnabled = false
                dateto.isEnabled = false
            }
        }
        rbSpecific.setOnClickListener {
            if (rbSpecific.isChecked) {
                datefrom.isEnabled = true
                dateto.isEnabled = true
            }
        }
        datefrom.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this.context as Context)
            datePickerDialog.setOnDateSetListener { datePicker, _, _, _ ->
                val dateFrom = LocalDate.of(datePicker.year, datePicker.month + 1, datePicker.dayOfMonth)
                val formatters: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                dateStart = Timestamp.from(dateFrom.atStartOfDay(ZoneId.systemDefault()).toInstant()).time
                datefrom.setText(dateFrom.format(formatters))
            }
            datePickerDialog.show()
        }
        dateto.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this.context as Context)
            datePickerDialog.setOnDateSetListener { datePicker, _, _, _ ->
                val dateTo = LocalDate.of(datePicker.year, datePicker.month + 1, datePicker.dayOfMonth)
                val formatters: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                dateEnd = Timestamp.from(dateTo.atStartOfDay(ZoneId.systemDefault()).toInstant()).time
                dateto.setText(dateTo.format(formatters))
            }
            datePickerDialog.show()
        }
        filterDialog = AlertDialog.Builder(requireContext()).create()
        filterDialog.setView(layout)
        filterDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        filterDialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}