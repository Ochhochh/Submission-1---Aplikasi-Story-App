package com.example.submission1_mystoryapp.view.activity.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission1_mystoryapp.R
import com.example.submission1_mystoryapp.adapter.StoryAdapter
import com.example.submission1_mystoryapp.data.lokal.session.StorySession
import com.example.submission1_mystoryapp.data.remote.response.LoginResult
import com.example.submission1_mystoryapp.databinding.ActivityMainBinding
import com.example.submission1_mystoryapp.data.Result
import com.example.submission1_mystoryapp.view.activity.login.LoginActivity
import com.example.submission1_mystoryapp.view.activity.story.StoryActivity

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var pref: StorySession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: MainViewModelFactory = MainViewModelFactory.getInstance(this)
        val viewModel: MainViewModel by viewModels {
            factory
        }

        pref = StorySession(this)
        val user: LoginResult = pref.getToken()

        val storyAdapter = StoryAdapter()
        binding.apply{
            rvUser.adapter = storyAdapter
            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)

            Add.setOnClickListener {
                val intent = Intent(this@MainActivity, StoryActivity::class.java)
                startActivity(intent)
            }
        }

        viewModel.getStory("Bearer ${user.token}").observe(this) {
            if(it != null) {
                when(it) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val storyData = it.data
                        storyAdapter.submitList(storyData)
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            this@MainActivity,
                            resources.getString(R.string.not_found) + it.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                pref.getUser(
                    LoginResult(
                        name = null,
                        token = null,
                        isLogin = false
                    )
                )
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> false
        }
    }

}