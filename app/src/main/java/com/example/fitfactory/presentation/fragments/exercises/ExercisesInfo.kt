package com.example.fitfactory.presentation.fragments.exercises

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.fitfactory.R
import com.example.fitfactory.presentation.base.BaseFragment
import com.example.fitfactory.utils.SpanTextUtil
import kotlinx.android.synthetic.main.fragment_exercises_info.*

class ExercisesInfo : BaseFragment() {


    private val viewModel by lazy { ExercisesInfoViewModel() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_exercises_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        flexibleLayout?.isViewEnable = false
        topBar?.setTitle("ćwiczenia")
        setPaddingTop(view)
        SpanTextUtil(context ?: return).apply {
            setSpanOnTextView(firstInfo, "QR", R.color.primaryLight)
            setSpanOnTextView(secondInfo, "kontuzji", R.color.negativeMedium)
        }
    }
}
