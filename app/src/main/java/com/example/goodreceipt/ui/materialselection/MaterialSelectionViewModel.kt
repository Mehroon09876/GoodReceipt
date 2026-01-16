package com.example.goodreceipt.ui.materialselection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.goodreceipt.data.model.Material
import com.example.goodreceipt.data.repository.MaterialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MaterialSelectionViewModel @Inject constructor(
    private val materialRepository: MaterialRepository
) : ViewModel() {

    private val _materials = MutableLiveData<List<Material>>()
    val materials: LiveData<List<Material>> = _materials

    private val _filteredMaterials = MutableLiveData<List<Material>>()
    val filteredMaterials: LiveData<List<Material>> = _filteredMaterials

    private val _selectedMaterials = MutableLiveData<Set<String>>(emptySet())
    val selectedMaterials: LiveData<Set<String>> = _selectedMaterials

    private val _searchQuery = MutableLiveData<String>("")
    val searchQuery: LiveData<String> = _searchQuery

    private val _selectedWarehouses = MutableLiveData<Set<String>>(emptySet())
    val selectedWarehouses: LiveData<Set<String>> = _selectedWarehouses

    private val _inStockFilter = MutableLiveData<Boolean?>(null)
    val inStockFilter: LiveData<Boolean?> = _inStockFilter

    private val _selectedMaterialNames = MutableLiveData<List<String>>(emptyList())
    val selectedMaterialNames: LiveData<List<String>> = _selectedMaterialNames

    private val _isSelectionMode = MutableLiveData<Boolean>(false)
    val isSelectionMode: LiveData<Boolean> = _isSelectionMode

    init {
        loadMaterials()
    }

    private fun loadMaterials() {
        val materialsList = materialRepository.getMaterials()
        _materials.value = materialsList
        applyFilters()
    }

    fun search(query: String) {
        _searchQuery.value = query
        applyFilters()
    }

    fun toggleWarehouseFilter(warehouse: String) {
        val currentWarehouses = _selectedWarehouses.value?.toMutableSet() ?: mutableSetOf()
        if (warehouse == "All Materials") {
            currentWarehouses.clear()
        } else {
            if (currentWarehouses.contains(warehouse)) {
                currentWarehouses.remove(warehouse)
            } else {
                currentWarehouses.add(warehouse)
            }
            currentWarehouses.remove("All Materials")
        }
        _selectedWarehouses.value = currentWarehouses
        applyFilters()
    }

    fun toggleInStockFilter() {
        val currentFilter = _inStockFilter.value
        _inStockFilter.value = if (currentFilter == true) null else true
        applyFilters()
    }

    private fun applyFilters() {
        val materials = _materials.value ?: return
        val warehouses = _selectedWarehouses.value
        val warehouseFilter = if (warehouses.isNullOrEmpty() || warehouses.contains("All Materials")) {
            null
        } else {
            warehouses.firstOrNull()
        }
        
        val filtered = materialRepository.filterMaterials(
            materials = materials,
            query = _searchQuery.value,
            warehouse = warehouseFilter,
            inStock = _inStockFilter.value
        )
        _filteredMaterials.value = filtered
    }

    fun toggleMaterialSelection(materialId: String) {
        val currentSelection = _selectedMaterials.value?.toMutableSet() ?: mutableSetOf()
        val materials = _materials.value ?: emptyList()
        
        if (currentSelection.contains(materialId)) {
            currentSelection.remove(materialId)
        } else {
            currentSelection.add(materialId)
        }
        _selectedMaterials.value = currentSelection
        
        val selectedNames = materials
            .filter { currentSelection.contains(it.id) }
            .map { it.name }
        _selectedMaterialNames.value = selectedNames
    }

    fun clearSelection() {
        _selectedMaterials.value = emptySet()
    }

    fun getSelectedCount(): Int {
        return _selectedMaterials.value?.size ?: 0
    }

    fun enableSelectionMode() {
        _isSelectionMode.value = true
    }

    fun disableSelectionMode() {
        _isSelectionMode.value = false
    }
}
