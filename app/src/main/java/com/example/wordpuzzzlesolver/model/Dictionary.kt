package com.example.wordpuzzzlesolver.model

import android.content.res.Resources
import com.example.wordpuzzzlesolver.R
import java.io.File

fun main() {
    val dictionary = Dictionary.initialize(CompressedTrieTree())
    println(dictionary.isValidWord("cadb"))
}

object Dictionary {
    var onlyAlphabets: Boolean = true
        private set
    var root: DictionaryStorage? = null

    internal fun initialize(dictionaryStorage: DictionaryStorage): Dictionary {
        root = dictionaryStorage
        var dictionaryPath =
            System.getProperty("user.dir") // Uses this to get the base dir eg "~/.../ProjectName"

        dictionaryPath += "\\app\\src\\main\\java\\com\\example\\wordpuzzzlesolver\\assets\\dictionary_words.txt"
//        dictionaryPath += "\\app\\src\\main\\res\\raw\\dictionary_words.txt"
        File(dictionaryPath).bufferedReader().forEachLine {
            assert(root != null) { error("Fatal crash: Could not resolve a storage medium for the dictionary") }
            root!!.insertIntoTree(it)
        }
        return this
    }

    fun initialize(resources: Resources, dictionaryStorage: DictionaryStorage, onlyAlphabets: Boolean = true): Dictionary {
        root = dictionaryStorage
        this.onlyAlphabets = onlyAlphabets
        resources
            .openRawResource(R.raw.dictionary_words)
            .bufferedReader().forEachLine {
                assert(root != null) { error("Fatal crash: Could not resolve a storage medium for the dictionary") }
                root!!.insertIntoTree(it.lowercase())
            }
        println("Reconfigured")
        return this
    }

    fun printDictionary() {
        root?.printAllWords()
    }

    fun isValidWord(word: String): Boolean {
        return root!!.isValidWord(word)
    }
}

