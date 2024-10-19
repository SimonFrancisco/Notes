package com.example.notes.presentation.allNotesFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.R
import com.example.notes.databinding.FragmentAllNotesBinding
import com.example.notes.domain.entity.Mode
import com.example.notes.presentation.recycleView.NoteListAdapter

class AllNotesFragment : Fragment(), SearchView.OnQueryTextListener {
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
        setMenu()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Log.d("onDestroyView","ViewDestroyed")
        _binding = null
    }

    private fun setUpRecyclerView() {
        noteListAdapter = NoteListAdapter()
        binding.rvNotes.adapter = noteListAdapter
        setUpSwipeListener(binding.rvNotes)
    }

    private fun observeViewModel() {
        viewModel.notes.observe(viewLifecycleOwner) {
            with(binding) {
                if (it.isEmpty()) {
                    rvNotes.visibility = View.GONE
                    cvWarning.visibility = View.VISIBLE
                } else {
                    rvNotes.visibility = View.VISIBLE
                    cvWarning.visibility = View.GONE
                }
            }
            noteListAdapter.submitList(it)
        }

    }

    private fun addNote() {
        binding.buttonAddShopItem.setOnClickListener {
            launchNoteFragment()
        }
    }

    private fun setUpOnClickListener() {
        noteListAdapter.onNoteClickListener = {
            launchNoteFragment(Mode.EDIT, it.id)
        }
    }

    private fun setMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.first_sreen_menu, menu)
                val menuSearch = menu.findItem(
                    R.id.menu_search
                ).actionView as SearchView
                menuSearch.isSubmitButtonEnabled = true

                menuSearch.setOnQueryTextListener(this@AllNotesFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
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

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchNotesByTopic(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null) {
            searchNotesByTopic(newText)
            //Log.d("searchNotesByTopic", newText)

        }
        return true
    }

    private fun searchNotesByTopic(noteTopic: String) {
        //Log.d("searchNotesByTopic", noteTopic)
        val searchNote = "%$noteTopic%"
        if (view != null) {
            //Log.d("searchNotesByTopic", noteTopic)
            viewModel.searchNotesByTopic(searchNote).observe(viewLifecycleOwner) {
                noteListAdapter.submitList(it)
            }
        }
    }

    companion object {
        private const val DEFAULT_NOTE_ID = 0
    }
}