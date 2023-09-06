package com.example.wordpuzzzlesolver.model

class CompactTrieTree : DictionaryStorage {
    private var _word: String = "$"
    private var _nextNode: MutableMap<String, CompactTrieTree> = mutableMapOf()
    private var _isValidWord: Boolean = false

    override fun insertIntoTree(word: String, onlyAlphabets: Boolean) {
        if (word.matches(Regex("[A-Za-z]+")) && onlyAlphabets){
            insertIntoTreeHelper(word, this)
        }
    }

    override fun isValidWord(word: String):Boolean {
        if (word.isEmpty()) return false
        return isValidWordHelper(word,this)
    }

    private fun isValidWordHelper(word: String, currNode: CompactTrieTree): Boolean {
        val(foundWord, suffixIndex, nextNode) = currNode.getStartingStringInfo(word)
        // Word is not found || not all char of the found word starts the given word
        if (foundWord == null || suffixIndex != foundWord.lastIndex) return false
        if (word == foundWord) return nextNode._isValidWord
        return isValidWordHelper(subString(word,suffixIndex),nextNode)
    }

    override fun printAllWords() {
        printAllWordsHelper(this._nextNode, StringBuilder())
    }

    private fun insertIntoTreeHelper(word: String, currNode: CompactTrieTree) {
        val (foundWord, suffixIndex, nextNode) = currNode.getStartingStringInfo(word)
        if (foundWord == null) { // New word not matched to any already contained  map -> {man,dog} new -> boy
            nextNode.addWholeWord(word)
            return
        } else if (suffixIndex < foundWord.lastIndex) { // New word is shorter than foundMath (1) found -> mens new -> man (2) found -> tasks new -> tank
            val newKey = nextNode.reconfigureTree(word, suffixIndex)
            currNode.replaceKey(newKey, foundWord)
            return
        } else {
            val partWord = subString(word,suffixIndex)
            insertIntoTreeHelper(partWord, nextNode)
        }
    }


    var count = 1
    private fun printAllWordsHelper(
        children: MutableMap<String, CompactTrieTree>,
        wordBuilder: StringBuilder
    ) {
        var currWord: String?;
        for ((word, currNode) in children) {
            wordBuilder.append(word)
            currWord = word
            if (currNode._isValidWord) {
                println(wordBuilder)
                println(count++)
            }
            printAllWordsHelper(currNode._nextNode, wordBuilder)
            // Delete formed words
            wordBuilder.apply {
                delete(lastIndex - currWord.lastIndex, length)
            }
        }
    }

    /**
     * Gets the last index of the already contained word
     *
     * Triple(foundWord, suffixIndex, parentNodeToBe)
     */
    private fun getStartingStringInfo(word: String): Triple<String?, Int, CompactTrieTree> {
        for (str in _nextNode.keys) {
            if (word[0] == str[0]) {
                val pointer = str.startingStringIndex(word)
                assert(pointer != -1) {
                    "$str substring $word should not result in $pointer"
                }
                return Triple(str, pointer, _nextNode[str]!!)
            }
        }
        return Triple(null, -1, this)
    }

    private fun addWholeWord(word: String) {
        if (word.isEmpty()) return
        _nextNode[word] = CompactTrieTree().apply {
            this._word = word
            _isValidWord = true
        }
    }

    private fun replaceKey(newKey: String, foundWord: String) {
        _nextNode.apply {
            val currVal = this[foundWord]!!
            remove(foundWord)
            this[newKey] = currVal
        }
    }

    /**
    *   This breaks up the current node by splitting the curr node word into left hand side(LHS)
    *   and right hand side(RHS). It moving the curr nodes connected words into the RHS newly
    *   created node under the curr node which now only contains the LHS of the prev word.
    *   The new word you are adding- part of the new word, would then be added as a child of the
    *   LHS of the curr node
    *
    *   Eg -> old -> bark,barks : new -> bands
    *   (bark)          (ba | rk)              (ba) <- LHS
    *      |      =>        |         =>        /\
    *      v                v                  v  v
    *     (s)              (s)        RHS -> (rk)(nds) <- newPartWord
    *                                          v
    *                                         (s)
    *
     * @param word The current node's word.
     * @param suffixIndex The index at which to split the word into LHS and RHS.
     * @return The LHS of the current node's word after reconfiguration.
    */
    private fun reconfigureTree(word: String, suffixIndex: Int): String {
        val newPartWord = subString(word,suffixIndex)

        val wordLHS = subString(this._word,suffixIndex,0)
        val wordRHS = subString(this._word,suffixIndex)

        // Create the RHS node
        val nodeWordRHS = CompactTrieTree().also { nodeWordRHS ->
            nodeWordRHS._word = wordRHS
            nodeWordRHS._nextNode = this._nextNode
            nodeWordRHS._isValidWord = this._isValidWord
        }

        // Fixing the currNode-LHS members
        this._nextNode = HashMap<String, CompactTrieTree>().apply {
            this[wordRHS] = nodeWordRHS
        }
        this._word = wordLHS
        this._isValidWord = false

        // Adding newPartWord under the LHS node
        if (newPartWord.isNotEmpty()) { // If your word isn't exactly smaller than th curr word : curr -> menu, new -> mens
            this._nextNode[newPartWord] = CompactTrieTree().also {
                it._word = newPartWord
                it._isValidWord = true
            }
        } else { // If your word is exactly smaller than th curr word : curr -> menu, new -> men
            this._isValidWord = true
        }
        val str: String = "sfd"
        return wordLHS
    }

    private fun subString(word: String, endIndex: Int,startIndex: Int = -1): String {
        return  if(startIndex==-1) StringBuilder(word).substring(endIndex + 1)
                else StringBuilder(word).substring(startIndex, endIndex + 1)
    }

    private fun String.startingStringIndex(otherString: String): Int {
        var pointer = 0
        for (index in this.indices) {
            pointer = index
            // If the char keeps matching but is terminated as a result of otherString being shorter
            if (index > otherString.lastIndex) {
                return pointer - 1
            } else { // Terminates if char don't match
                if (this[index] != otherString[index]) return pointer - 1
            }
        }
        /*
         *  If all matches and is same len or the other is longer than this but with an identical
         *  full len substring
         */
        return pointer
    }
}