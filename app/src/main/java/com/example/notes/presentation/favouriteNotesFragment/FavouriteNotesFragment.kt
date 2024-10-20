package com.example.notes.presentation.favouriteNotesFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.notes.R
import com.example.notes.databinding.FragmentFavouriteNotesBinding
import com.example.notes.domain.entity.Mode
import com.example.notes.presentation.MainActivity
import com.example.notes.presentation.recycleView.NoteListAdapter

class FavouriteNotesFragment : Fragment() {

    private val viewModel: FavouriteNotesViewModel by lazy {
        ViewModelProvider(this)[FavouriteNotesViewModel::class.java]
    }
    private var _binding: FragmentFavouriteNotesBinding? = null
    private val binding: FragmentFavouriteNotesBinding
        get() = _binding ?: throw RuntimeException("FragmentFavouriteNotesBinding == null")
    private lateinit var noteListAdapter: NoteListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteNotesBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        observeViewModel()
        setUpOnLongClickListener()
        setUpOnClickListener()
        popToAllNotesFragment()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).supportActionBar?.title =
            getString(R.string.my_favourites_action_bar)
    }
    private fun setUpRecyclerView() {
        noteListAdapter = NoteListAdapter()
        binding.rvNotes.setHasFixedSize(true)
        binding.rvNotes.adapter = noteListAdapter
    }

    private fun setUpOnClickListener() {
        noteListAdapter.onNoteClickListener = {
            findNavController().navigate(
                FavouriteNotesFragmentDirections.actionFavouriteNotesToNoteFragment(
                    Mode.EDIT, it.id
                )
            )
        }
    }
    private fun setUpOnLongClickListener() {
        noteListAdapter.onNoteLongClickListener = {
            viewModel.changeIsFavouriteState(note = it)
        }
    }
    private fun observeViewModel() {
        viewModel.favouriteNotes.observe(viewLifecycleOwner) {
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
    private fun popToAllNotesFragment() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack()
        }
    }
}