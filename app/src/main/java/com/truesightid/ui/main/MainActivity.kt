package com.truesightid.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.chibatching.kotpref.Kotpref
import com.google.android.material.tabs.TabLayout
import com.truesightid.R
import com.truesightid.ui.ViewModelFactory
import com.truesightid.ui.adapter.ViewPagerAdapter
import com.truesightid.ui.explore.ExploreNewsFragment
import com.truesightid.ui.language.LanguageManager
import com.truesightid.ui.login.LoginActivity
import com.truesightid.ui.prediction.NewsPredictFragment
import com.truesightid.ui.profile.ProfileFragment
import com.truesightid.utils.Prefs
import com.truesightid.utils.extension.pushActivity

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

        when (Prefs.isDarkMode("pref_is_dark_mode")) {
            true -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            false -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val languageManager = LanguageManager(this)
        languageManager.getLang()?.let { languageManager.updateResource(it) }

        initTabs()
    }

    private fun setupLogin() {
        if (Prefs.isLogin) { //  true atau false
            Log.d("TAG", "sudah login")
        } else {
            pushActivity(LoginActivity::class.java)
            Log.d("TAG", "belum login, pindah ke menu login")
        }
    }

    private fun setupViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
    }

    private fun initTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(ExploreNewsFragment(), resources.getString(R.string.title_explore_news))
        adapter.addFragment(NewsPredictFragment(), resources.getString(R.string.title_news_predict))
        adapter.addFragment(ProfileFragment(), resources.getString(R.string.title_profile))

        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)

        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_explore_state)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_prediction_state)
        tabs.getTabAt(2)!!.setIcon(R.drawable.ic_profile_state)

        val extras = intent.extras
        if(extras != null){
            if(extras.getBoolean("shouldProfile")){
                // Go to profile
                tabs.selectTab(tabs.getTabAt(2))
            }
        }
    }

}