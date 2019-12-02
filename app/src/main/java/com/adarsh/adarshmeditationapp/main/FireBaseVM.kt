package com.adarsh.adarshmeditationapp.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.adarsh.adarshmeditationapp.data.FirebaseModel
import com.adarsh.adarshmeditationapp.data.MusicModel
import com.adarsh.adarshmeditationapp.db.MusicDao
import com.adarsh.adarshmeditationapp.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import javax.inject.Inject

class FireBaseVM @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val musicDao: MusicDao
) : ViewModel() {

    private val mediatorLiveData = MediatorLiveData<Resource<List<FirebaseModel>>>()

    fun getDetailsFromFireBase(): LiveData<Resource<List<FirebaseModel>>> {
        fetchDetailsFromFireBase()
        return mediatorLiveData
    }

    private fun fetchDetailsFromFireBase() {
        val listOfFireBaseModel = ArrayList<FirebaseModel>()

        CoroutineScope(Dispatchers.IO).launch {

            setValue(Resource.loading(null))

            val listOfFirebaseModel = musicDao.getAllMusicDetails()

            setValue(Resource.success(listOfFirebaseModel))

            firebaseFirestore.collection("meditation")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    firebaseFirestoreException?.let {
                        setValue(Resource.error(listOfFireBaseModel, "Network issue"))
                        return@addSnapshotListener
                    }
                    setValue(Resource.loading(listOfFireBaseModel))
                    val list = querySnapshot?.documents

                    list?.let {
                        for (item in it) {
                            val firebaseModel = item.toObject(FirebaseModel::class.java)
                            firebaseModel?.key = item.id
                            firebaseFirestore.collection("meditation").document(item.id)
                                .collection("session").get()
                                .addOnSuccessListener { subCollQuerySnapShot ->
                                    if (!subCollQuerySnapShot.isEmpty) {
                                        val musicList =
                                            subCollQuerySnapShot.toObjects(MusicModel::class.java)
                                        for (musicItem in musicList) {
                                            firebaseModel?.musicModel = musicItem
                                            firebaseModel?.let { listOfFireBaseModel.add(it) }
                                        }
                                    }
                                }.addOnCompleteListener {
                                    updateMusicDb(listOfFireBaseModel)
                                }
                        }
                    }
                }

        }
    }

    private fun updateMusicDb(listOfMusicModel: List<FirebaseModel>) {
        CoroutineScope(Dispatchers.IO).launch {
            musicDao.clearAllMusicDetails()
            musicDao.upsertData(listOfMusicModel)
            val listOfFirebaseModelFromNetwork = musicDao.getAllMusicDetails()
            setValue(Resource.success(listOfFirebaseModelFromNetwork))
        }
    }

    private fun setValue(listOfFirebaseModel: Resource<List<FirebaseModel>>) {
        CoroutineScope(Dispatchers.Main).launch {
            if (mediatorLiveData != null)
                mediatorLiveData.value = listOfFirebaseModel
        }
    }
}