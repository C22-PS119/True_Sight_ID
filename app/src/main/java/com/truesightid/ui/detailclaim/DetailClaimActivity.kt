package com.truesightid.ui.detailclaim

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.truesightid.R
import com.truesightid.data.source.local.entity.ClaimEntity
import com.truesightid.data.source.local.entity.CommentEntity
import com.truesightid.data.source.remote.ApiResponse
import com.truesightid.data.source.remote.StatusResponse
import com.truesightid.data.source.remote.request.AddCommentRequest
import com.truesightid.data.source.remote.request.AddRemoveBookmarkRequest
import com.truesightid.data.source.remote.request.GetCommentsRequest
import com.truesightid.databinding.ActivityClaimDetailBinding
import com.truesightid.ui.ViewModelFactory
import com.truesightid.ui.adapter.CommentsAdapter
import com.truesightid.ui.adapter.DetailImagesAdapter
import com.truesightid.utils.DateUtils
import com.truesightid.utils.Prefs
import com.truesightid.utils.UserAction
import com.truesightid.utils.extension.getTotalWords
import java.lang.Thread.sleep


class DetailClaimActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClaimDetailBinding
    private lateinit var imagesAdapter: DetailImagesAdapter
    private lateinit var commentsAdapter: CommentsAdapter
    private lateinit var viewModel: DetailClaimViewModel
    private lateinit var alertDialog: AlertDialog
    private lateinit var itemExtras: ClaimEntity
    private var isAddComment = false

    companion object {
        const val EXTRA_CLAIM = "extra_claim"
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClaimDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[DetailClaimViewModel::class.java]

        imagesAdapter =
            DetailImagesAdapter(this, object : DetailImagesAdapter.DetailImagesCallback {
                override fun onNextArrow(position: Int) {
                    binding.vpImages.currentItem = position
                }

                override fun onPrevArrow(position: Int) {
                    binding.vpImages.currentItem = position
                }

            })

        commentsAdapter = CommentsAdapter()

        // Setup back button
        binding.ibBackDetail.setOnClickListener {
            finish()
        }

        // Setup view more button
        binding.tvViewMore.setOnClickListener {
            showAllText()
        }

        // Setup view less button
        binding.tvViewLess.setOnClickListener {
            hideText()
        }

        // Setup comment
        binding.tvMyComment.setOnClickListener {
            binding.tvMyComment.visibility = View.GONE
            binding.etComment.visibility = View.VISIBLE
            binding.etComment.requestFocus()
            showSoftKeyboard(binding.etComment)
        }

        binding.etComment.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_send_disable, 0)

        binding.etComment.doOnTextChanged { _, _, _, _ ->
            if (binding.etComment.text.isBlank())
                binding.etComment.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_send_disable, 0)
            else
                binding.etComment.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_send, 0)
        }

        binding.etComment.setOnTouchListener(OnTouchListener { _, event ->
            if (binding.etComment.text.isBlank())
                return@OnTouchListener false
            val drawableEnd = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding.etComment.right - binding.etComment.compoundDrawables[drawableEnd].bounds.width()) {
                    val request = AddCommentRequest(
                        Prefs.getUser()?.apiKey as String,
                        itemExtras.id,
                        binding.etComment.text.toString()
                    )
                    viewModel.addCommentByClaimId(request)
//                    Toast.makeText(this, request.toString(), Toast.LENGTH_LONG).show()
                    sleep(200)
                    // Update view by calling this
                    viewModel.getCommentsByClaimId(
                        GetCommentsRequest(Prefs.getUser()?.apiKey as String, itemExtras.id)
                    ).observe(this, commentsObserver)

                    removeFocusAfterComment()
                    isAddComment = true
                    return@OnTouchListener true
                }
            }
            false
        })

        // Get intent extras
        val extras = intent.extras
        if (extras != null) {
            val items = intent.getParcelableExtra<ClaimEntity>(EXTRA_CLAIM)

            if (items != null) {
                setupView(items)
            }
        }

        // Set adapter
        binding.vpImages.adapter = imagesAdapter

        with(binding.rvComment) {
            val linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager
            adapter = commentsAdapter
            setHasFixedSize(true)
        }

        viewModel.getCommentsByClaimId(
            GetCommentsRequest(
                Prefs.getUser()?.apiKey as String,
                itemExtras.id
            )
        ).observe(this, commentsObserver)
    }

    private fun removeFocusAfterComment() {
        hideKeyboard(this)
        binding.etComment.text.clear()
        binding.etComment.clearFocus()
    }

    @SuppressLint("NotifyDataSetChanged")
    private val commentsObserver = Observer<ApiResponse<List<CommentEntity>>> { comments ->
        showLoading()
        if (comments != null) {
            when (comments.status) {
                StatusResponse.EMPTY -> alertDialog.dismiss()
                StatusResponse.SUCCESS -> {
                    val data = comments.body
                    alertDialog.dismiss()
                    if (isAddComment) {
                        commentsAdapter.setComments(data)
                        commentsAdapter.notifyItemInserted(data.size)
                        binding.rvComment.scrollToPosition(commentsAdapter.itemCount)
                        isAddComment = false

                    } else {
                        commentsAdapter.setComments(data)
                        commentsAdapter.notifyDataSetChanged()
                    }


                }
                StatusResponse.ERROR -> {
                    Toast.makeText(this, "Error: Somethings went wrong", Toast.LENGTH_SHORT)
                        .show()
                    alertDialog.dismiss()
                }
            }
        }
    }

    private fun showCommentAvatar() {
        Glide.with(this)
            .load(Prefs.getUser()?.avatar as String)
            .timeout(3000)
            .apply(
                RequestOptions.placeholderOf(R.drawable.ic_loading)
                    .error(R.drawable.logo_true_sight)
            )
            .into(binding.ivAvatarComment)
    }

    private fun showSoftKeyboard(etComment: EditText) {
        if (etComment.requestFocus()) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(etComment, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun showAllText() {
        binding.tvViewMore.visibility = View.INVISIBLE
        binding.tvViewLess.visibility = View.VISIBLE
        binding.tvDescription.maxLines = 10000
        binding.tvDescription.ellipsize = null
    }

    private fun hideText() {
        binding.tvViewMore.visibility = View.VISIBLE
        binding.tvViewLess.visibility = View.INVISIBLE
        binding.tvDescription.maxLines = 6
        binding.tvDescription.ellipsize = TextUtils.TruncateAt.END
    }

    private fun setupView(items: ClaimEntity) {
        itemExtras = items
        showCommentAvatar()

        with(binding) {
            tvTitleDetail.text = items.title
            tvDescription.text = items.description

            if (getTotalWords(items.description) < 40) {
                binding.tvViewMore.visibility = View.INVISIBLE
                binding.tvViewLess.visibility = View.INVISIBLE
            }

            binding.tvDescription.maxLines = 10
            binding.tvDescription.post {
                if (binding.tvDescription.lineCount > 6) {
                    hideText()
                } else {
                    binding.tvViewMore.visibility = View.GONE
                    binding.tvViewLess.visibility = View.GONE
                }
            }

            imagesAdapter.setImages(items.image)

            var url = StringBuilder()
            items.url.forEachIndexed { index, s ->
                if (index == 0 && items.url.count() == 1) {
                    url = StringBuilder().append(s)
                } else if (index == 0 && items.url.count() > 1) {
                    url = StringBuilder().append("$s\n")
                } else if (index > 0 && items.url.count() > 1 && index != items.url.count()) {
                    url.append("$s\n")
                } else if (index > 0 && items.url.count() > 1 && index == items.url.count()) {
                    url.append(s)
                }
            }

            tvSourceDetail.text = url

            if (url.isBlank()){
                tvTitleSource.visibility = View.GONE
            }else{
                tvTitleSource.visibility = View.VISIBLE
            }

            tvClaimerDetail.text = getString(R.string.claimed_by, items.claimer)
            tvDateDetail.text = DateUtils.getDateTime(items.date.toLong())

            if (items.fake == 1) {
                tvClaim.text = getString(R.string.fake_status)
                tvClaim.background =
                    AppCompatResources.getDrawable(applicationContext, R.drawable.fake_claim)
            } else {
                tvClaim.text = getString(R.string.fact_status)
                tvClaim.background =
                    AppCompatResources.getDrawable(applicationContext, R.drawable.fact_claim)
            }

            binding.ibBookmark.setOnClickListener {
                val user = Prefs.getUser()
                val bookmark = user?.bookmark
                if (bookmark?.contains(items.id) == true) {
                    binding.ibBookmark.setImageResource(R.drawable.ic_add_bookmark)
                    viewModel.removeBookmarkById(AddRemoveBookmarkRequest(user.apiKey, items.id))
                    UserAction.applyUserBookmarks(items.id, false)
                } else {
                    binding.ibBookmark.setImageResource(R.drawable.ic_remove_bookmark)
                    viewModel.addBookmarkById(AddRemoveBookmarkRequest(user?.apiKey.toString(), items.id))
                    UserAction.applyUserBookmarks(items.id, true)
                }
            }

            val user = Prefs.getUser()
            val bookmark = user?.bookmark
            if (bookmark?.contains(items.id) == true) {
                binding.ibBookmark.setImageResource(R.drawable.ic_remove_bookmark)
            } else {
                binding.ibBookmark.setImageResource(R.drawable.ic_add_bookmark)
            }


            binding.ibShare.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    resources.getString(R.string.share_messages, items.claimer, items.title)
                )
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, resources.getString(R.string.send_to)))
            }
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

    private fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}