package com.example.videoplayers2.viewmodels

import android.app.Application
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.videoplayers2.models.VideoModel

class ViewModel(private val application: Application) : AndroidViewModel(application) {

    private var tempFolderName: String? = null
    private var position: Int = 0
    var fileSending = false
    var folderSending = false

    private var videoArrayList = MutableLiveData<ArrayList<VideoModel>>()
    private var folderNameArrayList = MutableLiveData<HashMap<String, Int>>()

    init {
        Log.e("viewModel", "ViewModel created ")
    }


    fun setTempFolderName(name: String) {
        Log.d("TAG", "setTempFolderName: name $name")
        tempFolderName = name
    }

    fun getTempFolderName(): String? {
        Log.d("TAG", "getTempFolderName: ")
        return tempFolderName
    }

    fun setTempPosition(pos: Int) {
        Log.d("TAG", "setTempPosition: pos $pos ")
        position = pos
        Log.d("TAG", "setTempPosition: position $position ")
    }

    fun getTempPosition(): Int {
        Log.d("TAG", "getTempPosition: position $position ")
        return position
    }

    fun setVideoArrayList(list: ArrayList<VideoModel>) {
        videoArrayList.value?.clear()
        videoArrayList.value = list
    }

    fun getVideoArrayList(): MutableLiveData<ArrayList<VideoModel>> {
        Log.d("TAG", "getVideoArrayList: ")
        return videoArrayList
    }

    fun setFolderNameArrayList(list: HashMap<String, Int>) {
        Log.d("TAG", "setFolderNameArrayList: ")
        folderNameArrayList.value?.clear()
        folderNameArrayList.value = list
    }

    fun getFolderNameArrayList(): MutableLiveData<HashMap<String, Int>> {
        Log.d("TAG", "getFolderNameArrayList: ")
        return folderNameArrayList
    }

    fun getAllVideoArrayList() {
        Log.d("TAG", "getAllVideoArrayList: ")
        val videoModelArrayListTemp = ArrayList<VideoModel>()
        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.DISPLAY_NAME
        )
        val cursor = application.contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id = cursor.getString(0)
                val path = cursor.getString(1)
                val title = cursor.getString(2)
                val size = cursor.getString(3)
                val dateAdded = cursor.getString(4)
                val duration = cursor.getString(5)
                val fileName = cursor.getString(6)

                val videoModel = VideoModel(id, path, title, fileName, size, dateAdded, duration)

                Log.e("path", path)
                val slashIndex = path.lastIndexOf("/")
                Log.d("TAG", "getVideoModelArrayList: slashIndex $slashIndex ")
                val sunString = path.substring(0, slashIndex)
                Log.d("TAG", "getVideoModelArrayList: sunString $sunString ")

                var tempFolderMap = folderNameArrayList.value
                if (tempFolderMap == null) {
                    tempFolderMap = HashMap()
                }
                tempFolderMap.put(sunString, tempFolderMap.getOrDefault(sunString, 0) + 1)
                folderNameArrayList.value = tempFolderMap!!
                videoModelArrayListTemp.add(videoModel)
            }
            cursor.close()
        }

        videoArrayList.value = videoModelArrayListTemp
    }
}