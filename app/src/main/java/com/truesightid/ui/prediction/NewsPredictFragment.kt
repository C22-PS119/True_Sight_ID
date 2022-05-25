package com.truesightid.ui.prediction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.truesightid.databinding.FragmentPredictionBinding
import com.truesightid.ui.explore.ExploreNewsViewModel

class NewsPredictFragment : Fragment() {

    private var _binding: FragmentPredictionBinding? = null
    private val binding get() = _binding!!

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}