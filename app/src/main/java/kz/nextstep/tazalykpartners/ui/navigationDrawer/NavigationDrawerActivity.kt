package kz.nextstep.tazalykpartners.ui.navigationDrawer

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import kz.nextstep.tazalykpartners.MainApplication
import kz.nextstep.tazalykpartners.R
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator

class NavigationDrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var router: Router
    lateinit var navigationDrawerViewModel: NavigationDrawerViewModel

    lateinit var toolbar: Toolbar
    lateinit var iv_main_nav_profile: ImageView
    lateinit var tv_main_nav_name: TextView
    lateinit var layout_main_drawer: DrawerLayout
    lateinit var navView: NavigationView

    private val navigator = SupportAppNavigator(this, R.id.activity_navigation_drawer_container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_drawer)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        layout_main_drawer = findViewById(R.id.layout_main_drawer)
        navView= findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, layout_main_drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        layout_main_drawer.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)


        router = MainApplication.INSTANCE?.getRouter()!!


    }

    override fun onBackPressed() {
        if (layout_main_drawer.isDrawerOpen(GravityCompat.START)) {
            layout_main_drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
            MainApplication.INSTANCE?.getRouter()?.exit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navigation_drawer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_filter -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_pin_list -> {
                // Handle the camera action
            }
            R.id.nav_statistics -> {

            }
            R.id.nav_edit_profile -> {

            }
            R.id.nav_sign_out -> {

            }
        }
        layout_main_drawer.closeDrawer(GravityCompat.START)
        return true
    }


    override fun onResume() {
        super.onResume()
        MainApplication.INSTANCE?.getNavigatorHolder()?.setNavigator(navigator)
    }

    override fun onPause() {
        MainApplication.INSTANCE?.getNavigatorHolder()?.removeNavigator()
        super.onPause()
    }
}
