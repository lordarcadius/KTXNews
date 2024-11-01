package com.vipuljha.ktxnews.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.vipuljha.ktxnews.R
import com.vipuljha.ktxnews.data.local.ArticleDB
import com.vipuljha.ktxnews.data.repository.NewsRepository
import com.vipuljha.ktxnews.databinding.ActivityNewsBinding
import com.vipuljha.ktxnews.viewmodels.NewsViewModel
import com.vipuljha.ktxnews.viewmodels.NewsViewModelProviderFactory

class NewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding
    lateinit var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repository = NewsRepository(ArticleDB(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.bottomNavigationView.setupWithNavController(findNavController(R.id.newsNavHostFragment))
    }
}