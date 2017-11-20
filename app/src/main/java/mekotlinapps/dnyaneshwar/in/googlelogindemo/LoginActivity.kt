package mekotlinapps.dnyaneshwar.`in`.googlelogindemo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    lateinit var googleSignInOptions: GoogleSignInOptions
    lateinit var googleApiClient: GoogleApiClient
    val GOOGLE_SIGN_IN: Int = 201

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        googleApiClient = GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build()

        btnSignIn.setSize(SignInButton.SIZE_STANDARD);

        btnSignIn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                progressBar.visibility = View.VISIBLE
                signIn()
            }
        })
    }

    fun signIn() {
        val signIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        startActivityForResult(signIntent, GOOGLE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {

            GOOGLE_SIGN_IN -> {
                val googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                handelSignInResult(googleSignInResult)
            }
        }
    }

    fun handelSignInResult(completedTask: GoogleSignInResult) {
        try {

            if (completedTask.isSuccess) {
                val account = completedTask.signInAccount

                if (account != null) {
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    intent.putExtra("name", account.displayName.toString())
                    intent.putExtra("email", account.email.toString())
                    intent.putExtra("url", account.photoUrl.toString())
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                }
            }
        } catch (e: Exception) {
        } finally {
            progressBar.visibility = View.GONE
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.d("ConnectionResult", connectionResult.toString())
    }
}
