package com.truesightid.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.truesightid.R
import com.truesightid.ui.adapter.ViewPagerAdapter
import com.truesightid.ui.explore.ExploreNewsFragment
import com.truesightid.ui.prediction.NewsPredictFragment
import com.truesightid.ui.profile.ProfileFragment

class DetailClaimActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)
        tabs = findViewById(R.id.tabs)

        initTabs()
    }

    private fun initTabs(){
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(ExploreNewsFragment(), "Explore News")
        adapter.addFragment(NewsPredictFragment(), "News Predict")
        adapter.addFragment(ProfileFragment(), "Profile")

        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)

        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_explore_news_unselected)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_news_predict_unselected)
        tabs.getTabAt(2)!!.setIcon(R.drawable.ic_profile_unselected)
    }

}