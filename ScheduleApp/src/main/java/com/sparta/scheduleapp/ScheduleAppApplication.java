package com.sparta.scheduleapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScheduleAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScheduleAppApplication.class, args);

    }
    public int[][] solution(int[][] arr1, int[][] arr2) {
        int[][] answer = {};
        for(int i = 0; i< arr1.length; i++) {
            if (arr1[i].length == 0) {
                answer[i][0] = arr1[i][0] + arr2[i][0];
            }
            for(int j = 0; j<arr1[i].length; j++) {
                answer[i][j] = arr1[i][j] + arr2[i][j];
            }
        }
        return answer;
    }
}
