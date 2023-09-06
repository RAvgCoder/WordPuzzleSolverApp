package com.example.wordpuzzzlesolver.model

interface DictionaryStorage {

    /**
     * Inserts a word into the tree
     */
    fun insertIntoTree(word: String,  onlyAlphabets: Boolean = true)

    /**
     * Checks if the word you want inserted
     */
    fun isValidWord(word: String): Boolean

    fun printAllWords()
}