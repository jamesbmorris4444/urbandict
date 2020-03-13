package com.fullsekurity.urbandict.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.fullsekurity.urbandict.repository.storage.Meaning

class Utils {

    companion object {
        fun hideKeyboard(view: View?) {
            if (view == null) return
            val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun showKeyboard(view: View?) {
            if (view == null) return
            val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.showSoftInput(view, InputMethodManager.SHOW_FORCED)
        }

//        fun donorEquals(donor: Meaning, otherMeaning: Meaning): Boolean {
//            return donor.lastName == otherMeaning.lastName && donor.firstName == otherMeaning.firstName && donor.middleName == otherMeaning.middleName && donor.dob == otherMeaning.dob
//        }

        fun donorComparisonByThumbsUp(meaning: Meaning): Int {
            return meaning.thumbsUp
        }

        fun donorComparisonByThumbsDown(meaning: Meaning): Int {
            return meaning.thumbsDown
        }

        fun newPatternOfSubpatterns(patternOfSubpatterns: String, index: Int, newPattern: String): String {
            // patternOfSubpatterns = P|P|P|...|P
            // if there are N subpatterns then index = 0 to N-1
            // example of usage: newPatternOfSubpatterns("aaaa|bbbb|cccc|dddd", 2, "xxxxxxxx")
            // will return the string value: "aaaa|bbbb|xxxxxxxx|dddd"
            val split: MutableList<String> = patternOfSubpatterns.split('|').toMutableList()
            val stringBuilder = StringBuilder()
            split[index] = newPattern
            for (newIndex in split.indices) {
                stringBuilder.append(split[newIndex])
                if (newIndex < split.size - 1) {
                    stringBuilder.append('|')
                }
            }
            return stringBuilder.toString()
        }

        fun getPatternOfSubpatterns(patternOfSubpatterns: String, index: Int): String {
            // patternOfSubpatterns = P|P|P|...|P|
            // if there are N subpatterns then index = 0 to N-1
            // example of usage: getPatternOfSubpatterns("aaaa|bbbb|cccc|dddd", 2)
            // will return the string value: "cccc"
            val split: MutableList<String> = patternOfSubpatterns.split('|').toMutableList()
            return split[index]
        }

    }

}