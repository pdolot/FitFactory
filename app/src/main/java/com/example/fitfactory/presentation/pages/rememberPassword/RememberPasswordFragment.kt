package com.example.fitfactory.presentation.pages.rememberPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.fitfactory.R
import com.example.fitfactory.data.models.app.StateComplete
import com.example.fitfactory.data.models.app.StateError
import com.example.fitfactory.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_remember_password.*

class RememberPasswordFragment : BaseFragment() {

    private lateinit var viewModel: RememberPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RememberPasswordViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_remember_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        viewModel.addValidators(this)
        viewModel.callResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is StateError -> rememberPassword.onError(it.message)
                is StateComplete -> rememberPassword.onSuccess(it.message ?: "")
            }
            rememberPassword.stop()
        }
        )
    }

    private fun setListeners() {
        rememberPassword.setOnClickListener {
            if (viewModel.validate()){
                viewModel.rememberPassword(rememberPasswordFragment_userEmail.text.toString().trim())
                rememberPassword.startAnim()
            }
        }
    }

    override fun flexibleViewEnabled() = false
    override fun paddingTopEnabled() = false
    override fun topBarTitle() = null
    override fun topBarEnabled() = false
    override fun backButtonEnabled(): Boolean = true

}
