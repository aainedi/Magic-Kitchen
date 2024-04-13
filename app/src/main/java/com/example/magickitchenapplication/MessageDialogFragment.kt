package com.example.magickitchenapplication

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class MessageDialogFragment : DialogFragment() {

    private var onDialogButtonClickListener: (() -> Unit)? = null

    fun setOnDialogButtonClickListener(listener: () -> Unit) {
        onDialogButtonClickListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Scan the ingredients that you would like to identify")
                .setPositiveButton("OK") { dialog, id ->
                    onDialogButtonClickListener?.invoke()
                    dismiss()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
