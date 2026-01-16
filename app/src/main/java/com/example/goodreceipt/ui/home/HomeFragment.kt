package com.example.goodreceipt.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.goodreceipt.MainActivity
import com.example.goodreceipt.R
import com.example.goodreceipt.databinding.FragmentHomeBinding
import com.example.goodreceipt.ui.common.LogoutDialog
import com.example.goodreceipt.ui.identifyitem.IdentifyItemFragment
import com.example.goodreceipt.ui.login.LoginFragment
import com.example.goodreceipt.utils.PrefsHelper
import com.example.goodreceipt.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    @Inject
    lateinit var prefsHelper: PrefsHelper
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as? MainActivity)?.apply {
            showBottomNavigation(true)
            setSelectedNavigationItem(R.id.nav_home)
        }
        val email=prefsHelper.getUserEmail()
        if(email?.isNotEmpty() == true){
            binding.tvGreeting.text="Hello, $email"
        }
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as? MainActivity)?.apply {
            showBottomNavigation(true)
            setSelectedNavigationItem(R.id.nav_home)
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            (requireActivity() as? MainActivity)?.apply {
                showBottomNavigation(true)
                setSelectedNavigationItem(R.id.nav_home)
            }
        }
    }

    private fun setupClickListeners() {
        binding.cardNewReceipt.setOnClickListener {
            IdentifyItemFragment.navigateTo(parentFragmentManager)
        }

        binding.ivMaximize.setOnClickListener {
            requireContext().showToast("Maximize clicked")
        }

        binding.cardGrHistory.setOnClickListener {
            requireContext().showToast("GR History clicked")
        }

        binding.cardSyncStatus.setOnClickListener {
            requireContext().showToast("Sync Status clicked")
        }

        binding.cardLogout.setOnClickListener {
            showLogoutDialog()
        }

        binding.cardSupport.setOnClickListener {
            requireContext().showToast("Support clicked")
        }

        binding.tvViewAll.setOnClickListener {
            requireContext().showToast("View All clicked")
        }

        binding.cardReceiptItem.setOnClickListener {
            requireContext().showToast("Receipt item clicked")
        }

        binding.ivNotifications.setOnClickListener {
            requireContext().showToast("Notifications clicked")
        }
    }

    private fun showLogoutDialog() {
        LogoutDialog.show(
            context = requireContext(),
            onLogoutConfirmed = {
                requireContext().showToast("Logging out...")
                prefsHelper.clearSharedPrefs()
                LoginFragment.navigateTo(parentFragmentManager)
                (requireActivity() as? MainActivity)?.showBottomNavigation(false)
            },
            onCancel = {
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun navigateTo(fragmentManager: FragmentManager, addToBackStack: Boolean = false) {
            val fragment = HomeFragment()
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            if (addToBackStack) {
                transaction.addToBackStack(null)
            }
            transaction.commit()
        }
    }
}
