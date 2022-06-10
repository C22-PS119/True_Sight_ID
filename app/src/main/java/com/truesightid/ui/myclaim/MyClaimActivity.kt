package com.truesightid.ui.myclaim

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.truesightid.R
import com.truesightid.data.source.local.entity.ClaimEntity
import com.truesightid.data.source.remote.ApiResponse
import com.truesightid.data.source.remote.StatusResponse
import com.truesightid.data.source.remote.request.MyDataRequest
import com.truesightid.databinding.ActivityMyclaimBinding
import com.truesightid.ui.ViewModelFactory
import com.truesightid.ui.adapter.MyClaimAdapter
import com.truesightid.ui.main.MainActivity
import com.truesightid.utils.Prefs
import com.truesightid.utils.extension.toastInfo
import com.truesightid.utils.extension.toastSuccess

class MyClaimActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyclaimBinding
    private lateinit var myClaimAdapter: MyClaimAdapter
    private lateinit var alertDialog: AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyclaimBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[MyClaimViewModel::class.java]

        binding.ibBackLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("shouldProfile", true)
            startActivity(intent)
        }

        myClaimAdapter = MyClaimAdapter(object : MyClaimAdapter.ItemClaimClickListener {
            override fun onClaimUpvote(claim_id: Int) {
                viewModel.upvoteClaimById(Prefs.getUser()?.apiKey as String, claim_id)
            }

            override fun onClaimDownvote(claim_id: Int) {
                viewModel.downvoteClaimById(Prefs.getUser()?.apiKey as String, claim_id)
            }

        }, Prefs)

        with(binding.rvMyClaim) {
            layoutManager = LinearLayoutManager(context)
            adapter = myClaimAdapter
            setHasFixedSize(true)
        }

        binding.refreshLayout.setOnRefreshListener {
            viewModel.getMyClaims(MyDataRequest(Prefs.getUser()?.apiKey as String))
                .observe(this, claimObserver)
            toastSuccess(resources.getString(R.string.page_refreshed))
            binding.refreshLayout.isRefreshing = false
        }


        viewModel.getMyClaims(MyDataRequest(Prefs.getUser()?.apiKey as String))
            .observe(this, claimObserver)
    }

    @SuppressLint("NotifyDataSetChanged")
    private val claimObserver = Observer<ApiResponse<List<ClaimEntity>>> { claims ->
        showLoading()
        if (claims != null) {
            when (claims.status) {
                StatusResponse.ERROR -> {
                    toastInfo(resources.getString(R.string.something_went_wrong))
                    alertDialog.dismiss()
                }
                StatusResponse.SUCCESS -> {
                    alertDialog.dismiss()
                    if (claims.body.isNotEmpty()) {
                        showIllustrator(false)
                    }else{
                        showIllustrator(true)
                    }
                    myClaimAdapter.setData(claims.body)
                    myClaimAdapter.notifyDataSetChanged()
                }
                StatusResponse.EMPTY -> {
                    toastInfo(resources.getString(R.string.something_went_wrong))
                    alertDialog.dismiss()
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
        alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setView(layout)
        alertDialog.setCancelable(false)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }
}