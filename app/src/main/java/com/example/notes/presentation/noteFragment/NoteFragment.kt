package com.example.notes.presentation.noteFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notes.databinding.FragmentNoteBinding
import com.example.notes.domain.entity.Mode


class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding: FragmentNoteBinding
        get() = _binding ?: throw RuntimeException("FragmentNoteBinding == null")

    private val args by navArgs<NoteFragmentArgs>()
    private lateinit var mode: Mode

    private val viewModel: NoteViewModel by lazy {
        ViewModelProvider(this)[NoteViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mode = args.mode
        launchMode()
        launchAllNotesFragment()
        saveDraft()
    }

    private fun launchAllNotesFragment() {
        viewModel.closeScreen.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchMode() {
        when (mode) {
            Mode.ADD -> launchAddMode()
            Mode.EDIT -> launchEditMode()
        }
    }

    private fun launchAddMode() {
        binding.saveButton.setOnClickListener {
            viewModel.addNote(
                binding.etTopic.text.toString().trim(),
                binding.etContent.text.toString().trim()
            )
        }
    }

    private fun launchEditMode() {
        viewModel.getNote(args.noteId)
        viewModel.note.observe(viewLifecycleOwner) {
            binding.etTopic.setText(it.topic)
            binding.etContent.setText(it.content)
        }
        binding.saveButton.setOnClickListener {
            viewModel.editNote(
                binding.etTopic.text.toString().trim(),
                binding.etContent.text.toString().trim(),
            )
        }
    }

    private fun saveDraft() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.draftNote(
                binding.etTopic.text.toString().trim(),
                binding.etContent.text.toString().trim()
            )
        }
    }
}