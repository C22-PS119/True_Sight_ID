package com.truesightid.ui.prediction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.truesightid.databinding.FragmentPredictionBinding

class NewsPredictFragment : Fragment() {

    private var _binding: FragmentPredictionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val newsPredictViewModel =
            ViewModelProvider(this).get(NewsPredictViewModel::class.java)

        _binding = FragmentPredictionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNewsPredict
        newsPredictViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}