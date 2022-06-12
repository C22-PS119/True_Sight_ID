package com.truesightid.ui.myclaim

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors.ALPHA_FULL
import com.truesightid.R
import com.truesightid.data.source.local.entity.ClaimEntity
import com.truesightid.data.source.remote.ApiResponse
import com.truesightid.data.source.remote.StatusResponse
import com.truesightid.data.source.remote.request.GetClaimsRequest
import com.truesightid.data.source.remote.request.MyDataRequest
import com.truesightid.databinding.ActivityMyclaimBinding
import com.truesightid.ui.ViewModelFactory
import com.truesightid.ui.adapter.MyClaimAdapter
import com.truesightid.ui.main.MainActivity
import com.truesightid.utils.Prefs
import com.truesightid.utils.extension.toastError
import com.truesightid.utils.extension.toastInfo
import com.truesightid.utils.extension.toastSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.abs


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

        val itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0,  ItemTouchHelper.RIGHT ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val id = viewHolder.absoluteAdapterPosition
                val claimId = myClaimAdapter.getClaimAt(id)?.id ?: -1
                viewModel.deleteClaimById(Prefs.getUser()?.apiKey.toString(), claimId) { success ->
                    if (success) {
                        viewModel.getMyClaims(MyDataRequest(Prefs.getUser()?.apiKey as String))
                            .observe(this@MyClaimActivity, claimObserver)
                        GlobalScope.launch(Dispatchers.IO) {
                            viewModel.deleteLocalClaim(claimId)
                        }
                        toastInfo(getString(R.string.deleted))
                    }
                    else {
                        myClaimAdapter.notifyDataSetChanged() // Cancel changes
                        toastError(getString(R.string.delete_failed))
                    }
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView
                    val p = Paint()

                     if (dX <= 0){
                        super.onChildDraw(
                            c,
                            recyclerView,
                            viewHolder,
                            dX,
                            dY,
                            actionState,
                            isCurrentlyActive
                        )
                    } else {
                        val rightButton = RectF(
                            itemView.left.toFloat(),
                            itemView.top.toFloat(),
                            itemView.right.toFloat(),
                            itemView.bottom.toFloat()
                        )
                        p.color = Color.RED
                        val radius = 8F * resources.displayMetrics.density
                        c.drawRoundRect(rightButton,radius , radius ,p)
                        val d = resources.getDrawable(www.sanju.motiontoast.R.drawable.ic_delete_, null)
                        val wrappedDrawable = DrawableCompat.wrap(d!!)
                        DrawableCompat.setTint(wrappedDrawable, Color.WHITE)
                        d.setBounds(itemView.left + itemView.height /4, itemView.top + itemView.height/4, itemView.left + itemView.height / 4 +  itemView.height / 2 , itemView.bottom - itemView.height / 4)
                        d.draw(c)
                    }

//                    val alpha = ALPHA_FULL - abs(dX) / viewHolder.itemView.width
//                        .toFloat()
//                    viewHolder.itemView.alpha = alpha
                    viewHolder.itemView.translationX = dX
                }else{
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )}
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvMyClaim)

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