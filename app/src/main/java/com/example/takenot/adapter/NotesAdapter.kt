package com.example.takenot.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.takenot.model.Note
import androidx.navigation.Navigation
import com.example.takenot.databinding.RecyclerRowBinding
import com.example.takenot.view.ListFragmentDirections

class NotesAdapter(var NoteList: List<Note>) : RecyclerView.Adapter<NotesAdapter.NoteHolder>() {

    class NoteHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val recyclerRowBinding: RecyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteHolder(recyclerRowBinding)
    }

    override fun onBindViewHolder(holder: NotesAdapter.NoteHolder, position: Int) {
        holder.binding.recyclerViewTextView.text = NoteList[position].Notes
        holder.itemView.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToNoteFragment(info = "old", id = NoteList[position].id)
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return NoteList.size
    }
}

