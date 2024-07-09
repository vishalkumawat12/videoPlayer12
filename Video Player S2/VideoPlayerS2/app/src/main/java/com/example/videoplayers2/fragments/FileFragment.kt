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
import com.example.videoplayers2.adapters.VideoAdapter
import com.example.videoplayers2.databinding.FragmentFileBinding
import com.example.videoplayers2.models.VideoModel
import com.example.videoplayers2.viewmodels.ViewModel

class FileFragment : Fragment(), VideoAdapter.OnCLickListener {

    lateinit var binding: FragmentFileBinding
    var videomodel = ArrayList<VideoModel>()
    var videoAdapter: VideoAdapter? = null
    private val viewModel: ViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFileBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        videoAdapter = VideoAdapter(requireContext(), this)
        recyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerView.adapter = videoAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getVideoArrayList().observe(viewLifecycleOwner) {
            Log.d("TAG", "onViewCreated: observer called")
            videomodel = it
            videoAdapter?.updateList(videomodel)
        }

    }

    override fun onFileClick(
        position: Int,
        videoModelArrayList: ArrayList<VideoModel>
    ) {
        Log.d("TAG", "onFileClick: ")
        viewModel.fileSending = true
        val bundle = Bundle()
        bundle.putInt("position", position)
        bundle.putParcelableArrayList("videoList", videoModelArrayList)
        findNavController().navigate(R.id.action_fileFragment_to_playerActivity2, bundle)
    }

}