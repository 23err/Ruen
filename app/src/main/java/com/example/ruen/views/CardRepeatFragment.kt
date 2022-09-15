package com.example.ruen.views

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.ruen.R
import com.example.ruen.databinding.FragmentCardRepeatBinding
import com.example.ruen.viewmodels.CardRepeatViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CardRepeatFragment :
    BaseFragment<FragmentCardRepeatBinding>(FragmentCardRepeatBinding::inflate),
    View.OnClickListener {

    private val viewModel: CardRepeatViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToUIState()
        setClickListeners()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadNextCard()
    }

    private fun subscribeToUIState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { uiState ->
                    updateUIState(uiState)
                }
            }
        }
    }

    private fun updateUIState(uiState: CardRepeatViewModel.UIState) = with(binding) {
        when (uiState) {
            is CardRepeatViewModel.UIState.Card -> setCardUIState(uiState)
            is CardRepeatViewModel.UIState.Empty -> setNoMoreWords()
        }

    }

    private fun setCardUIState(uiState: CardRepeatViewModel.UIState.Card) = with(binding) {
        wordView.text = uiState.card.value
        translationsView.text = uiState.translations?.map { it.value }?.joinToString(", ")
        uiState.repeatIntervals?.forEach {
            when (it.first) {
                CardRepeatViewModel.KnowLevel.DONT_KNOW -> dontKnowView.text =
                    resources.getString(
                        R.string.dont_know,
                        it.second
                    )
                CardRepeatViewModel.KnowLevel.BAD_KNOW -> badKnowView.text =
                    resources.getString(
                        R.string.bad_know,
                        it.second
                    )
                CardRepeatViewModel.KnowLevel.GOOD_KNOW -> goodKnowView.text =
                    resources.getString(
                        R.string.good_know,
                        it.second
                    )
                CardRepeatViewModel.KnowLevel.EXCELLENT_KNOW -> excellentKnowView.text =
                    resources.getString(
                        R.string.excellent_know,
                        it.second
                    )
            }
        }
        noCardView.visibility = View.GONE
    }

    private fun setNoMoreWords() = with(binding) {
        wordView.text = ""
        translationsView.text = ""
        noCardView.visibility = View.VISIBLE
    }

    private fun setClickListeners() = with(binding) {
        arrayOf(dontKnowView, badKnowView, goodKnowView, excellentKnowView).forEach {
            it.setOnClickListener(this@CardRepeatFragment)
        }
    }

    override fun onClick(view: View) {
        val answer = when (view.id) {
            R.id.dont_know_view -> CardRepeatViewModel.KnowLevel.DONT_KNOW
            R.id.bad_know_view -> CardRepeatViewModel.KnowLevel.BAD_KNOW
            R.id.good_know_view -> CardRepeatViewModel.KnowLevel.GOOD_KNOW
            R.id.excellent_know_view -> CardRepeatViewModel.KnowLevel.EXCELLENT_KNOW
            else -> throw IllegalArgumentException("Button not supported")
        }
        viewModel.chooseLevelKnow(answer)
    }
}