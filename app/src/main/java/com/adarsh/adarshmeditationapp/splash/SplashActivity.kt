package com.adarsh.adarshmeditationapp.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.adarsh.adarshmeditationapp.main.MainActivity
import com.adarsh.adarshmeditationapp.R
import com.adarsh.adarshmeditationapp.extensions.gone
import com.adarsh.adarshmeditationapp.extensions.showMessage
import com.adarsh.adarshmeditationapp.extensions.visible
import com.adarsh.adarshmeditationapp.utils.isNetworkAvailable
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SplashActivity : DaggerAppCompatActivity(), View.OnClickListener {

    @Inject
    lateinit var gso: GoogleSignInOptions
    @Inject
    lateinit var auth: FirebaseAuth

    private val disposable = CompositeDisposable()
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        sibGoogle.setOnClickListener(this)
        btnAnonymous.setOnClickListener(this)
        btnRetry.setOnClickListener(this)

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        disposable.add(
            Completable.timer(
                3,
                TimeUnit.SECONDS,
                AndroidSchedulers.mainThread()
            ).subscribe {
                checkNetworkAvailabilityAndProceed()
            })
    }

    private fun checkNetworkAvailabilityAndProceed() {
        if (isNetworkAvailable(this)) {
            checkUserSignedInOrNot()
        } else {
            showRetry()
            this.showMessage(getString(R.string.no_network_available))
        }
    }

    private fun checkUserSignedInOrNot() {
        auth.addAuthStateListener { fireBaseAuth ->
            if (fireBaseAuth.currentUser != null) {
                showLoading(false)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                showLogin()
            }
        }
    }

    private fun showLogin() {
        lavLoading.gone()
        tvAppName.gone()
        sibGoogle.visible()
        btnAnonymous.visible()
        btnRetry.gone()
        clParent.setBackgroundColor(ContextCompat.getColor(this, R.color.theme))
    }

    private fun showRetry() {
        lavLoading.gone()
        tvAppName.gone()
        btnRetry.visible()
        clParent.setBackgroundColor(ContextCompat.getColor(this, R.color.theme))
    }

    private fun signInWithGoogle() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signInWithAnonymous() {
        auth.signInAnonymously().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
            } else {
                showLoading(false)
                this.showMessage("Authentication failed!")
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.sibGoogle -> {
                showLoading()
                signInWithGoogle()
            }

            R.id.btnAnonymous -> {
                showLoading()
                signInWithAnonymous()
            }

            R.id.btnRetry -> checkNetworkAvailabilityAndProceed()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                fireBaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                showLoading(false)
                this.showMessage("Authentication failed!")
            }
        }
    }

    private fun fireBaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                } else {
                    showLoading(false)
                    this.showMessage("Authentication failed!")
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!disposable.isDisposed)
            disposable.dispose()
    }

    companion object {
        const val RC_SIGN_IN = 100
    }

    private fun showLoading(show: Boolean = true) {
        if (show) {
            pbLoading.visible()
        } else {
            pbLoading.gone()
        }
    }
}
