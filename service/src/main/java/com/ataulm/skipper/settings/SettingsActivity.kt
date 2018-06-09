package com.ataulm.skipper.settings

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.ataulm.skipper.ClickableWord
import com.ataulm.skipper.SharedPreferencesSkipperPersistence
import com.ataulm.skipper.observer.DataObserver
import com.example.R
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val repository = ClickableWordsRepository(SharedPreferencesSkipperPersistence.create(this))
        clickableWordsRecyclerView.layoutManager = LinearLayoutManager(this)
        clickableWordsRecyclerView.adapter = ClickableWordsAdapter(object : ClickableWordsAdapter.Callback {

            override fun onClickConfigure(word: ClickableWord) {
                startActivity(Intent(this@SettingsActivity, ConfigureEntriesActivity::class.java)
                        .putExtra(ConfigureEntriesActivity.EXTRA_CLICKABLE_WORD, word.word))
            }

            override fun onClickDelete(word: ClickableWord) {
                repository.delete(word)
            }
        })

        addClickableWordButton.setOnClickListener({
            val text = addClickableWordEditText.text
            if (text.isNotBlank()) {
                val clickableWord = ClickableWord(text.trim().toString())
                repository.add(clickableWord)
                addClickableWordEditText.text = null
            }
        })

        repository.clickableWords().observe(this, DataObserver<List<ClickableWord>> {
            (clickableWordsRecyclerView.adapter as ClickableWordsAdapter).update(it)
        })
    }
}

