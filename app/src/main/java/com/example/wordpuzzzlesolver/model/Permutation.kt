package com.example.wordpuzzzlesolver.model

fun main() {
    Permutation.combination("i am").forEach { println(it) }
}

object Permutation {
    fun combination(str: String): ArrayList<String> {
        val words = ArrayList<String>()
        for (permuteLen in str.indices) {
            val permutedWords = HashSet<String>()
            findCombination(
                str, BooleanArray(str.length),
                permutedWords,
                permuteLen + 1,
                StringBuilder()
            )
            //            System.out.println("-".repeat(10));
            words.addAll(permutedWords)
        }
        return words
    }

    private fun findCombination(
        word: String,
        checkedChar: BooleanArray,
        pWords: HashSet<String>,
        permuteLen: Int,
        stringBuilder: java.lang.StringBuilder
    ) {
        if (permuteLen == 0) {
            pWords.add(stringBuilder.toString())
            //            System.out.println(stringBuilder+"\n"+pWords);
            return
        }
        for (i in word.indices) {
            if (checkedChar[i]) continue
            stringBuilder.append(word[i])
            checkedChar[i] = true
            findCombination(word, checkedChar, pWords, permuteLen - 1, stringBuilder)
            stringBuilder.deleteCharAt(stringBuilder.length - 1)
            checkedChar[i] = false
        }
    }

    fun permute(str: String): ArrayList<String>? {
        val words = HashSet<String>()
        permuteHelper(str, words)
        return ArrayList(words)
    }

    private fun permuteHelper(str: String, permutedWords: HashSet<String>) {
        permutedWords.add(str)
        if (str.length <= 1) return
        for ((wordIndex, _) in (0 until str.length - 1).withIndex()) {
            val nextIndex = wordIndex + 1
            val newWord = StringBuilder(str)
            newWord.setCharAt(wordIndex, str[nextIndex])
            newWord.setCharAt(nextIndex, str[wordIndex])
            val word = newWord.toString()
            if (!permutedWords.contains(word)) {
                permuteHelper(word, permutedWords)
            }
        }
    }
}