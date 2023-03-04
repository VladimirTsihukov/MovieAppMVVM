package com.androidapp.movieappmvvm.view.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.androidapp.movieappmvvm.R
import com.androidapp.movieappmvvm.api.NetworkStatusLiveData
import com.androidapp.movieappmvvm.di.components.appComponent
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

private const val REG_CODE_SETTING = 789

class ActivityMain : AppCompatActivity() {

    private lateinit var snackBar: Snackbar

    @Inject
    lateinit var networkStatusLiveData: NetworkStatusLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appComponent.inject(this)

        initSnackBar()
        setVisibilityProgressBar(networkStatusLiveData.isNetworkAvailable())

        networkStatusLiveData.observe(this) {
            setVisibilityProgressBar(it)
        }
    }

    override fun onStart() {
        super.onStart()
        findNavController(R.id.nav_host_fragment_container).addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.movie_detail -> {
                    bottom_navigation.visibility = View.GONE
                }
                R.id.movie_list -> {
                    bottom_navigation.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setVisibilityProgressBar(connectInternet: Boolean) {
        if (connectInternet) {
            snackBar.dismiss()
        } else {
            snackBar.show()
        }
    }

    private fun initSnackBar() {
        snackBar = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            Snackbar.make(
                container_connect_internet, R.string.snackBar_title_device_is_offline_version_q,
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(R.string.dialog_button_yes) {
                    startActivityForResult(
                        Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY),
                        REG_CODE_SETTING
                    )
                }
        } else {
            Snackbar.make(
                container_connect_internet, R.string.snackBar_title_device_is_offline,
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(R.string.dialog_button_hide) {
                    snackBar.dismiss()
                }
        }.setTextColor(Color.RED)
            .setActionTextColor(getColor(R.color.app_assent))
    }
}
