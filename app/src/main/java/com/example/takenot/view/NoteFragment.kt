package com.example.takenot.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.takenot.databinding.FragmentNoteBinding
import com.example.takenot.model.Note
import com.example.takenot.roomdb.NoteDAO
import com.example.takenot.roomdb.NoteDatabasebase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var db : NoteDatabasebase
    private lateinit var noteDao :NoteDAO
    private val mDisposable = CompositeDisposable()
    private var selectedNote :Note? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(requireContext(),NoteDatabasebase::class.java, name = "Notes").build()
        noteDao = db.noteDAO()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingSaveButton.setOnClickListener { save(it) }
        binding.floatingDeleteButton.setOnClickListener { delete(it) }
        binding.floatingBackButton.setOnClickListener { back(it) }

        arguments?.let {
            val info = NoteFragmentArgs.fromBundle(it).info
            if (info == "new"){
                selectedNote = null
                binding.floatingDeleteButton.isEnabled = false
                binding.floatingSaveButton.isEnabled = true
            }else{
                binding.floatingDeleteButton.isEnabled=true
                binding.floatingSaveButton.isEnabled=true
                val id = NoteFragmentArgs.fromBundle(it).id

                mDisposable.add(
                    noteDao.findById(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::handleResponse)
                )


            }




        }

    }

    private fun handleResponse(note: Note) {
        binding.editTextTextMultiLine4.setText(note.Notes)
        selectedNote = note

    }

    fun save(view: View){
        val notes = binding.editTextTextMultiLine4.text.toString().trim()
        val notess = Note(notes)
        mDisposable.add(
            noteDao.insert(notess)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponseForInsert)
        )



    }
    private fun handleResponseForInsert() {
        val action = NoteFragmentDirections.actionNoteFragmentToListFragment()
        Navigation.findNavController(requireView()).navigate(action)
    }

    fun delete(view: View){
        if (selectedNote !=null) {
            mDisposable.add(
                noteDao.delete(note = selectedNote!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResponseForInsert)
            )
        }
    }

    fun back(view: View){
        val action =NoteFragmentDirections.actionNoteFragmentToListFragment()
        Navigation.findNavController(view).navigate(action)

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }


}