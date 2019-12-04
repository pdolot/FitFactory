package com.example.fitfactory.presentation.fragments.exercises.scannerView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.fitfactory.R
import com.example.fitfactory.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_scanner.*

class ScannerFragment : BaseFragment() {

    private val viewModel by lazy { ScannerViewModel() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_scanner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        flexibleLayout?.isViewEnable = false
//        topBar?.setTitle("Zeskanuj kod")
//        setPaddingTop(view)

        torch.setOnClickListener {
            viewModel.isTorchEnabled = !viewModel.isTorchEnabled
            setTorch(viewModel.isTorchEnabled)
        }
        viewModel.barcode.observe(viewLifecycleOwner, Observer {
            if (!it.isNullOrEmpty()) findNavController().navigate(R.id.exerciseFragment)
        })
    }


    override fun onResume() {
        super.onResume()
        viewModel.startCamera(activity ?: return, firePreview, fireFaceOverlay)
        viewModel.setTorch()
        setTorch(viewModel.isTorchEnabled)
    }


    private fun setTorch(isEnabled: Boolean) {
        torch.setImageResource(if (isEnabled) R.drawable.torch_on_icon else R.drawable.torch_off_icon)
    }

    /** Stops the camera.  */
    override fun onPause() {
        super.onPause()
        firePreview?.release()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        firePreview?.stop()
        viewModel.barcode.postValue(null)
    }

    override fun flexibleViewEnabled() = false
    override fun paddingTopEnabled() = true
    override fun topBarTitle() = getString(R.string.scanCode)
    override fun topBarEnabled() = true
    override fun backButtonEnabled(): Boolean = true

}
