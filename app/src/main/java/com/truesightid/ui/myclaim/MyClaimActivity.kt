package com.truesightid.ui.myclaim

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.truesightid.data.source.local.entity.ClaimEntity
import com.truesightid.data.source.remote.ApiResponse
import com.truesightid.data.source.remote.StatusResponse
import com.truesightid.data.source.remote.request.MyClaimRequest
import com.truesightid.databinding.ActivityMyclaimBinding
import com.truesightid.ui.ViewModelFactory
import com.truesightid.ui.adapter.MyClaimAdapter
import com.truesightid.ui.main.MainActivity
import com.truesightid.utils.Prefs
import com.truesightid.utils.extension.dismisLoading
import com.truesightid.utils.extension.pushActivity
import com.truesightid.utils.extension.showLoading
import com.truesightid.utils.extension.toastSuccess

class MyClaimActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyclaimBinding
    private lateinit var myClaimAdapter: MyClaimAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyclaimBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        val viewModel = ViewModelProvider(this, factory)[MyClaimViewModel::class.java]

        binding.ibBackLogin.setOnClickListener {
            pushActivity(MainActivity::class.java)
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
            viewModel.getMyClaims(MyClaimRequest(Prefs.getUser()?.apiKey as String))
                .observe(this, claimObserver)
            toastSuccess("Page Refreshed")
            binding.refreshLayout.isRefreshing = false
        }

        viewModel.getMyClaims(MyClaimRequest(Prefs.getUser()?.apiKey as String))
            .observe(this, claimObserver)
    }

    @SuppressLint("NotifyDataSetChanged")
    private val claimObserver = Observer<ApiResponse<List<ClaimEntity>>> { claims ->
        if (claims != null) {
            when (claims.status) {
                StatusResponse.ERROR -> showLoading()
                StatusResponse.SUCCESS -> {
                    dismisLoading()
                    myClaimAdapter.setData(claims.body)
                    myClaimAdapter.notifyDataSetChanged()
                }
                StatusResponse.EMPTY -> {
                    dismisLoading()
                    Toast.makeText(this, "Error: Somethings went wrong", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

}