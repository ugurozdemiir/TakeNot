package com.example.takenot.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.takenot.adapter.NotesAdapter
import com.example.takenot.databinding.FragmentListBinding
import com.example.takenot.model.Note
import com.example.takenot.roomdb.NoteDAO
import com.example.takenot.roomdb.NoteDatabasebase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var db : NoteDatabasebase
    private lateinit var noteDao : NoteDAO
    private val mDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerlauncher()

        db = Room.databaseBuilder(requireContext(),NoteDatabasebase::class.java, name = "Notes").build()
        noteDao = db.noteDAO()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingActionButton.setOnClickListener { newAdd(it) }
        binding.noteRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        getData()
    }

    private fun getData() {
        mDisposable.add(
            noteDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse)
        )

    }
    private fun handleResponse(notes: List<Note>) {
        val adapter = NotesAdapter(notes)
        binding.noteRecyclerView.adapter = adapter

    }


    fun newAdd(view: View) {

        val action =ListFragmentDirections.actionListFragmentToNoteFragment(info = "",id=0)
        Navigation.findNavController(view).navigate(action)
    }

    private fun registerlauncher(){

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDisposable.clear()
    }


}