package com.truesightid.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.truesightid.databinding.FragmentExploreBinding
import com.truesightid.ui.adapter.ExploreAdapter
import com.truesightid.utils.DataDummy

class ExploreNewsFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ExploreNewsViewModel>()
    private lateinit var exploreAdapter: ExploreAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textExplore
//        exploreNewsViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {

            exploreAdapter = ExploreAdapter()

            val listClaims = DataDummy.generateDummyMovies()

            exploreAdapter.setList(listClaims)

            with(binding.rvClaimer) {
                layoutManager = LinearLayoutManager(context)
                adapter = exploreAdapter
                setHasFixedSize(true)
            }

        }

        binding.fab.setOnClickListener {
            Toast.makeText(activity, "add claim", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}