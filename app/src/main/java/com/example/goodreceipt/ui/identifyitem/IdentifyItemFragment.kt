package com.example.goodreceipt.ui.identifyitem

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.goodreceipt.MainActivity
import com.example.goodreceipt.R
import com.example.goodreceipt.databinding.FragmentIdentifyItemBinding
import com.example.goodreceipt.ui.materialselection.MaterialSelectionFragment
import com.example.goodreceipt.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IdentifyItemFragment : Fragment() {

    private var _binding: FragmentIdentifyItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIdentifyItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as? MainActivity)?.showBottomNavigation(false)
        binding.btnNextStep.isEnabled = false
        binding.btnNextStep.alpha = 0.6f
        setupClickListeners()
        setupGoodIdInput()
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as? MainActivity)?.showBottomNavigation(false)
    }

    private fun setupClickListeners() {
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.btnScanQrCode.setOnClickListener {
            requireContext().showToast("Scan QR Code clicked")
        }

        binding.btnNextStep.setOnClickListener {
            val goodId = binding.etGoodId.text?.toString()?.trim() ?: ""
            if (goodId.length == 10) {
                MaterialSelectionFragment.navigateTo(parentFragmentManager)
            } else {
                requireContext().showToast("Please enter a valid 10-digit Good ID")
            }
        }
    }

    private fun setupGoodIdInput() {
        binding.etGoodId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isValid = s?.length == 10
                binding.btnNextStep.isEnabled = isValid
                binding.btnNextStep.alpha = if (isValid) 1.0f else 0.6f
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun navigateTo(fragmentManager: FragmentManager, addToBackStack: Boolean = true) {
            val fragment = IdentifyItemFragment()
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            if (addToBackStack) {
                transaction.addToBackStack(null)
            }
            transaction.commit()
        }
    }
}
