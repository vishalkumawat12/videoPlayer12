package com.example.videoplayers2.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.videoplayers2.R

class FolderAdapter(
    private var onClickListener: OnCLickListener
) : RecyclerView.Adapter<FolderAdapter.ViewHolder>() {

    private var folderName = ArrayList<String>()

    //    private var videoModels = ArrayList<VideoModel>()
    private var videoMapList = HashMap<String, Int>()


    //    fun updateList(folderList: ArrayList<String>, videoList: ArrayList<VideoModel>) {
//        folderName.clear()
//        folderName = folderList
//
//        videoModels.clear()
//        videoModels = videoList
//
//        notifyDataSetChanged()
//    }

    fun getFolderName() {
        folderName.clear()
        videoMapList.forEach { (key) ->
            folderName.add(key)
        }
        folderName.sort()
    }

    fun updateList(map: HashMap<String, Int>) {
        videoMapList.clear()
        videoMapList = map
        getFolderName()
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var folderName: TextView
        var conter: TextView

        init {
            folderName = itemView.findViewById(R.id.folName)
            conter = itemView.findViewById(R.id.child)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.folder_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val folder = folderName[position]
        Log.d("TAG", "onBindViewHolder: folder $folder ")
        val index = folderName[position].lastIndexOf("/")
        val folname = folderName[position].substring(index + 1)
        holder.folderName.text = folname
        holder.conter.text = videoMapList.get(folder).toString()
        holder.itemView.setOnClickListener {
            onClickListener.onFolderClick(folname)
        }
    }

    override fun getItemCount(): Int {
        return folderName.size
    }


//    private fun NumberOfFiles(name: String?): Int {
//        var countFile = 0
//        for (v in videoModels) {
//            if (v.path.substring(0, v.path.lastIndexOf("/")).endsWith(name!!)) {
//                countFile++
//            }
//        }
//        return countFile
//    }

    interface OnCLickListener {
        fun onFolderClick(folname: String)
    }
}