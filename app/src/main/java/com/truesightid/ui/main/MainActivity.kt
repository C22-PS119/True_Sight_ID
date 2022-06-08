package com.truesightid.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.chibatching.kotpref.Kotpref
import com.google.android.material.tabs.TabLayout
import com.truesightid.R
import com.truesightid.ui.ViewModelFactory
import com.truesightid.ui.adapter.ViewPagerAdapter
import com.truesightid.ui.explore.ExploreNewsFragment
import com.truesightid.ui.login.LoginActivity
import com.truesightid.ui.prediction.NewsPredictFragment
import com.truesightid.ui.profile.ProfileFragment
import com.truesightid.utils.Prefs


class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Kotpref.init(this)
        setupViewModel()
        setupLogin()
        viewPager = findViewById(R.id.viewPager)
        tabs = findViewById(R.id.tabs)

        initTabs()

        val extras = intent.extras
        if(extras != null){
            if(extras.getBoolean("fromEditProfile")){
                // Go to profile
                tabs.selectTab(tabs.getTabAt(2))
            }
        }
    }

    private fun setupLogin() {
        if (Prefs.isLogin) { //  true atau false
            Log.d("TAG", "sudah login")
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            Log.d("TAG", "belum login, pindah ke menu login")
        }
    }

    private fun setupViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
    }

    private fun initTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(ExploreNewsFragment(), "Explore News")
        adapter.addFragment(NewsPredictFragment(), "News Predict")
        adapter.addFragment(ProfileFragment(), "Profile")

        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)

        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_explore_state)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_prediction_state)
        tabs.getTabAt(2)!!.setIcon(R.drawable.ic_profile_state)
    }

}