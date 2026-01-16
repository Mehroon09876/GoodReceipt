package com.example.goodreceipt.ui.materialselection

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goodreceipt.R
import com.example.goodreceipt.data.model.Material
import com.example.goodreceipt.databinding.FragmentMaterialSelectionBinding
import com.example.goodreceipt.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MaterialSelectionFragment : Fragment() {

    private var _binding: FragmentMaterialSelectionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MaterialSelectionViewModel by viewModels()
    private lateinit var adapter: MaterialAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMaterialSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as? com.example.goodreceipt.MainActivity)?.showBottomNavigation(false)

        setupRecyclerView()
        setupObservers()
        setupClickListeners()
        setupSearch()
        setupFilters()
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as? com.example.goodreceipt.MainActivity)?.showBottomNavigation(false)
    }

    private fun setupRecyclerView() {
        adapter = MaterialAdapter(
            materials = emptyList(),
            selectedIds = emptySet(),
            isSelectionMode = false,
            onItemLongClick = { material ->
                viewModel.enableSelectionMode()
                viewModel.toggleMaterialSelection(material.id)
            },
            onItemClick = { material ->
                viewModel.toggleMaterialSelection(material.id)
            }
        )
        binding.rvMaterials.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMaterials.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.isSelectionMode.observe(viewLifecycleOwner) { isSelectionMode ->
            updateAdapter(isSelectionMode)
        }

        viewModel.filteredMaterials.observe(viewLifecycleOwner) { materials ->
            updateAdapter(viewModel.isSelectionMode.value ?: false)
        }

        viewModel.selectedMaterials.observe(viewLifecycleOwner) { selectedIds ->
            updateAdapter(viewModel.isSelectionMode.value ?: false)

            val count = selectedIds.size
//            binding.tvSelectedCount.text = count.toString()
            binding.btnReviewSummary.isEnabled = count > 0
            binding.btnReviewSummary.alpha = if (count > 0) 1.0f else 0.6f
        }

        viewModel.selectedMaterialNames.observe(viewLifecycleOwner) { selectedNames ->
            // Observer kept for potential future use
        }
    }

    private fun setupClickListeners() {
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.ivFilter.setOnClickListener {
            val currentMode = viewModel.isSelectionMode.value ?: false
            if (currentMode) {
                viewModel.disableSelectionMode()
            } else {
                viewModel.enableSelectionMode()
            }
        }

        binding.btnReviewSummary.setOnClickListener {
            val selectedIds = viewModel.selectedMaterials.value ?: emptySet()
            val allMaterials = viewModel.materials.value ?: emptyList()
            
            if (selectedIds.isNotEmpty()) {
                val selectedMaterials = allMaterials.filter { selectedIds.contains(it.id) }
                val toastMessage = formatSelectedItemsForToast(selectedMaterials, allMaterials)
                requireContext().showToast(toastMessage)
            }
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.search(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupFilters() {
        viewModel.selectedWarehouses.observe(viewLifecycleOwner) { warehouses ->
//            updateFilterButtons(warehouses)
        }

        viewModel.inStockFilter.observe(viewLifecycleOwner) { inStock ->
//            updateInStockButton(inStock == true)
        }


    }



    private fun formatSelectedItemsForToast(selectedMaterials: List<Material>, allMaterials: List<Material>): String {
        if (selectedMaterials.isEmpty()) return ""
        
        val items = selectedMaterials.map { material ->
            val originalIndex = allMaterials.indexOfFirst { it.id == material.id }
            "$originalIndex: Name: ${material.name}, PONumber: ${material.poNumber}"
        }
        
        return items.joinToString("\n")
    }

    private fun formatSelectedMaterialsDetails(materials: List<Material>): String {
        if (materials.isEmpty()) return ""
        
        val details = materials.mapIndexed { index, material ->
            val itemNumber = index + 1
            """
            Item $itemNumber:
            - Name: ${material.name}
            - SKU: ${material.sku}
            - PO Number: ${material.poNumber}
            - Vendor Number: ${material.vendorNumber}
            - Delivery Date: ${material.deliveryDate}
            - Location: ${material.location}
            - Warehouse: ${material.warehouse}
            - Sector: ${material.sector}
            - Unit: ${material.unit}
            - Quantity: ${material.quantity}
            """.trimIndent()
        }
        
        return "Selected Items:\n${details.joinToString("\n\n")}"
    }

    private fun updateAdapter(isSelectionMode: Boolean) {
        val materials = viewModel.filteredMaterials.value ?: emptyList()
        val selectedIds = viewModel.selectedMaterials.value ?: emptySet()
        
        adapter = MaterialAdapter(
            materials = materials,
            selectedIds = selectedIds,
            isSelectionMode = isSelectionMode,
            onItemLongClick = { material ->
                viewModel.enableSelectionMode()
                viewModel.toggleMaterialSelection(material.id)
            },
            onItemClick = { material ->
                viewModel.toggleMaterialSelection(material.id)
            }
        )
        binding.rvMaterials.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun navigateTo(fragmentManager: FragmentManager, addToBackStack: Boolean = true) {
            val fragment = MaterialSelectionFragment()
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            if (addToBackStack) {
                transaction.addToBackStack(null)
            }
            transaction.commit()
        }
    }
}
