package com.truesightid.ui.prediction

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.truesightid.R
import com.truesightid.data.source.local.entity.PredictEntity
import com.truesightid.data.source.remote.response.NewsPredictionResponse
import com.truesightid.databinding.FragmentPredictionBinding
import com.truesightid.ui.ViewModelFactory
import com.truesightid.utils.Prefs
import com.truesightid.utils.extension.toastError
import com.truesightid.utils.extension.toastInfo

class NewsPredictFragment : Fragment() {

    private var _binding: FragmentPredictionBinding? = null
    private val binding get() = _binding!!

    private lateinit var apiKey: String
    private lateinit var alertDialog: android.app.AlertDialog

    private lateinit var viewModel: NewsPredictViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPredictionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = ViewModelFactory.getInstance(
            requireContext()
        )
        if (Prefs.isLogin) {
            apiKey = Prefs.getUser()?.apiKey as String
            viewModel = ViewModelProvider(this, factory)[NewsPredictViewModel::class.java]


            binding.btnPredict.setOnClickListener {
                val predict =
                    "${binding.titleNews.editText?.text} ${binding.authorNews.editText?.text} ${binding.contentNews.editText?.text}"
                toastInfo("Predict: $predict")
                showLoading()
                viewModel.getNewsPrediction(apiKey, predict) { success ->
                    alertDialog.dismiss()
                    if (success){
                        viewModel.predictViewModel.observe(viewLifecycleOwner) { predict ->
                            if (predict != null) {
                                showPrediction(predict)
                            }
                        }
                        removeFocusAfterPredict()
                    }else{
                        toastError("Timeout")
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    private fun showPrediction(prediction: NewsPredictionResponse) {
        val predictionModel = PredictEntity(
            binding.titleNews.editText?.text.toString(),
            binding.authorNews.editText?.text.toString(),
            binding.contentNews.editText?.text.toString()
        )
        val newsDialog = NewsPredictionDialog(requireActivity(), prediction, Prefs, predictionModel)
        newsDialog.show(parentFragmentManager, "dialog")
    }

    private fun showLoading() {
        val inflater = layoutInflater
        val layout = inflater.inflate(R.layout.view_loading, null)
        alertDialog = android.app.AlertDialog.Builder(requireContext()).create()
        alertDialog.setView(layout)
        alertDialog.setCancelable(false)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }

    private fun showDialog(isResponse: Boolean) {
        val positiveButtonClick = { dialog: DialogInterface, _: Int ->
            dialog.cancel()
        }
        if (!isResponse) {
            AlertDialog.Builder(requireContext())
                .setTitle(resources.getString(R.string.failed_to_get_data))
                .setMessage(resources.getString(R.string.something_is_wrong_check_internet))
                .setPositiveButton(
                    resources.getString(R.string.dialog_ok),
                    DialogInterface.OnClickListener(function = positiveButtonClick)
                )
                .show()
        }
    }

    private fun removeFocusAfterPredict() {
        hideKeyboard(requireActivity())
        binding.titleNews.clearFocus()
        binding.authorNews.clearFocus()
        binding.contentNews.clearFocus()
    }

    private fun hideKeyboard(activity: Activity) {
        val imm =
            activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}