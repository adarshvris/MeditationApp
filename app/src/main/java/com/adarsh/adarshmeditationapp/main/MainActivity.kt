package com.adarsh.adarshmeditationapp.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.adarsh.adarshmeditationapp.R
import com.adarsh.adarshmeditationapp.constants.FIREBASE_MODEL
import com.adarsh.adarshmeditationapp.data.FirebaseModel
import com.adarsh.adarshmeditationapp.extensions.gone
import com.adarsh.adarshmeditationapp.extensions.showMessage
import com.adarsh.adarshmeditationapp.extensions.visible
import com.adarsh.adarshmeditationapp.extensions.vmProvider
import com.adarsh.adarshmeditationapp.meditation.MeditationActivity
import com.adarsh.adarshmeditationapp.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), View.OnClickListener {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory
    private lateinit var fireBaseVM: FireBaseVM
    @Inject
    lateinit var firebaseDb: FirebaseFirestore
    private val listOfFireBaseModel = ArrayList<FirebaseModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnMeditation.setOnClickListener(this)
        btnCalmDown.setOnClickListener(this)
        btnDestress.setOnClickListener(this)
        btnRelax.setOnClickListener(this)

        vmProvider<FireBaseVM>(viewModelProvider) {
            fireBaseVM = it
        }

        fetchDetailsFromFirebase()
    }

    private fun fetchDetailsFromFirebase() {
        fireBaseVM.getDetailsFromFireBase().observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    llLoading.visible()
                    btnRetry.gone()
                    llOptions.gone()
                }

                Resource.Status.SUCCESS -> {
                    Log.e("firebase", "value ${it.data}")
                    if (listOfFireBaseModel.isNotEmpty()) {
                        listOfFireBaseModel.clear()
                    }
                    it.data?.let { listOfData ->
                        if (listOfData.isNotEmpty()) {
                            llLoading.gone()
                            llOptions.visible()
                        }
                        listOfFireBaseModel.addAll(listOfData)
                    }
                }

                Resource.Status.ERROR -> {
                    if (listOfFireBaseModel.isEmpty()) {
                        this.showMessage(getString(R.string.no_network_available))
                        btnRetry.visible()
                        btnRetry.setOnClickListener {
                            fetchDetailsFromFirebase()
                        }
                    }
                    Log.e("firebase", "error ${it.data}")
                    llLoading.gone()
                    llOptions.gone()

                }
            }

        })
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnMeditation -> navigateToMeditationActivity(listOfFireBaseModel[0])

            R.id.btnCalmDown -> navigateToMeditationActivity(listOfFireBaseModel[1])

            R.id.btnDestress -> navigateToMeditationActivity(listOfFireBaseModel[2])

            R.id.btnRelax -> navigateToMeditationActivity(listOfFireBaseModel[3])
        }
    }

    private fun navigateToMeditationActivity(firebaseModel: FirebaseModel) {
        val intent = Intent(this, MeditationActivity::class.java)
        intent.putExtra(FIREBASE_MODEL, firebaseModel)
        startActivity(intent)
    }
}
