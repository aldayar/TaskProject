package com.example.taskproject.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.taskproject.databinding.FragmentAddBinding

class AddFragment(private val listener: OnNoteAddedListener) : DialogFragment() {
    private lateinit var binding: FragmentAddBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        binding = FragmentAddBinding.inflate(inflater)

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)
            .setTitle("Add new note")
            .setPositiveButton("Add") { _, _ ->
                val title = binding.etTitle.text.toString()
                val desc = binding.etDesc.text.toString()
                if (title.isNotBlank() && desc.isNotBlank()) {
                    listener.onNoteAdded(title, desc)
                }
            }
            .setNegativeButton("Cancel") { _, _ ->
                dialog?.cancel()
            }
        return builder.create()
    }

    interface OnNoteAddedListener {
        fun onNoteAdded(title: String, desc: String)
    }
}