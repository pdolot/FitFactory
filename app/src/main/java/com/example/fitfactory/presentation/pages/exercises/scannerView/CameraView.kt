package com.example.fitfactory.presentation.pages.exercises.scannerView

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.fitfactory.R
import com.example.fitfactory.constants.RequestCode
import com.example.fitfactory.presentation.base.BaseFragment
import com.example.fitfactory.presentation.pages.exercises.exercise.ExerciseFragmentDirections
import com.otaliastudios.cameraview.controls.Facing
import kotlinx.android.synthetic.main.fragment_camera_view.*

class CameraView : BaseFragment() {


    private val viewModel by lazy { CameraViewViewModel() }
    private var scannerMeshHeight = 0f
    private var anim: ViewPropertyAnimator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_camera_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isCameraPermissionGranted()) {
            startCamera()
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), RequestCode.CAMERA_REQUEST_CODE)
        }

        viewModel.result.observe(viewLifecycleOwner, Observer {
            println(it)
            findNavController().navigate(ExerciseFragmentDirections.toExerciseFragment(it))
            viewModel.isAnalyzingEnabled = true
        })


        view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                scannerMeshHeight = scannerMesh.measuredHeight.toFloat()
                scannerMesh.translationY = scannerMeshHeight
                animateMeshToTop()
                scannerMesh.visibility = View.VISIBLE
                viewModel.frameRect = Rect(
                    frameView.x.toInt(),
                    frameView.y.toInt(),
                    (frameView.x + frameView.measuredWidth).toInt(),
                    (frameView.y + frameView.measuredHeight).toInt()
                )
            }
        })
    }


    private fun startCamera() {
        cameraView.facing = Facing.BACK
        cameraView.addFrameProcessor(viewModel)
        cameraView.setLifecycleOwner(viewLifecycleOwner)
    }

    private fun animateMeshToTop() {
        anim = scannerMesh.animate().apply {
            translationY(-scannerMeshHeight * 2)
            duration = 3000
            interpolator = FastOutSlowInInterpolator()
            withEndAction {
                animateMeshToBottom()
                scannerMesh.rotation = 180f
            }
            start()
        }
    }

    private fun animateMeshToBottom() {
        anim = scannerMesh.animate().apply {
            translationY(scannerMeshHeight)
            duration = 3000
            interpolator = FastOutSlowInInterpolator()
            withEndAction {
                animateMeshToTop()
                scannerMesh.rotation = 360f
            }
            start()
        }

    }

    private fun isCameraPermissionGranted(): Boolean {
        val selfPermission =
            ContextCompat.checkSelfPermission(context ?: return false, Manifest.permission.CAMERA)
        return selfPermission == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == RequestCode.CAMERA_REQUEST_CODE) {
            if (isCameraPermissionGranted()) {
                startCamera()
            } else {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        anim?.cancel()
        super.onDestroyView()
    }

    override fun flexibleViewEnabled() = false
    override fun paddingTopEnabled() = true
    override fun topBarTitle() = getString(R.string.scanCode)
    override fun topBarEnabled() = true
    override fun backButtonEnabled(): Boolean = true
}
