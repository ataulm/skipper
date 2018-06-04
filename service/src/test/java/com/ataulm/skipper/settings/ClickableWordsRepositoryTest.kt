package com.ataulm.skipper.settings

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.ataulm.skipper.ClickableWord
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*

class ClickableWordsRepositoryTest {

    companion object {

        fun clickableWordsRepository(vararg initialWords: String = emptyArray()): ClickableWordsRepository {
            val skipperPersistence = FakeSkipperPersistence(initialWords.toList().asClickableWords().toMutableList())
            return ClickableWordsRepository(skipperPersistence)
        }
    }

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val observer = mock(Observer::class.java) as Observer<List<ClickableWord>>

    @Test
    fun returnsEmptyListIfNothingPersisted() {
        val repository = clickableWordsRepository()

        repository.clickableWords().observeForever(observer)

        verify(observer).onChanged(emptyList())
    }

    @Test
    fun returnsPersistedWords() {
        val repository = clickableWordsRepository("skip", "next", "ok")

        repository.clickableWords().observeForever(observer)

        verify(observer).onChanged(listOf("skip", "next", "ok").asClickableWords())
    }

    @Test
    fun addingToEmptyRepositoryReturnsListWithAddedWord() {
        val repository = clickableWordsRepository()
        repository.clickableWords().observeForever(observer)
        verify(observer).onChanged(ArgumentMatchers.any())

        repository.add(ClickableWord("skip intro"))

        verify(observer, times(2)).onChanged(listOf("skip intro").asClickableWords())
    }

    @Test
    fun addingDuplicateWordDoesNothing() {
        val repository = clickableWordsRepository("continue")
        val liveData = repository.clickableWords()
        liveData.observeForever({
            observer.onChanged(it)
        })
        verify(observer).onChanged(ArgumentMatchers.any())

        repository.add(ClickableWord("continue"))

        verifyNoMoreInteractions(observer)
        assertThat(liveData.value).isEqualTo(listOf("continue").asClickableWords())
    }

    @Test
    fun addingToRepositoryReturnsListWithAllWords() {
        val repository = clickableWordsRepository("continue")
        repository.clickableWords().observeForever(observer)
        verify(observer).onChanged(ArgumentMatchers.any())

        repository.add(ClickableWord("skip intro"))

        verify(observer, times(2)).onChanged(listOf("continue", "skip intro").asClickableWords())
    }

    @Test
    fun deletingSoleWordReturnsEmptyList() {
        val soleWord = "skip intro"
        val repository = clickableWordsRepository(soleWord)
        repository.clickableWords().observeForever(observer)
        verify(observer).onChanged(ArgumentMatchers.any())

        repository.delete(ClickableWord(soleWord))

        verify(observer, times(2)).onChanged(emptyList())
    }

    @Test
    fun deletingFromRepositoryReturnsListWithAllRemainingWords() {
        val repository = clickableWordsRepository("skip intro", "continue")
        repository.clickableWords().observeForever(observer)
        verify(observer).onChanged(ArgumentMatchers.any())

        repository.delete(ClickableWord("skip intro"))

        verify(observer, times(2)).onChanged(listOf("continue").asClickableWords())
    }
}

private fun List<String>.asClickableWords(): List<ClickableWord> {
    return map { ClickableWord(it) }
}
