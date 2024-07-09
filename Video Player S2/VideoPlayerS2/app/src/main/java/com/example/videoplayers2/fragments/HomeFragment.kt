package com.example.videoplayers2.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.videoplayers2.R
import com.example.videoplayers2.adapters.FolderAdapter
import com.example.videoplayers2.databinding.FragmentHomeBinding
import com.example.videoplayers2.viewmodels.ViewModel

class HomeFragment : Fragment(), FolderAdapter.OnCLickListener {

    private lateinit var binding: FragmentHomeBinding
    val viewModel: ViewModel by activityViewModels()
    var folderAdapter: FolderAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        folderAdapter = FolderAdapter(this)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.recyclerView.adapter = folderAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFolderNameArrayList().observe(viewLifecycleOwner) {
            Log.d("TAG", "onViewCreated: observer called")
            viewModel.getFolderNameArrayList().value?.let {
                folderAdapter?.updateList(it) }
        }
    }

    override fun onFolderClick(folname: String) {
        viewModel.folderSending = true
        viewModel.setTempFolderName(folname)
        findNavController().navigate(R.id.action_homeFragment_to_videoFragment)
    }
}