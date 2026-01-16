package com.example.goodreceipt.ui.login

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.example.goodreceipt.MainActivity
import com.example.goodreceipt.R
import com.example.goodreceipt.databinding.FragmentLoginBinding
import com.example.goodreceipt.ui.common.LoadingHelper
import com.example.goodreceipt.ui.home.HomeFragment
import com.example.goodreceipt.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()
    
    @Inject
    lateinit var loadingHelper: LoadingHelper
    
    private var isPasswordVisible = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as? com.example.goodreceipt.MainActivity)?.showBottomNavigation(false)

        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.btnSignIn.isEnabled = !isLoading
            if (isLoading) {
                loadingHelper.showLoading(this, "Signing in...")
            } else {
                loadingHelper.hideLoading()
            }
        }

        viewModel.loginSuccess.observe(viewLifecycleOwner) { response ->
            response?.let {
                requireContext().showToast("Login successful!")
                HomeFragment.navigateTo(parentFragmentManager)
                (requireActivity() as? MainActivity)?.apply {
                    showBottomNavigation(true)
                    setSelectedNavigationItem(R.id.nav_home)
                }
            }
        }

        viewModel.loginError.observe(viewLifecycleOwner) { error ->
            error?.let {
                requireContext().showToast(it)
            }
        }

        viewModel.emailError.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                binding.tvEmailError.text = error
                binding.tvEmailError.visibility = View.VISIBLE
            } else {
                binding.tvEmailError.visibility = View.GONE
            }
        }

        viewModel.passwordError.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                binding.tvPasswordError.text = error
                binding.tvPasswordError.visibility = View.VISIBLE
            } else {
                binding.tvPasswordError.visibility = View.GONE
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()
            viewModel.login(email, password)
        }

        binding.ivPasswordToggle.setOnClickListener {
            togglePasswordVisibility()
        }

        binding.tvForgotPassword.setOnClickListener {
            requireContext().showToast("Forgot password clicked")
        }

        binding.tvRequestAccess.setOnClickListener {
            requireContext().showToast("Request access clicked")
        }

        binding.etEmail.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                viewModel.clearErrors()
            }
        }

        binding.etPassword.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                viewModel.clearErrors()
            }
        }
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        if (isPasswordVisible) {
            binding.etPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.ivPasswordToggle.setImageResource(R.drawable.ic_visibility)
        } else {
            binding.etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.ivPasswordToggle.setImageResource(R.drawable.ic_visibility_off)
        }
        binding.etPassword.setSelection(binding.etPassword.text?.length ?: 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        fun navigateTo(fragmentManager: FragmentManager, addToBackStack: Boolean = false) {
            val fragment = LoginFragment()
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            if (addToBackStack) {
                transaction.addToBackStack(null)
            }
            transaction.commit()
        }
    }
}
