package mekotlinapps.dnyaneshwar.`in`.googlelogindemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

/**
 * Created by Dnyaneshwar Dalvi on 20/11/17.
 */
class HomeActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    lateinit var googleApiClient: GoogleApiClient
    lateinit var googleSignInOptions: GoogleSignInOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        googleApiClient = GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build()

        val intent = intent
        val name = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")
        val imageURL = intent.getStringExtra("url")

        try {
            tvName.setText("Name : $name")
            tvEmail.setText("Email : $email")

            if (!imageURL.equals("null")) {
                Glide.with(this).load(imageURL).into(imageView)
            } else {
                imageView.visibility = View.GONE
            }
            toast("Thank you $name")
        } catch (e: Exception) {
        }

        btnSignOut.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                progress_bar.visibility = View.VISIBLE
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                        object : ResultCallback<Status> {
                            override fun onResult(status: Status) {
                                progress_bar.visibility = View.GONE
                                val intent_ = Intent(this@HomeActivity, LoginActivity::class.java)
                                intent_.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                intent_.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                startActivity(intent_)
                                finish()
                            }
                        })
            }
        })
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.d("ConnectionResult", connectionResult.toString())
    }
}
