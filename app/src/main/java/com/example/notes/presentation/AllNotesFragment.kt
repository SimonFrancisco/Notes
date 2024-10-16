package com.example.notes.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.notes.databinding.FragmentAllNotesBinding
import com.example.notes.domain.entity.Mode
import com.example.notes.presentation.recycleView.NoteListAdapter

class AllNotesFragment : Fragment() {
    private var _binding: FragmentAllNotesBinding? = null
    private val binding: FragmentAllNotesBinding
        get() = _binding ?: throw RuntimeException("FragmentAllNotesBinding == null")

    private val viewModel: AllNotesViewModel by lazy {
        ViewModelProvider(this)[AllNotesViewModel::class.java]
    }
    private lateinit var noteListAdapter:NoteListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllNotesBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        viewModel.notes.observe(viewLifecycleOwner){
            noteListAdapter.submitList(it)
        }
        binding.buttonAddShopItem.setOnClickListener {
            launchNoteFragment(mode = Mode.ADD, 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchNoteFragment(mode: Mode, noteId: Int) {
        when (mode) {
            Mode.ADD -> {
                findNavController().navigate(
                    AllNotesFragmentDirections.actionAllNotesFragmentToNoteFragment(
                        mode,
                        DEFAULT_NOTE_ID
                    )
                )
            }

            Mode.EDIT -> {
                findNavController().navigate(
                    AllNotesFragmentDirections.actionAllNotesFragmentToNoteFragment(mode, noteId)
                )
            }
        }

    }
    private fun setUpRecyclerView(){
        noteListAdapter = NoteListAdapter()
        binding.rvNotes.adapter = noteListAdapter
    }

    companion object {
        private const val DEFAULT_NOTE_ID = 0
    }

}