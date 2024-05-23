package com.example.kotrip.naver

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ModifiyTspService(size: Int, private val graph: Array<LongArray>, places: Array<Int>) {
    private var dp: Array<LongArray>
    private var path = ArrayList<Int>()
    private var placeToIndex = HashMap<Int, Int>()
    private var indexToPlace = ArrayList<Int>()

    init {
        N = size
        dp = Array(N) { LongArray(1 shl N) }
        for (i in 0 until N) {
            Arrays.fill(dp[i], 0)
        }
        for (i in 0 until N) {
            placeToIndex[places[i]] = i
            indexToPlace.add(places[i])
        }
    }

    fun printCost(s: Int, b: Int): Long {
        dfs(s, b)
        return dp[start][bit]
    }

    private fun dfs(now: Int, visited: Int): Long {
        if (visited == (1 shl N) - 1) {
            return if (graph[now][start] != 0L) 0 else INF
        }
        if (dp[now][visited] != 0L) return dp[now][visited]

        dp[now][visited] = INF
        for (i in 0 until N) {
            if (graph[now][i] == 0L || visited and (1 shl i) != 0) continue
            val temp = dfs(i, visited or (1 shl i))
            dp[now][visited] = kotlin.math.min(dp[now][visited], graph[now][i] + temp)
        }
        return dp[now][visited]
    }

    fun printPath(s: Int, visited: Int): ArrayList<Int> {
        path.add(indexToPlace[s])
        if (visited == (1 shl N) - 1) {
            path.add(indexToPlace[start])
            return path
        }

        var nextCost = INF
        var nextPath = 0
        for (i in 0 until N) {
            if (visited and (1 shl i) != 0) continue
            val currentCost = graph[s][i] + dp[i][visited or (1 shl i)]
            if (currentCost < nextCost) {
                nextCost = currentCost
                nextPath = i
            }
        }
        return printPath(nextPath, visited or (1 shl nextPath))
    }

    companion object {
        private var N: Int = 0
        private const val INF = 987654321L
        private var start = 0
        private var bit = 1
    }
}
