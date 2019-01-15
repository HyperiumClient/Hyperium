package com.chattriggers.ctjs.utils

import com.google.common.collect.UnmodifiableListIterator
import java.util.*

object Utils {
    @JvmField
    val EMPTY_ITERATOR = object : UnmodifiableListIterator<Any>() {
        override fun hasNext(): Boolean {
            return false
        }

        override fun next(): Any {
            throw NoSuchElementException()
        }

        override fun hasPrevious(): Boolean {
            return false
        }

        override fun previous(): Any {
            throw NoSuchElementException()
        }

        override fun nextIndex(): Int {
            return 0
        }

        override fun previousIndex(): Int {
            return -1
        }
    }
}
