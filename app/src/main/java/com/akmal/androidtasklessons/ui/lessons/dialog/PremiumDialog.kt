package com.akmal.androidtasklessons.ui.lessons.dialog

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import by.kirich1409.viewbindingdelegate.viewBinding
import com.akmal.androidtasklessons.R
import com.akmal.androidtasklessons.databinding.DialogPurchaseBinding
import com.akmal.androidtasklessons.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PremiumDialog : BottomSheetDialogFragment(R.layout.dialog_purchase) {
    private val binding: DialogPurchaseBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.go.setOnClickListener {
            Constants.isPurchased = true
            setFragmentResult("unlocked", bundleOf("unlocked" to true))
            this.dismiss()
        }
    }
}