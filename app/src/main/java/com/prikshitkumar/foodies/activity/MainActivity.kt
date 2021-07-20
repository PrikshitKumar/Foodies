package com.prikshitkumar.foodies

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.prikshitkumar.foodies.activity.LoginPage
import com.prikshitkumar.foodies.fragments.*

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinateLayout: CoordinatorLayout
    lateinit var navigationView: NavigationView
    lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var headerView: View
    lateinit var profilePic: ImageView

    lateinit var userName: TextView
    lateinit var userPhoneNo: TextView

    var previouMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeTheFields()
        setUpToolbar()
        openHome()
        userHeaderData()

        val actionBarDrawerToggle= ActionBarDrawerToggle(this@MainActivity, drawerLayout, R.string.open_Drawer, R.string.close_Drawer)

        //Adding ClickListener for Hamburger Icon
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()  // When the Navigation Drawer is shown then, the Hamburger Icon changes to Back Arrow and vice versa

        // ClickListener for items of Navigation Drawer
        profilePic.setOnClickListener {
            openProfile()
            drawerLayout.closeDrawers()
        }

        navigationView.setNavigationItemSelectedListener {

            if(previouMenuItem != null) { // If some menuItem is checked and selected previously then we will uncheck that first in this block
                previouMenuItem?.isChecked = false
            }
            it.isCheckable = true  // isCheckable and isChecked are used for current selected MenuItem
            it.isChecked = true
            previouMenuItem = it  // Add the current seleted MenuItem to previousMenuItem variable for deselect that next time

            when(it.itemId) {
                R.id.id_itemHome -> {
                    openHome()
                    drawerLayout.closeDrawers()
                }
                R.id.id_itemProfile -> {
                    openProfile()
                    drawerLayout.closeDrawers()
                }
                R.id.id_itemFavoriteRestaurants -> {
                    openFavorites()
                    drawerLayout.closeDrawers()
                }
                R.id.id_itemOrderHistory -> {
                    openHistory()
                    drawerLayout.closeDrawers()
                }
                R.id.id_itemFAQs -> {
                    openFAQs()
                    drawerLayout.closeDrawers()
                }
                R.id.id_itemLogOut -> {
                    logOut()
                    drawerLayout.closeDrawers()
                }
            }
            // this is lambda representation that's why we use return statement with @setNavigationItemSelectedListener
            return@setNavigationItemSelectedListener true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {  // ClickListener for Hamburger Icon
        val id = item.itemId
        if(id == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    /* When we pressed the back button then our application's Activity gets back and close the App but we want to get back to the previously
     * selected fragment. For that we are using "BACK-STACK" like {transaction.addToBackStack("Dashboard")}, but we want always to open the
     * Dasboard Fragment when clicked back button otherwise close the application if current activity is Dashboard. For that we have to override
     * the onBackPressed() method */
    override fun onBackPressed() {
        val fragment= supportFragmentManager.findFragmentById(R.id.id_frameLayout)
        if(drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawers()
        }
        else {
            when(fragment) {
                !is Home -> openHome()  /* If current Fragment is not Dashboard Fragment and when we pressed the back button then open
            the Dashboard Fragment. Otherwise close the application.. */
                else -> super.onBackPressed()
            }
        }
    }

    fun initializeTheFields(){
        drawerLayout= findViewById(R.id.id_drawerLayout)
        navigationView= findViewById(R.id.id_navigationView)
        coordinateLayout= findViewById(R.id.id_coordinateLayout)
        toolbar= findViewById(R.id.id_toolbar)
        frameLayout= findViewById(R.id.id_frameLayout)
        headerView= navigationView.getHeaderView(0)

        userName = headerView.findViewById(R.id.id_headerUserName)
        userPhoneNo = headerView.findViewById(R.id.id_headerUserPhoneNo)
        profilePic = headerView.findViewById(R.id.id_profilePic)
    }
    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun openHome() {
        val fragment = Home()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.id_frameLayout, fragment)
        transaction.commit()
        supportActionBar?.title = "Restaurants"
        navigationView.setCheckedItem(R.id.id_itemHome)  //Set the Dashboard Fragment as currently selected Layout and highlights it in Menu
    }
    fun openProfile() {
        val fragment = Profile()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.id_frameLayout, fragment)
        transaction.commit()
        supportActionBar?.title = "My Profile"
        navigationView.setCheckedItem(R.id.id_itemProfile)
    }
    fun openFavorites() {
        val fragment = Favorites()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.id_frameLayout, fragment)
        transaction.commit()
        supportActionBar?.title = "Favorite Restaurants"
        navigationView.setCheckedItem(R.id.id_itemFavoriteRestaurants)
    }
    fun openHistory() {
        val fragment = History()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.id_frameLayout, fragment)
        transaction.commit()
        supportActionBar?.title = "Order History"
        navigationView.setCheckedItem(R.id.id_itemOrderHistory)
    }
    fun openFAQs() {
        val fragment = Faqs()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.id_frameLayout, fragment)
        transaction.commit()
        supportActionBar?.title = "FAQs"
        navigationView.setCheckedItem(R.id.id_itemFAQs)
    }
    fun logOut() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure to Logging Out")
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->

            val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putBoolean("first_Start", true)
            editor.apply()
            Toast.makeText(this, "Logout Successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@MainActivity, LoginPage::class.java)
            startActivity(intent)
            finishAffinity()

        })
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
            openHome()
        })
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    fun userHeaderData() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE)
        userName.text = sharedPreferences.getString("userName", " ").toString()
        userPhoneNo.text = sharedPreferences.getString("mobileNo", " ").toString()
    }
}