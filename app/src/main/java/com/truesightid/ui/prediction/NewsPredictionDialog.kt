package com.truesightid.ui.prediction

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.truesightid.R
import com.truesightid.data.source.local.entity.PredictEntity
import com.truesightid.data.source.remote.request.AddRemoveBookmarkRequest
import com.truesightid.data.source.remote.request.GetClaimsRequest
import com.truesightid.data.source.remote.response.NewsPredictionResponse
import com.truesightid.databinding.DialogPredictionBinding
import com.truesightid.ui.ViewModelFactory
import com.truesightid.ui.adapter.RecommendationAdapter
import com.truesightid.utils.Prefs
import com.truesightid.utils.UserAction
import com.truesightid.utils.extension.toastInfo

class NewsPredictionDialog(
    val activity: Activity,
    val prediction: NewsPredictionResponse,
    val Pref: Prefs,
    val predictModel: PredictEntity
) :
    DialogFragment() {
    private var _binding: DialogPredictionBinding? = null
    private val binding get() = _binding!!
    private lateinit var recommendationAdapter: RecommendationAdapter
    private lateinit var viewModel: NewsPredictViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogPredictionBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[NewsPredictViewModel::class.java]

        prediction.valPrediction?.let { binding.progressBarFact.setProgressPercentage((100 - it.toDouble() * 100L)) }
        prediction.valPrediction?.let { binding.progressBarFake.setProgressPercentage(it.toDouble() * 100L) }

        binding.btnConfirm.setOnClickListener {
            dialog?.dismiss()
        }

        recommendationAdapter = RecommendationAdapter(object: RecommendationAdapter.ItemClaimClickListener{
            override fun onClaimUpvote(claim_id: Int) {
                viewModel.upvoteClaimById(Prefs.getUser()?.apiKey as String, claim_id)
            }

            override fun onClaimDownvote(claim_id: Int) {
                viewModel.downvoteClaimById(Prefs.getUser()?.apiKey as String, claim_id)
            }

            override fun onBookmarkRemoved(claim_id: Int) {
                viewModel.addBookmarkById(
                    AddRemoveBookmarkRequest(
                        Prefs.getUser()?.apiKey as String,
                        claim_id
                    )
                )
                UserAction.applyUserBookmarks(claim_id, false)
                toastInfo(resources.getString(R.string.added_to_bookmark))
            }

            override fun onBookmarkAdded(claim_id: Int) {
                viewModel.removeBookmarkById(
                    AddRemoveBookmarkRequest(
                        Prefs.getUser()?.apiKey as String,
                        claim_id
                    )
                )
                UserAction.applyUserBookmarks(claim_id, true)
                toastInfo(resources.getString(R.string.removed_from_bookmark))
            }

        }, Prefs)

        setupView()

        with(binding.rvClaimer) {
            adapter = recommendationAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.getClaimsBySearch(
            GetClaimsRequest(
                Pref.getUser()?.apiKey as String,
                prediction.claim.toString()
            )
        ).observe(viewLifecycleOwner) { claims ->
            if (claims.body.isNotEmpty()) {
                binding.tvDescription3.visibility = View.VISIBLE
                binding.rvClaimer.visibility = View.VISIBLE
            }
            recommendationAdapter.setData(claims.body)
            recommendationAdapter.notifyDataSetChanged()
        }


    }

    override fun getView(): View? {
        val margin = (8F * resources.displayMetrics.density).toInt()
        val view = super.getView()
        view?.clipBounds?.set(margin,margin,resources.displayMetrics.widthPixels - margin,resources.displayMetrics.heightPixels - margin)
        return view
    }

    @SuppressLint("SetTextI18n")
    private fun setupView() {
        binding.tvTitleHeader.text = "Title: ${predictModel.title}"
        binding.tvAuthorHeader.text = "Author: ${predictModel.author}"
        binding.tvContentHeader.text = "Content: ${predictModel.content}"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }
}