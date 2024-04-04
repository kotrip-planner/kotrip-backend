package com.example.kotrip.naver.sample

import com.example.kotrip.dto.NewNode
import com.example.kotrip.dto.Node

val sampleNodeList: List<Node>
    get() = listOf<Node>(sampleNode1, sampleNode2, sampleNode3, sampleNode4)

fun List<Node>.suffleNewNode(): List<NewNode> {
    val newList = mutableListOf<NewNode>()
    this.forEachIndexed { i, iNode ->
        this.forEachIndexed { j, jNode ->
            if (i != j) {
                val node = NewNode(
                    startId = iNode.id,
                    startName = iNode.name,
                    startLatitude = iNode.latitude,
                    startLongitude = iNode.longitude,
                    destId = jNode.id,
                    destName = jNode.name,
                    destLatitude = jNode.latitude,
                    destLongitude = jNode.longitude,
                    time = iNode.time
                )
                newList.add(node)
            }
        }
    }
    return newList
}

val sampleNode1 = Node(0, "동대문", 126.87691713713438, 37.577536707517076, 1)
val sampleNode2 = Node(1, "서대문", 126.9780711, 36.909783215, 1)
val sampleNode3 = Node(2, "남대문", 127.8830711, 36.6163215, 2)
val sampleNode4 = Node(3, "북대문", 128.9180711, 37.1963215, 2)

