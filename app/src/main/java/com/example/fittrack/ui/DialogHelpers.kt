package com.example.fittrack.ui

import android.app.AlertDialog
import android.view.View
import androidx.fragment.app.Fragment
import com.example.fittrack.R
import com.example.fittrack.util.attachDatePicker
import com.example.fittrack.util.value
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

internal fun View.field(id: Int): TextInputEditText = findViewById(id)

internal fun Fragment.formDialog(
    title: String,
    layout: Int,
    prefill: (View) -> Unit,
    save: (View) -> Boolean
) {
    val form = layoutInflater.inflate(layout, null)
    prefill(form)
    val dialog = MaterialAlertDialogBuilder(requireContext())
        .setTitle(title)
        .setView(form)
        .setNegativeButton("Cancel", null)
        .setPositiveButton("Save", null)
        .create()
    dialog.setOnShowListener {
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            if (save(form)) dialog.dismiss()
        }
    }
    dialog.show()
}

internal fun Fragment.confirmDelete(message: String, delete: () -> Unit) {
    MaterialAlertDialogBuilder(requireContext())
        .setTitle("Delete entry?")
        .setMessage(message)
        .setNegativeButton("Cancel", null)
        .setPositiveButton("Delete") { _, _ -> delete() }
        .show()
}

internal fun View.initDate(id: Int, existing: String) {
    field(id).setText(existing)
    field(id).attachDatePicker(context)
}
