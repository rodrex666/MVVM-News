package com.example.newsapprodrigo.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.newsapprodrigo.R
import com.example.newsapprodrigo.adapter.breakNewsDiffCall
import com.example.newsapprodrigo.databinding.NewsMainActivityBinding
import com.example.newsapprodrigo.repository.NewsRepository
import com.google.android.material.navigation.NavigationView

class NewsMainActivity : AppCompatActivity() {
    private lateinit var binding: NewsMainActivityBinding
    private var navigationController:NavController? = null
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navView: NavigationView
    private val viewModel:NewsViewModel by viewModels()
    private lateinit var swipeContainer: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.news_main_activity)
        binding.lifecycleOwner = this
        initView()

    }
    private fun initView() {
        drawerLayout = binding.drawerLayout
        navigationController = Navigation.findNavController(this,R.id.newsNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this,navigationController!!,drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navigationController!!)
        appBarConfiguration = AppBarConfiguration(navigationController!!.graph,drawerLayout)
        optionsDrawerMenuSelected()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.over_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!,navigationController!!)||
                return super.onOptionsItemSelected(item)
    }

    private fun optionsDrawerMenuSelected(){
        navView = findViewById(R.id.navView)
        navView.setNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.menu_item_1 -> {
                    Country.selectGermany()
                    navigateOptionsMenu(Country.getCountry())
                    true
                }
                R.id.menu_item_2 -> {
                    Country.selectUS()
                    navigateOptionsMenu(Country.getCountry())
                    true
                }
                else -> {
                    false
                }
            }

        }
    }
    private fun navigateOptionsMenu(country: String){
        viewModel.getBreakingNews(country)
        navigationController!!.navigate(R.id.breakNewsFragment)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.newsNavHostFragment)
        return NavigationUI.navigateUp(navController,drawerLayout)
    }


    object Country{
        private var country = "de"
        fun getCountry():String{
            return country
        }
        fun selectGermany(){
            country = "de"
        }
        fun selectUS(){
            country = "us"
        }
    }

}