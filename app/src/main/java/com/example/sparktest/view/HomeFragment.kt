package com.example.sparktest.view

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.sparktest.R
import com.example.sparktest.databinding.HomeFragmentBinding
import com.example.sparktest.util.*
import com.google.android.material.snackbar.Snackbar
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.home_fragment.*


/**
 * Fragment class for home screen
 */
@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.home_fragment), OnListItemClickListener,
    OptionsBottomSheet.ItemClickListener {
    private val viewModel: HomeViewModel by viewModels()
    private val PICK_IMAGE_REQUEST = 10011
    private val PICK_CAMERA_IMAGE_REQUEST = 10012
    private lateinit var fileUri: Uri

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = HomeFragmentBinding.bind(view)
        binding.homeViewModel = viewModel
        binding.homeFragment = this
        viewModel.imageAdapter.onClick = this
        imagesRecyclerView.addItemDecoration(SpacingItemDecorator(10))
        viewModel.synImages()
        observeData()
    }

    /**
     * Observe live data to update UI
     */
    private fun observeData() {
        //Observing livedata for sync gallery task
        viewModel.getSyncLiveData().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    progressBar2.visibility = View.GONE
                    if (!it.data.isNullOrEmpty()) {
                        emptyView.visibility = View.GONE
                        viewModel.imageAdapter.setAdapterData(it.data.sortedBy { image -> image.imageName })
                    } else emptyView.visibility = View.VISIBLE
                }
                Resource.Status.ERROR ->
                    Snackbar.make(requireView(), it.message!!, Snackbar.LENGTH_LONG).show()

                Resource.Status.LOADING ->
                    progressBar2.visibility = View.VISIBLE
            }
        })

        //Observing livedata for upload task
        viewModel.getUploadLiveData().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    progressBar2.visibility = View.GONE
                    viewModel.refreshImages()
                }
                Resource.Status.ERROR ->
                    Snackbar.make(requireView(), it.message!!, Snackbar.LENGTH_LONG).show()

                Resource.Status.LOADING ->
                    progressBar2.visibility = View.VISIBLE
            }
        })
    }

    override fun onListItemClick(view: View, obj: Any) {
        //Do Nothing
    }

    /**
     * Button click for upload using binding
     */
    fun onUploadClick() {
        var optionSheet = OptionsBottomSheet.newInstance()
        optionSheet.setListener(this)
        childFragmentManager.let {
            optionSheet.apply {
                show(it!!, tag)
            }
        }
    }

    /**
     * Check result after gallery/camera/crop intent
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    //It's gallery intent
                    if (data == null || data.data == null) return
                    val uri = data.data
                    uri?.let { launchCropIntent(it) }
                }
                PICK_CAMERA_IMAGE_REQUEST -> {
                    //It's camera intent
                    fileUri?.let { launchCropIntent(it) }
                }
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    //It's crop intent
                    val result = CropImage.getActivityResult(data)
                    val resultUri = result.uri
                    resultUri.let { viewModel.uploadToFirebase(it) }
                }
            }
        }
    }

    /**
     * Click listener for bottom sheet menu while taping upload button
     */
    override fun onItemClick(param: String) {
        when (param) {
            "Camera" -> {
                checkCameraPermission()
            }
            "Gallery" -> {
                checkStoragePermission()
            }
        }
    }

    /**
     * Launch crop intent using 3rd party library
     */
    private fun launchCropIntent(sourceUri: Uri) {
        CropImage.activity(sourceUri)
            .start(requireContext(), this)
    }

    /**
     * Handle camera permission
     */
    private fun checkCameraPermission() {
        handlePermission(
            AppPermission.ACCESS_CAMERA,
            onGranted = {
                launchCamera()
            },
            onDenied = {
                requestPermission(it)
            },
            onRationaleNeeded = {
                showAlert(
                    getString(R.string.label_alert),
                    getString(R.string.camera_permission),
                    getString(R.string.label_ask_again),
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogInterface.dismiss()
                        requestPermission(it)
                    },
                    getString(R.string.label_never_mind),
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogInterface.dismiss()
                    }
                )
            }
        )
    }

    /**
     * Handle storage permission
     */
    private fun checkStoragePermission() {
        handlePermission(
            AppPermission.WRITE_STORAGE,
            onGranted = {
                launchGallery()
            },
            onDenied = {
                requestPermission(it)
            },
            onRationaleNeeded = {
                showAlert(
                    getString(R.string.label_alert),
                    getString(R.string.storage_permission),
                    getString(R.string.label_ask_again),
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogInterface.dismiss()
                        requestPermission(it)
                    },
                    getString(R.string.label_never_mind),
                    DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogInterface.dismiss()
                    }
                )
            }
        )
    }

    /**
     * Launch camera intent
     */
    private fun launchCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        fileUri = Utils.createURI(requireContext())
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        startActivityForResult(cameraIntent, PICK_CAMERA_IMAGE_REQUEST)
    }

    /**
     * Launch gallery intent
     */
    private fun launchGallery() {
        val galleryIntent = Intent()
        galleryIntent.type = "image/*"
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(galleryIntent, getString(R.string.select_photo)),
            PICK_IMAGE_REQUEST
        )
    }

    /**
     * Handle permission request result
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        handlePermissionsResult(requestCode, permissions, grantResults,
            onPermissionGranted = {
                when (requestCode) {
                    42 -> launchCamera()
                    44 -> launchGallery()
                }
            },
            onPermissionDenied = {
                when (requestCode) {
                    42 -> {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.camera_permission),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    44 -> {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.storage_permission),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            },
            onPermissionDeniedPermanently = {

            }
        )
    }

    /**
     * Alert for permission rationale
     */
    private fun showAlert(
        title: String,
        message: String,
        positiveBtnText: String,
        positiveBtnListener: DialogInterface.OnClickListener,
        negativeBtnText: String,
        negativeBtnListener: DialogInterface.OnClickListener?
    ) {
        val alertBuilder =
            AlertDialog.Builder(requireContext())
        alertBuilder.setCancelable(true)
        alertBuilder.setTitle(title)
        alertBuilder.setMessage(message)
        alertBuilder.setPositiveButton(positiveBtnText, positiveBtnListener)
        if (negativeBtnText.isNotEmpty()) {
            alertBuilder.setNegativeButton(
                negativeBtnText,
                negativeBtnListener
            )
        }
        val alert = alertBuilder.create()
        alert.show()
    }
}