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
        setUpOnLongClickListener()
        setMenu()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpRecyclerView() {
        noteListAdapter = NoteListAdapter()
        binding.rvNotes.setHasFixedSize(true)
        binding.rvNotes.adapter = noteListAdapter
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

    private fun setUpOnLongClickListener() {
        noteListAdapter.onNoteLongClickListener = {
            viewModel.changeIsFavouriteState(note = it)
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
                menuSearch.isSubmitButtonEnabled = false
                menuSearch.setOnQueryTextListener(this@AllNotesFragment)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
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

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {

        if (newText != null && view != null) {
            /***
             * Check whether view is null because an empty string is sent searchNotesByTopic
             * when element on the list is clicked,
             * at this moment onDestroyView is called and therefore viewLifecycleOwner
             * stops existing, this leads to bags in this particular case when observing LiveData.
             */
            searchNotesByTopic(newText)
        }
        return true
    }

    private fun searchNotesByTopic(noteTopic: String) {
        val searchNote = "%$noteTopic%"
        viewModel.searchNotesByTopic(searchNote).observe(viewLifecycleOwner) {
            noteListAdapter.submitList(it)
        }
    }

    companion object {
        private const val DEFAULT_NOTE_ID = 0
    }
}