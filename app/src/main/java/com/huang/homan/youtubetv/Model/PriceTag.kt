package com.huang.homan.youtubetv.Model

import java.util.HashMap

class PriceTag {
    private val priceMap: MutableMap<String, Float>

    init {
        priceMap = HashMap()
    }

    fun add(item: String, price: Float?) {
        priceMap[item] = price!!
    }

    fun getPrice(item: String): Float? {
        return priceMap[item]
    }
}
