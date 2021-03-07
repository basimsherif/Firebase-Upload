package com.example.sparktest.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sparktest.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.upload_options_layout.*

/**
 * Bottom sheet dialog for choosing upload method
 */
class OptionsBottomSheet : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.upload_options_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    fun setListener(mListenerObj: ItemClickListener?) {
        mListener = mListenerObj
    }

    private fun setUpViews() {
        upload_camera.setOnClickListener {
            dismissAllowingStateLoss()
            mListener?.onItemClick("Camera")

        }

        upload_gallery.setOnClickListener {
            dismissAllowingStateLoss()
            mListener?.onItemClick("Gallery")
        }

        txt_cancel.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

    private var mListener: ItemClickListener? = null

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface ItemClickListener {
        fun onItemClick(item: String)
    }

    companion object {
        @JvmStatic
        fun newInstance(): OptionsBottomSheet {
            return OptionsBottomSheet()
        }
    }
}