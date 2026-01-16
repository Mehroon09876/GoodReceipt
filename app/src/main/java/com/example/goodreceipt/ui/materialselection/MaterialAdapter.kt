package com.example.goodreceipt.ui.materialselection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.goodreceipt.R
import com.example.goodreceipt.data.model.Material
import com.example.goodreceipt.databinding.ItemMaterialBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Random

class MaterialAdapter(
    private val materials: List<Material>,
    private val selectedIds: Set<String>,
    private val isSelectionMode: Boolean,
    private val onItemLongClick: (Material) -> Unit,
    private val onItemClick: (Material) -> Unit
) : RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialViewHolder {
        val binding = ItemMaterialBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MaterialViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MaterialViewHolder, position: Int) {
        holder.bind(
            materials[position],
            selectedIds.contains(materials[position].id),
            isSelectionMode
        )
    }

    override fun getItemCount() = materials.size

    inner class MaterialViewHolder(
        private val binding: ItemMaterialBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(material: Material, isSelected: Boolean, selectionMode: Boolean) {
            binding.apply {

                tvMaterialName.text = material.name
                tvSku.text = material.sku
                tvLocation.text = material.fullLocation
                tvUnit.text = material.unit
                tvQuantity.text = material.quantity.toString()

                // Generate random values for PO Number, Vendor Number, and Delivery Date
                val random = Random()
                val randomPoNumber = "PO-${10000 + random.nextInt(90000)}-${('A'..'Z').random()}"
                val randomVendorNumber = "VN-${1000 + random.nextInt(9000)}"
                
                // Generate random delivery date (within next 60 days)
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DAY_OF_YEAR, random.nextInt(60))
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val randomDeliveryDate = dateFormat.format(calendar.time)
                
                tvPoNumber.text = "PO: $randomPoNumber"
                tvVendorNumber.text = "VN: $randomVendorNumber"
                tvDeliveryDate.text = "DD: $randomDeliveryDate"

                tvQuantity.setTextColor(
                    ContextCompat.getColor(
                        root.context,
                        if (material.quantity > 0) R.color.accent_blue else R.color.red_accent
                    )
                )

                rlGr.isEnabled = material.quantity > 0
                rlGr.background = ContextCompat.getDrawable(
                    root.context,
                    if (material.quantity > 0) R.drawable.bg_gr_button else R.drawable.bg_gr_button_inactive
                )


                cbSelect.visibility = if (selectionMode) View.VISIBLE else View.GONE
                cbSelect.isChecked = isSelected

                cbSelect.setOnClickListener {
                    onItemClick(material)
                }

                if (isSelected) {
                    root.alpha = 0.7f
                    root.setBackgroundColor(
                        ContextCompat.getColor(root.context, R.color.light_blue)
                    )
                } else {
                    root.alpha = 1.0f
                    root.background = null
                }

                root.setOnLongClickListener {
                    onItemLongClick(material)
                    true
                }

                root.setOnClickListener {
                    if (selectionMode) {
                        onItemClick(material)
                    }
                }
            }
        }
    }
}
