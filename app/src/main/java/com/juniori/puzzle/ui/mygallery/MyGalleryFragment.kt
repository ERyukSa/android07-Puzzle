package com.juniori.puzzle.ui.mygallery

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.juniori.puzzle.databinding.FragmentMygalleryBinding
import com.juniori.puzzle.ui.playvideo.PlayVideoActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyGalleryFragment : Fragment() {

    private var _binding: FragmentMygalleryBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel: MyGalleryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMygalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerAdapter = MyGalleryAdapter(viewModel) {
            startActivity(
                Intent(
                    requireContext(),
                    PlayVideoActivity::class.java
                ).apply {
                    this.putExtra("videoInfo", it)
                })
        }

        binding.recycleMyGallery.apply {
            adapter = recyclerAdapter
            val gridLayoutManager = GridLayoutManager(requireContext(), 2)
            layoutManager = gridLayoutManager
        }

        viewModel.list.observe(viewLifecycleOwner) { dataList ->
            recyclerAdapter.submitList(dataList)
        }

        viewModel.refresh.observe(viewLifecycleOwner) { isRefresh ->
            binding.progressMyGallery.isVisible = isRefresh
        }

        viewModel.list.value.also { list ->
            if (list == null || list.isEmpty()) {
                viewModel.getMyData()
            }
        }

        binding.searchMyGallery.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setQueryText(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    viewModel.setQueryText(newText)
                }
                return false
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
