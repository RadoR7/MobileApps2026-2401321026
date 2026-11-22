package com.example.fittrack.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.fittrack.FitTrackApp
import com.example.fittrack.viewmodel.FitViewModel

// All screens share the activity's ViewModel (one observable source of truth).
internal fun Fragment.sharedFitViewModel(): Lazy<FitViewModel> =
    activityViewModels {
        FitViewModel.Factory((requireActivity().application as FitTrackApp).repository)
    }
