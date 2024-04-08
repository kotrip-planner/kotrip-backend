package com.example.kotrip.naver

import com.example.kotrip.dto.Node
import com.example.kotrip.naver.sample.suffleNewNode
import io.netty.channel.ChannelOption
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import java.time.Duration

@Service
class OptimalDurationService {

    var clientId: String = "ncxf9y01px";
    var clientSecret: String = "aWz84CcrXTVDMSZULVD9HH4Z3J2sWdge5zBYaMQw";

    /**
     * 관광지 최적의 경로 리스트
     * @param : NaverRequestDto
     * @return : Mono<List<List<Int>>>
     *     example : [[1,2,3,4,1], [5,6,7,8,5] ...]
      */
    fun getDriving(naverRequestDto: NaverRequestDto): Mono<List<List<Int>>> {
        val httpClient: HttpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10_000)
            .responseTimeout(Duration.ofMillis(10_000))

        val webClient = WebClient
            .builder()
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .baseUrl("https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()

        // 모든 kotrips의 모든 nodes를 순회하기 위해 flatMap을 사용하여 평탄화
        val nodes = naverRequestDto.kotrip.flatMap { it.nodes.suffleNewNode() }

        val uniqueNodes = naverRequestDto.kotrip.flatMap { it.nodes }
            .groupBy { it.time } // time 기준으로 그룹화

        val idDoubleList: List<List<Int>> = uniqueNodes.map { (_, value) ->
            value.map { it.id }
        }

        // 각 요청에 대한 Mono를 생성하고, Flux로 변환하여 모든 요청을 처리. 그 후, 결과를 리스트로 수집
        val responseMapList = Flux.fromIterable(nodes)
            .flatMap { node ->
                webClient
                    .get()
                    .uri { uriBuilder ->
                        uriBuilder.path("")
                            .queryParam("start", "${node.startLongitude},${node.startLatitude}")
                            .queryParam("goal", "${node.destLongitude},${node.destLatitude}")
                            .queryParam("option", "trafast")
                            .build()
                    }
                    .header("X-NCP-APIGW-API-KEY-ID", clientId)
                    .header("X-NCP-APIGW-API-KEY", clientSecret)
                    .retrieve()
                    .bodyToMono(NaverResponseDto::class.java)
                    .map {
                        mapOf("${node.time}-${node.startId}-${node.destId}" to it.route?.trafast?.get(0)?.summary?.duration)
                    }
                    .doOnError {
                        println(it.message)
                    }
            }.collectList() // 모든 Mono의 결과를 리스트로 수집

        // 결과 처리 예시
        return responseMapList.flatMap { sortedResultList ->

            val results = idDoubleList.map { list ->
                Array(list.size) { LongArray(list.size) { 0 } }
            }

            // 맵 리스트를 순회하면서 결과 이중 리스트를 채움
            for (map in sortedResultList) {
                for ((key, value) in map) {
                    val (listIndex, from, to) = key.split('-').map { it.toInt() }
                    val fromIndex = idDoubleList[listIndex - 1].indexOf(from)
                    val toIndex = idDoubleList[listIndex - 1].indexOf(to)
                    if (fromIndex != -1 && toIndex != -1) {
                        // 해당 위치에 값을 할당
                        results[listIndex - 1][fromIndex][toIndex] = value ?: 0
                    }
                }
            }


            /**
             * 인접 행렬 출력하기
             */
//            // 결과 출력
//            for (result in results) {
//                for (row in result) {
//                    println(row.joinToString(" "))
//                }
//                println() // 각 결과 리스트 사이에 공백을 추가하여 구분
//            }
//
//            // 결과 출력
//            results.forEach { row ->
//                println(row.map { it?.toString() ?: "null" }.joinToString(", "))
//            }

            val resultList = mutableListOf<List<Int>>()

            idDoubleList.forEachIndexed { index, ints ->
                val tsp = TspService(ints.size, results.get(index), ints.toTypedArray())
                val start = 0
                val bit = 1
                val cost= tsp.printCost(start, bit)
                val list = tsp.printPath(start, bit)

                list.removeLast()
                resultList.add(list)
            }

            Mono.just(resultList)
        }
    }

    /**
     * 숙박 편의성을 고려한 전체 일정 최적의 경로 리스트
     * @param : naverRequestDto : NaverRequestDto
     * @param : optimalTourList : List<List<Int>>
     * @return : Mono<List<List<Int>>>
     *     example) [[1,2,3],[4,5,6],[7,8,9]...]
     */
    fun getOptimalRoute(naverRequestDto: NaverRequestDto, optimalTourList:List<List<Int>>): Mono<List<List<Int>>> {
        val httpClient: HttpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10_000)
            .responseTimeout(Duration.ofMillis(10_000))

        val webClient = WebClient
            .builder()
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .baseUrl("https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()

        // time 기준으로 노드 그룹화
        val groupedByTime = naverRequestDto.kotrip.flatMap { it.nodes }
            .groupBy { it.time }
            .toSortedMap()

        val responseMapList = Flux.fromIterable(groupedByTime.keys)
            .flatMap { currentTime ->
                val currentNodes = groupedByTime[currentTime] ?: emptyList()
                val nextTime = currentTime + 1
                val prevTime = currentTime - 1
                val nextNodes = groupedByTime[nextTime] ?: emptyList()
                val prevNodes = groupedByTime[prevTime] ?: emptyList()

                    Flux.fromIterable(currentNodes)
                    .flatMap { currentNode ->
                        Flux.fromIterable(nextNodes)
                            .flatMap { nextNode ->
                                createWebClientRequest(webClient, currentNode, nextNode, currentTime, nextTime)
                            }
                    }
            }.collectList()

        return responseMapList.map { sortedResultList ->
            // 결과 리스트 처리 로직
            val data = groupPathsByDay(sortedResultList)
            println(data)

            /**
             * {1=[{1-2=1-5=1255}, ...] 1일차에서 2일차로 가는 경로들 가져오기
             */
            val keyOneData = data[1]

            val tourList = mutableListOf<List<Int>>()


            keyOneData?.let {
                /**
                 * {1-2=1-5=1255}, {1-2=1-4=5154}, {1-2=2-5=3124} ... 의 값 중 =1255 최소 값
                 * 즉, 위에서는 {1-2=1-5=1255} 가져옴
                 */
                // List<Map<String, Long>>에서 최소 Long 값을 가진 Map 항목 찾기
                val minEntry = it.minByOrNull { map -> map.values.first() }

                /**
                 * 1-2=1-5=12355 (1일차에서 2일차로 가는 경로에서 1일차 관광지 1과 2일차 관광지 5의 경로 시간이 12355ms 이다.
                 * 1-5 부분 가져오기
                 */
                val keyPart = minEntry?.keys?.first()?.split("=")?.get(1) ?: ""

                /**
                 * 1-5 에서  - 분할
                 * 1일차 목적지 -> 1
                 * 2일차 출발지 -> 5
                 */
                val (start, end) = keyPart.split("-").let {
                    it[0].toInt() to it[1].toInt()
                }

                /**
                 * [1.2.3] 에서 1이 목적지 이면 -> 이동
                 * [2,3,1] 만들기
                 */
                val currentTourList = turnRightIndex(optimalTourList, start)
                tourList.add(currentTourList)

                /**
                 * [2,1,3] 에서 1이 출발지 이면 <- 이동
                 * [1,3,2] 만들기
                 */
                val nextTourList = turnLeftIndex(optimalTourList, end)
                tourList.add(nextTourList)
            }

            println(tourList)


            /**
             * 1일차와 2일차는 명시적으로 위에서 진행,
             * 이유는 1일차의 목적지와 2일차의 출발지를 구하면 암묵적으로 2일차의 목적지가 구해짐
             * 즉 해당 목적지에서 다음 일정의 가까운 경로를 찾으면 해당 경로에 위치한 관광지고 목적지가 되는 것
             *
             * 3일차부터 있는 지 판별
             * 그리고 위와 비슷한 과정을 통해서 목적지에서 가장 가까운 관광지를 찾아
             * 해당 일차의 출발지로 선정
             * -> 반복
             */
            while (data[tourList.size]?.isNotEmpty() == true) {
                val backDestination = optimalTourList[tourList.size - 1].last()
                optimalTourList[tourList.size]

                data[tourList.size]?.let {
                    val filteredItems = it.filter { it.keys.first().split("=")[1].startsWith("$backDestination-") }

                    val minItem = filteredItems.minByOrNull { it.values.first() }

                    val suffix = minItem?.keys?.first()?.split("=")?.get(1)?.split("-")?.get(1)?.toInt()

                    val tripList = optimalTourList[tourList.size].toMutableList()
                    var tripIndex = tripList.indexOf(suffix)

                    while (tripIndex > 0) {
                        val temp = tripList[tripIndex]
                        tripList[tripIndex] = tripList[tripIndex - 1]
                        tripList[tripIndex -1] = temp

                        tripIndex --
                    }

                    tourList.add(tripList)
                }
            }

            tourList
        }
    }

    private fun turnRightIndex(optimalTourList: List<List<Int>>, destination: Int): List<Int> {
        val list = optimalTourList[0].toMutableList()
        var currentIndex = list.indexOf(destination)
        while (currentIndex < list.size - 1) {
            val temp = list[currentIndex]
            list[currentIndex] = list[currentIndex + 1]
            list[currentIndex + 1] = temp

            currentIndex ++
        }
        return list
    }

    private fun turnLeftIndex(optimalTourList: List<List<Int>>, start: Int): List<Int> {
        val list = optimalTourList[1].toMutableList()
        var twoCurrentIndex = list.indexOf(start)

        while (twoCurrentIndex > 0) {
            val temp = list[twoCurrentIndex]
            list[twoCurrentIndex] = list[twoCurrentIndex - 1]
            list[twoCurrentIndex -1] = temp

            twoCurrentIndex --
        }
        return list
    }

    private fun createWebClientRequest(
            webClient: WebClient,
            startNode: Node,
            endNode: Node,
            startTime: Int,
            endTime: Int
    ): Mono<Map<String, Long>> {
        return webClient
            .get()
            .uri { uriBuilder ->
                uriBuilder.path("")
                    .queryParam("start", "${startNode.longitude},${startNode.latitude}")
                    .queryParam("goal", "${endNode.longitude},${endNode.latitude}")
                    .queryParam("option", "trafast")
                    .build()
            }
            .header("X-NCP-APIGW-API-KEY-ID", clientId)
            .header("X-NCP-APIGW-API-KEY", clientSecret)
            .retrieve()
            .bodyToMono(NaverResponseDto::class.java)
            .map { mapOf("${startTime}-${endTime}=${startNode.id}-${endNode.id}" to ( it.route?.trafast?.get(0)?.summary?.duration ?: 0L)) }
            .doOnError { println(it.message) }
    }


    fun groupPathsByDay(sortedResultList: MutableList<Map<String, Long>>): MutableMap<Int, MutableList<Map<String, Long>>> {
        // 결과를 저장할 맵 초기화
        val resultMap = mutableMapOf<Int, MutableList<Map<String, Long>>>()

        val sortedList = sortedResultList.sortedBy { map ->
            map.keys.firstOrNull()?.let { key ->
                extractFirstNumber(key)
            } ?: Int.MAX_VALUE
        }

        sortedList.forEach { map ->
            // 각 항목을 순회하여 키 추출
            val key = map.keys.first()
            // 키에서 숫자만 추출하여 리스트로 변환
            val numbers = key.split("=", "-").filter { it.any(Char::isDigit) }.map { it.toInt() }
            // 가장 작은 숫자를 기준으로 그룹화
            val day = numbers.minOrNull() ?: 0

            resultMap.getOrPut(day) { mutableListOf() }.add(map)
        }

        return resultMap
    }

    fun extractFirstNumber(key: String): Int {
        val parts = key.split("-")
        return parts[0].toInt()
    }
}