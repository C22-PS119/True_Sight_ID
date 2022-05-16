package com.truesightid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.truesightid.ui.adapter.ViewPagerAdapter
import com.truesightid.ui.dashboard.DashboardFragment
import com.truesightid.ui.home.HomeFragment
import com.truesightid.ui.notifications.NotificationsFragment

class MainActivity : AppCompatActivity() {

    lateinit var viewPager: ViewPager;
    lateinit var tabs: TabLayout;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById<ViewPager>(R.id.viewPager)
        tabs = findViewById<TabLayout>(R.id.tabs)

        initTabs()
    }

    private fun initTabs(){
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(DashboardFragment(), "Explore News")
        adapter.addFragment(HomeFragment(), "News Predict")
        adapter.addFragment(NotificationsFragment(), "Profile")

        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)

        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_explore_news_unselected)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_news_predict_unselected)
        tabs.getTabAt(2)!!.setIcon(R.drawable.ic_profile_unselected)
    }

}