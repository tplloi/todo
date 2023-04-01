package com.roy93group.activity

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.applovin.mediation.ads.MaxAdView
import com.google.android.material.navigation.NavigationView
import com.loitp.annotation.LogTag
import com.loitp.core.base.BaseActivityFont
import com.loitp.core.ext.*
import com.roy93group.R
import com.roy93group.fragment.HomeFragment
import com.roy93group.fragment.SettingFragment
import com.roy93group.helper.Applovin
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_drawer_end.*
import kotlinx.android.synthetic.main.view_drawer_main.*
import kotlinx.android.synthetic.main.view_drawer_start.view.*

/**
 * Created by Loitp on 12.09.2022
 * Galaxy One company,
 * Vietnam
 * +840766040293
 * freuss47@gmail.com
 */
@LogTag("MainActivity")
class MainActivity : BaseActivityFont(), NavigationView.OnNavigationItemSelectedListener {

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_main
    }

    private var adView: MaxAdView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        setSupportActionBar(toolbar)
        adView = Applovin.createAdBanner(
            a = this,
            logTag = logTag,
            bkgColor = Color.CYAN,
            viewGroup = flAd,
            isAdaptiveBanner = true,
        )

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navViewStart.setNavigationItemSelectedListener(this)
        drawerLayout.useCustomBehavior(Gravity.START)
        drawerLayout.useCustomBehavior(Gravity.END)

        // cover
        navViewStart.getHeaderView(0).ivCover.loadGlide(
            any = getString(R.string.link_cover),
        )

        tvAd.text = readTxtFromRawFolder(nameOfRawFile = R.raw.infor)
//        tvAd.isVisible = BuildConfig.DEBUG

        switchHomeScreen()
    }

    override fun onDestroy() {
        adView?.destroy()
        super.onDestroy()
    }

    private fun switchHomeScreen() {
        navViewStart.menu.performIdentifierAction(R.id.navHome, 0)
        navViewStart.menu.findItem(R.id.navHome).isChecked = true
    }

    private var doubleBackToExitPressedOnce = false
    override fun onBaseBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBaseBackPressed()
                return
            }
            this.doubleBackToExitPressedOnce = true
            showShortInformation(msg = getString(R.string.press_again_to_exit), isTopAnchor = false)
            Handler(Looper.getMainLooper()).postDelayed({
                doubleBackToExitPressedOnce = false
            }, 2000)
        }
    }

    private var currentItemId = R.id.navHome
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navHome -> {
                currentItemId = R.id.navHome
                this.replaceFragment(
                    containerFrameLayoutIdRes = R.id.flContainer,
                    fragment = HomeFragment(),
                    isAddToBackStack = false
                )
            }
            R.id.navSetting -> {
                currentItemId = R.id.navSetting
                this.replaceFragment(
                    containerFrameLayoutIdRes = R.id.flContainer,
                    fragment = SettingFragment(),
                    isAddToBackStack = false
                )
            }
            R.id.navRateApp -> {
                this.rateApp()
            }
            R.id.navMoreApp -> {
                this.moreApp()
            }
            R.id.navPolicy -> {
                this.openBrowserPolicy()
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        navViewStart.postDelayed({
            if (currentItemId == R.id.navHome) {
                navViewStart.menu.findItem(R.id.navHome).isChecked = true
            } else if (currentItemId == R.id.navSetting) {
                navViewStart.menu.findItem(R.id.navSetting).isChecked = true
            }
        }, 500)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_drawer_end, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionRightDrawer -> {
                drawerLayout.openDrawer(GravityCompat.END)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
