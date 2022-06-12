package com.truesightid.ui.prediction

import DirtyFilter
import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.truesightid.R
import com.truesightid.data.source.local.entity.PredictEntity
import com.truesightid.data.source.remote.response.NewsPredictionResponse
import com.truesightid.databinding.FragmentPredictionBinding
import com.truesightid.ui.ViewModelFactory
import com.truesightid.utils.Prefs
import com.truesightid.utils.extension.getTotalWords
import com.truesightid.utils.extension.showErrorDialog
import com.truesightid.utils.extension.toastInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

            observingViewModel(viewModel)

            binding.btnPredict.setOnClickListener {
                val title = binding.titleNews.editText?.text.toString()
                val content = binding.contentNews.editText?.text.toString()
                var error: String? = null
                GlobalScope.launch(Dispatchers.Main) {
                    delay(500) // Avoiding skip frames error
                    error = validateInput(title, content)
                }.invokeOnCompletion {
                    if (error != null) {
                        showErrorDialog(error.toString())
                    } else {
                        val predict =
                            "$title $content"
                        toastInfo("Predict: $predict")
                        viewModel.getNewsPrediction(apiKey, predict)
                        removeFocusAfterPredict()
                    }
                }
            }
        }

    }

    private fun validateInput(title: String, content: String): String? {
        if (getTotalWords(content) < 20)
            return resources.getString(R.string.content_must_be)
        if (getTotalWords(content) > 100)
            return resources.getString(R.string.content_must_under)
        if (getTotalWords(title) < 3)
            return resources.getString(R.string.title_must_be)
        if (getTotalWords(title) > 15)
            return resources.getString(R.string.title_must_be_under)
        if (DirtyFilter.isContainDirtyWord(content, DirtyFilter.DirtyWords))
            return resources.getString(R.string.content_dirty_words)
        if (DirtyFilter.isContainDirtyWord(title, DirtyFilter.DirtyWords))
            return resources.getString(R.string.title_dirty_words)
        return null
    }

    private fun observingViewModel(viewModel: NewsPredictViewModel) {
        viewModel.predictViewModel.observe(viewLifecycleOwner) { predict ->
            if (predict != null) {
                showPrediction(predict)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading == true) {
                showLoading()
            } else {
                if (this::alertDialog.isInitialized)
                    alertDialog.dismiss()
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