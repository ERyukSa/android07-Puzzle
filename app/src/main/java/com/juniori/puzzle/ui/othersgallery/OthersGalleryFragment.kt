package com.juniori.puzzle.ui.othersgallery

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.juniori.puzzle.R
import com.juniori.puzzle.databinding.FragmentOthersgalleryBinding
import com.juniori.puzzle.util.SortType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OthersGalleryFragment : Fragment() {

    private var _binding: FragmentOthersgalleryBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel: OthersGalleryViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOthersgalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerAdapter = OtherGalleryAdapter(viewModel)

        binding.recycleOtherGallery.apply {
            adapter = recyclerAdapter
            val gridLayoutManager = GridLayoutManager(requireContext(), 2)
            layoutManager = gridLayoutManager
        }


        val items = resources.getStringArray(R.array.other_order_type)
        val spinnerAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
        binding.spinnerOtherGallery.adapter = spinnerAdapter

        binding.spinnerOtherGallery.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        0 -> {
                            if(viewModel.setOrderType(SortType.NEW)){
                                binding.recycleOtherGallery.scrollToPosition(0)
                            }
                        }

                        1 -> {
                            if(viewModel.setOrderType(SortType.LIKE)){
                                binding.recycleOtherGallery.scrollToPosition(0)
                            }
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
        viewModel.list.observe(viewLifecycleOwner) { dataList ->
            recyclerAdapter.submitList(dataList)
        }

        viewModel.refresh.observe(viewLifecycleOwner) { isRefresh ->
            binding.progressOtherGallery.isVisible = isRefresh
        }

        viewModel.getMyData()

        binding.searchOtherGallery.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
