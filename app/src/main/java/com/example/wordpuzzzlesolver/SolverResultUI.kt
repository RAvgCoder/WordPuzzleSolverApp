package com.example.wordpuzzzlesolver

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordpuzzzlesolver.model.CompactTrieTree
import com.example.wordpuzzzlesolver.model.Dictionary

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ResultColumn(permutedWords: ArrayList<String>, modifier: Modifier = Modifier) {
    val resources = LocalContext.current.resources
    val dictionary by remember {
        mutableStateOf(
            Dictionary.initialize(
                resources, CompactTrieTree()
            )
        )
    }
    val validWords = permutedWords.filter(dictionary::isValidWord).sortedBy { it.length }
    val wordRange = findWordRanges(validWords)

    Column(
        modifier = modifier.padding(10.dp)
    ) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(160.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalItemSpacing = 15.dp,
        ) {
            items(wordRange) { range ->
                WordDropDown(
                    validWords = validWords, range = range, modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun WordDropDown(
    validWords: List<String>,
    range: IntRange,
    modifier: Modifier = Modifier
) {
    val wordLen = validWords[range.first].length
    var showWords by rememberSaveable { mutableStateOf(false) }
    val animatedColor by animateColorAsState(
        targetValue = if (showWords) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.primaryContainer, label = "Color"
    )
    Column(
        modifier
            .background(
                color = animatedColor, shape = RoundedCornerShape(5.dp)
            )
            .padding(
                start = 7.dp,
                end = 7.dp,
            )
            .fillMaxHeight()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showWords = !showWords
                    println(showWords)
                }
        ) {
            Text(
                text = stringResource(
                    R.string.drop_down_txt,
                    wordLen,
                    if (wordLen == 1) "Word" else "Words"
                ),
                fontWeight = FontWeight.Bold,
                fontSize = dimensionResource(id = R.dimen.text_size_result).value.sp,
                style = MaterialTheme.typography.bodySmall
            )
            Icon(
                imageVector = if (showWords) Icons.Default.KeyboardArrowUp
                else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
            )
        }

        // Shows the words when
        AnimatedVisibility(visible = showWords) {
            Divider()
            var count = 1
            Column {
                for (index in range) {
                    Text(text = "${count++}. ${validWords[index]}")
                }
            }
        }
    }
}

/**
 * Returns an array of index ranges that represent groups of words with the same lengths
 * in the given list of words.
 *
 * For example:
 * - Given words: {"a", "ab", "fd", "dfd", "yhr", "ererr", "rerere"}
 * - Resulting ranges: {0-0, 1-2, 3-4, 5-5, 6-6}
 *
 * @param validWords The list of words for which to find the ranges.
 * @return An array of IntRange objects representing the index ranges of words with the same lengths.
 */
@OptIn(ExperimentalStdlibApi::class)
fun findWordRanges(validWords: List<String>): Array<IntRange> {
    if (validWords.isEmpty()) return emptyArray()
    var indexHolder = 0
    var lastKnownLen = 0
    val intRangeList = mutableListOf<IntRange>()

    for (index in validWords.indices) {
        if (lastKnownLen != validWords[index].length) {
            intRangeList.add(indexHolder..<index)
            indexHolder = index
            lastKnownLen = validWords[index].length
        }
    }

    intRangeList.add(indexHolder..validWords.lastIndex)
    return intRangeList.apply { removeFirst() }.toTypedArray()
}


@Preview(showBackground = true)
@Composable
fun ResultColumnPreview() {
    ResultColumn(
        permutedWords = arrayListOf(
            "at",
            "yu",
            "za",
            "booked",
            "happiness",
            "illusion",
            "jungle",
            "ocean",
            "penguin",
            "wonder",
            "xylophone",
        )
    )
}
