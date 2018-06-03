package com.ataulm.skipper.settings

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.ataulm.skipper.ClickableWord
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class ClickableWordsRepositoryTest {

    companion object {

        private val CLICKABLE_WORDS = listOf("SKIP INTRO", "CONTINUE", "PLAY NEXT")
                .map { ClickableWord(it) }
    }

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val skipperPersistence = FakeSkipperPersistence(clickableWords = CLICKABLE_WORDS.toMutableList())
    private val repository = ClickableWordsRepository(skipperPersistence)
    private val observer = mock(Observer::class.java) as Observer<List<ClickableWord>>

    @Test
    fun repositoryReturnsPersistedWords() {
        val liveData = repository.clickableWords()
        liveData.observeForever(observer)

        assertThat(liveData.value).isEqualTo(CLICKABLE_WORDS)
    }

    @Test
    fun addingWordUpdatesLiveData() {
        val liveData = repository.clickableWords()
        liveData.observeForever(observer)

        val newWord = ClickableWord("new words")
        repository.add(newWord)

        val extendedWords = CLICKABLE_WORDS.toMutableList()
        extendedWords.add(newWord)
        assertThat(liveData.value).isEqualTo(extendedWords)
    }

    @Test
    fun deletingWordUpdatesLiveData() {
        val liveData = repository.clickableWords()
        liveData.observeForever(observer)

        repository.delete(CLICKABLE_WORDS.last())

        val reducedWords = CLICKABLE_WORDS.toMutableList()
        reducedWords.remove(CLICKABLE_WORDS.last())
        assertThat(liveData.value).isEqualTo(reducedWords)
    }
}
