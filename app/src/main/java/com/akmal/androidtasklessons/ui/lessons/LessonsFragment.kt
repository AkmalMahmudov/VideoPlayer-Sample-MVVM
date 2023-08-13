package com.akmal.androidtasklessons.ui.lessons

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.akmal.androidtasklessons.R
import com.akmal.androidtasklessons.databinding.FragmentLessonsBinding
import com.akmal.androidtasklessons.ui.lessons.adapter.LessonsAdapter
import com.akmal.androidtasklessons.ui.lessons.model.LessonsList
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LessonsFragment : Fragment(R.layout.fragment_lessons) {
    private val binding by viewBinding(FragmentLessonsBinding::bind)
    private val viewModel: LessonsViewModel by viewModels()
    private val adapter: LessonsAdapter by lazy { LessonsAdapter() }
    private val navController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupObservers()
        setFragmentResultListener("unlocked") { _, _ -> adapter.notifyDataSetChanged() }
    }

    private fun setupViews() = with(binding) {
        rv.adapter = this@LessonsFragment.adapter
        adapter.setOnItemClickListener { item, isLocked, list ->
            if (isLocked) {
                navController.navigate(LessonsFragmentDirections.actionLessonsFragmentToPremiumDialog())
            } else {
                navController.navigate(LessonsFragmentDirections.actionLessonsFragmentToVideoFragment(LessonsList(list), item))
            }
        }
    }

    private fun setupObservers() {
        viewModel.offers.onEach {
            adapter.submitList(it)
        }.launchIn(lifecycleScope)

        viewModel.messageFlow.onEach {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }.launchIn(lifecycleScope)

        viewModel.progressFlow.onEach {
            binding.progress.isVisible = it
        }.launchIn(lifecycleScope)
    }
}