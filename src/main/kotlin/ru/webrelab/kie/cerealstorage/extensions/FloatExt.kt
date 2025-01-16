package ru.webrelab.kie.cerealstorage.extensions

import kotlin.math.abs

fun Float.deltaEquals(other: Float, delta: Float = 0.01f) : Boolean {
    return (abs(this-other) < delta)
}