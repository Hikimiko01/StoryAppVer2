package com.dicoding.storyapp.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.adapter.ItemAdapter
import com.dicoding.storyapp.data.adapter.LoadingStateAdapter
import com.dicoding.storyapp.databinding.ActivityMainBinding
import com.dicoding.storyapp.view.ViewModelFactory
import com.dicoding.storyapp.view.add.AddStoryActivity
import com.dicoding.storyapp.view.maps.MapsActivity
import com.dicoding.storyapp.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var itemAdapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (user.isLogin) {
                binding.greetingTv.text = binding.root.context.getString(R.string.greeting,
                    user.name
                )
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        isLogin()
        setupView()
        setupRecyclerView()
        setupAction()
        addStory()
    }

    override fun onResume() {
        super.onResume()
        if(!itemAdapter.snapshot().isEmpty()){
            itemAdapter.refresh()
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupRecyclerView() {
        itemAdapter = ItemAdapter()
        binding.rvStory.layoutManager = LinearLayoutManager(this)
        binding.rvStory.adapter = itemAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                itemAdapter.retry()
            }
        )

        itemAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                showLoading(true)
            } else {
                showLoading(false)
            }
        }
    }

    private fun isLogin() {
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        viewModel.story.observe(this) { pagingData ->
            itemAdapter.submitData(lifecycle, pagingData)
            binding.loadingProgressBar.visibility = View.GONE
        }
    }

    private fun setupAction() {
        binding.actionLogout.setOnClickListener {
            viewModel.logout()
        }
        binding.mapsBtn.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addStory(){
        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}