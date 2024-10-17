package com.example.notes.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notes.databinding.FragmentNoteBinding


class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding: FragmentNoteBinding
        get() = _binding ?: throw RuntimeException("FragmentNoteBinding")

    private val args by navArgs<NoteFragmentArgs>()
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
        launchAddMode()
      //launchDraft()
        viewModel.close.observe(viewLifecycleOwner) {
            findNavController().navigate(NoteFragmentDirections.actionNoteFragmentToAllNotesFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchAddMode() {
        binding.saveButton.setOnClickListener {
            viewModel.addNote(binding.etTopic.text.toString(), binding.etContent.text.toString())
        }
    }
//    private fun launchDraft(){
//        if (requireActivity().onBackPressedDispatcher.onBackPressed()){
//            viewModel.draftNote(binding.etTopic.text.toString(), binding.etContent.text.toString())
//        }
//    }

}