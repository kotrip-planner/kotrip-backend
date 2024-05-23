package com.example.kotrip.service.road;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class TspService {
    private Integer size; // 노드의 수
    private Long[][] graph; // 그래프
    private Long[] places;
    private long[][] dp; // 동적 프로그래밍을 위한 배열
    private long INF = 987654321; // 무한대

    private int start = 0;
    private int bit = 1;
    private HashMap<Long, Integer> placeToIndex = new HashMap<>();
    private ArrayList<Long> indexToPlace = new ArrayList<>();
    private ArrayList<Long> path = new ArrayList<>();

    // 생성자
    public TspService(Integer size, Long[][] graph, Long[] places) {
        this.size = size;
        this.graph = graph;
        this.places = places;

        init();
    }

    private void init() {
        // dp[현재 노드][지금까지 방문한 노드] = 나머지 정점을 이동하고 출발 정점으로 돌아오는데 걸리는 최소 비용
        dp = new long[size][1 << size];
        for (int i = 0; i < size; i++) {
            Arrays.fill(dp[i], 0);
        }

        Long place = places[0];

        // bit-masking으로 탐색하기 위해 <관광지, number> 매핑
        for (int i = 0; i < size; i++) {
            placeToIndex.put(places[i], i);
            indexToPlace.add(places[i]);
        }
    }

    public long printCost(int s, int b) {
        dfs(s, b);
        return dp[start][bit];
    }

    public long dfs(int now, int visited) {
        // 모든 노드를 방문했을 시
        if (visited == (1 << size) - 1) {
            if (graph[now][start] != 0) // 시작점까지 가는 경로
                return 0;
            return INF; // 없으면 INF 값 반환
        }

        // 중복 경로일 시 이전 구한 값 return
        if (dp[now][visited] != 0)
            return dp[now][visited];

        // 노드 방문
        dp[now][visited] = INF; // 방문 표시, 길이 없어도 "if dp[now][visited] != 0"이 무한 재귀가 되지 않도록
        for (int i = 0; i < size; i++) {
            if (graph[now][i] == 0)
                continue;

            // i가 이전에 방문한 노드인지 판단 ("&")
            if ((visited & (1 << i)) != 0)
                continue;

            // i 노드를 방문한다. ("|")
            // 그리고, 방문하지 않은 노드들을 방문했을 때 걸리는 최소 비용을 temp에 저장한다.
            long temp = dfs(i, visited | (1 << i));

            // 현재 노드에서 i까지 비용 + temp 와 비교한다.
            dp[now][visited] = Math.min(dp[now][visited], graph[now][i] + temp);
        }

        return dp[now][visited];
    }
    public ArrayList<Long> printPath(int s, int visited) {
        
        // 경로에 number에 해당하는 관광지 추가
        path.add(indexToPlace.get(s));

        if (visited == (1 << size) - 1) {
            path.add(indexToPlace.get(start));
            return path;
        }

        long nextCost = INF;
        int nextPath = 0;

        for (int i = 0; i < size; ++i) {
            if ((visited & 1 << i) != 0)
                continue;

            // dp[i][visited] 현재 visited 에서의 최적이 들어있고, 우리가 찾는건 다음의 최적 점
            // 현재의 visited 에 하나씩 비트 붙여가면서 값을 구했을 때, 그 값들 중 최솟값이 다음 최적 점
            if (graph[s][i] + dp[i][visited | (1 << i)] < nextCost) {
                nextCost = graph[s][i] + dp[i][visited | (1 << i)];
                nextPath = i;
            }
        }

        // for loop 종료 시, nextCost 에는 남은 점들을 최적으로 돌았을 때의 최소 거리가
        // nextPath 에는 다음 점이 들어 있다.
        return printPath(nextPath, visited | (1 << nextPath));
    }
}
