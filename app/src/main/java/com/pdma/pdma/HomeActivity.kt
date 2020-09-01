package com.pdma.pdma

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.pdma.pdma.network.LoginResponse
import com.pdma.pdma.utils.Constants
import com.google.android.material.navigation.NavigationView
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission


class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var loginResponse: LoginResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val intent = getIntent()
        intent?.let {
            loginResponse = intent.getParcelableExtra(Constants.LOGIN_RESPONSE)!!
        }

        val permissionlistener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {

            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(
                    applicationContext,
                    "Permission Denied\n ${deniedPermissions.toString()}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permissionlistener)
            .setDeniedMessage("If you reject permission,you can not use Micro ATM service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .check()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.action_logout) {

            val sharedPreference =
                this.getSharedPreferences(Constants.LOGIN_CREDENTIAL, Context.MODE_PRIVATE)
            val editor = sharedPreference.edit()
            editor.remove(Constants.USER_ID)
            editor.remove(Constants.PASSWORD)
            editor.clear()
            editor.apply()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun getLoginResponse(): LoginResponse {
        return loginResponse
    }
}