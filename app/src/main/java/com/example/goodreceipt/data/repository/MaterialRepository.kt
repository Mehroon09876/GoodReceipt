package com.example.goodreceipt.data.repository

import com.example.goodreceipt.data.model.Material
import com.example.goodreceipt.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MaterialRepository @Inject constructor() {

    fun getMaterials(): List<Material> {
        return listOf(
            Material(
                id = "1",
                name = "Steel Pipe 40mm",
                sku = "SP-40-GALV",
                location = "Warehouse A - Sector 4",
                warehouse = "Warehouse A",
                sector = "Sector 4",
                unit = "PCS I",
                quantity = 150,
                poNumber = "PO-88291-B",
                vendorNumber = "VN-4501",
                deliveryDate = "2024-01-15",
                iconRes = R.drawable.ic_cabinet
            ),
            Material(
                id = "2",
                name = "Aluminum Sheet",
                sku = "AL-SHT-001",
                location = "Warehouse A - Sector 2",
                warehouse = "Warehouse A",
                sector = "Sector 2",
                unit = "PCS I",
                quantity = 85,
                poNumber = "PO-92547-C",
                vendorNumber = "VN-3827",
                deliveryDate = "2024-01-22",
                iconRes = R.drawable.ic_grid
            ),
            Material(
                id = "3",
                name = "Steel Tools Set",
                sku = "ST-TLS-500",
                location = "Warehouse A - Sector 1",
                warehouse = "Warehouse A",
                sector = "Sector 1",
                unit = "PCS I",
                quantity = 12,
                poNumber = "PO-10384-A",
                vendorNumber = "VN-5621",
                deliveryDate = "2024-02-05",
                iconRes = R.drawable.ic_tools
            ),
            Material(
                id = "4",
                name = "Plastic Sheets",
                sku = "PL-SHT-200",
                location = "Warehouse A - Sector 3",
                warehouse = "Warehouse A",
                sector = "Sector 3",
                unit = "PCS I",
                quantity = 0,
                poNumber = "PO-11492-D",
                vendorNumber = "VN-2934",
                deliveryDate = "2024-02-12",
                iconRes = R.drawable.ic_layers
            ),
            Material(
                id = "5",
                name = "Copper Wire 10mm",
                sku = "CW-10-001",
                location = "Warehouse A - Sector 5",
                warehouse = "Warehouse A",
                sector = "Sector 5",
                unit = "PCS I",
                quantity = 200,
                poNumber = "PO-12756-E",
                vendorNumber = "VN-6789",
                deliveryDate = "2024-02-18",
                iconRes = R.drawable.ic_cabinet
            ),
            Material(
                id = "6",
                name = "Wooden Planks",
                sku = "WD-PLK-300",
                location = "Warehouse A - Sector 2",
                warehouse = "Warehouse A",
                sector = "Sector 2",
                unit = "PCS I",
                quantity = 45,
                poNumber = "PO-13924-F",
                vendorNumber = "VN-7845",
                deliveryDate = "2024-02-25",
                iconRes = R.drawable.ic_grid
            )
        )
    }

    fun filterMaterials(
        materials: List<Material>,
        query: String?,
        warehouse: String?,
        inStock: Boolean?
    ): List<Material> {
        var filtered = materials

        if (!query.isNullOrBlank()) {
            filtered = filtered.filter {
                it.name.contains(query, ignoreCase = true) ||
                it.sku.contains(query, ignoreCase = true)
            }
        }

        if (!warehouse.isNullOrBlank() && warehouse != "All Materials") {
            filtered = filtered.filter { it.warehouse == warehouse }
        }

        if (inStock == true) {
            filtered = filtered.filter { it.quantity > 0 }
        }

        return filtered
    }
}
