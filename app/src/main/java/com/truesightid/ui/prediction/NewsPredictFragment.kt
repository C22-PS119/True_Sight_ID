package com.truesightid.ui.prediction

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.truesightid.R
import com.truesightid.databinding.FragmentPredictionBinding

class NewsPredictFragment : Fragment() {

    private var _binding: FragmentPredictionBinding? = null
    private val binding get() = _binding!!
    private lateinit var dialog : Dialog

    private val viewModel by viewModels<NewsPredictViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPredictionBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textNewsPredict
//        newsPredictViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog = Dialog(requireContext())

        binding.btnPredict.setOnClickListener {
            predictionDialog()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun predictionDialog() {
        dialog.setContentView(R.layout.prediction_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val close = dialog.findViewById<ImageView>(R.id.iv_close)

        close.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }
}