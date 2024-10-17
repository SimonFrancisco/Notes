package com.example.notes.presentation.allNotesFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var noteListAdapter: NoteListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        observeViewModel()
        addNote()
        setUpOnClickListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        viewModel.notes.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.rvNotes.visibility = View.GONE
                binding.tvWarning.visibility = View.VISIBLE
            } else {
                binding.rvNotes.visibility = View.VISIBLE
                binding.tvWarning.visibility = View.GONE
            }
            noteListAdapter.submitList(it)
        }

    }

    private fun addNote() {
        binding.buttonAddShopItem.setOnClickListener {
            launchNoteFragment()
        }
    }
    private fun launchNoteFragment(mode: Mode = Mode.ADD, noteId: Int = DEFAULT_NOTE_ID) {
        when (mode) {
            Mode.ADD -> {
                findNavController().navigate(
                    AllNotesFragmentDirections.actionAllNotesFragmentToNoteFragment(
                        mode,
                        noteId
                    )
                )
            }
            Mode.EDIT -> {
                findNavController().navigate(
                    AllNotesFragmentDirections.actionAllNotesFragmentToNoteFragment(
                        mode,
                        noteId
                    )
                )
            }
        }
    }

    private fun setUpRecyclerView() {
        noteListAdapter = NoteListAdapter()
        binding.rvNotes.adapter = noteListAdapter
        setUpSwipeListener(binding.rvNotes)
    }

    private fun setUpSwipeListener(recyclerView: RecyclerView) {
        val callBack = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val note = noteListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteNote(note)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun setUpOnClickListener() {
        noteListAdapter.onNoteClickListener = {
            launchNoteFragment(Mode.EDIT, it.id)
        }
    }

    companion object {
        private const val DEFAULT_NOTE_ID = 0
    }

}