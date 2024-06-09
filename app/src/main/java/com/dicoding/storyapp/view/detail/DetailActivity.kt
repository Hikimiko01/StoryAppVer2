package com.dicoding.storyapp.view.detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.storyapp.view.ViewModelFactory
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel>{
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyId = intent.getStringExtra("itemId")
        val storyPhotoUrl = intent.getStringExtra("itemUrl")

        storyId?.let {
            viewModel.getStoryDetail(it)
        }

        storyPhotoUrl.let {
            Glide.with(binding.ivDetailPhoto.context)
                .load(storyPhotoUrl)
                .into(binding.ivDetailPhoto)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.story.observe(this) { response ->
            if (response.error == false) {
                binding.tvDetailName.text = response.story?.name
                binding.tvDetailDescription.text = response.story?.description
            } else {
                binding.tvDetailName.text = getString(R.string.error_occured)
                binding.tvDetailDescription.text = response.message
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}