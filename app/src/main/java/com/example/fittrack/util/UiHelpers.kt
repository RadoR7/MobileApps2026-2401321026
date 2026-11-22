package com.example.fittrack.util

import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun TextInputEditText.value(): String = text?.toString()?.trim().orEmpty()
fun Fragment.toast(message: String) = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

fun TextInputEditText.attachDatePicker(context: Context) {
    isFocusable = false
    isClickable = true
    setOnClickListener {
        val start = runCatching { LocalDate.parse(value()) }.getOrDefault(LocalDate.now())
        DatePickerDialog(context, { _, year, month, day ->
            setText(LocalDate.of(year, month + 1, day).toString())
        }, start.year, start.monthValue - 1, start.dayOfMonth).show()
    }
}

fun prettyDate(value: String): String = runCatching {
    LocalDate.parse(value).format(DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH))
}.getOrDefault(value)

fun View.visible(show: Boolean) {
    visibility = if (show) View.VISIBLE else View.GONE
}
