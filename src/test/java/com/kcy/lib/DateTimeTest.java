package com.kcy.lib;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Queue;

public class DateTimeTest {
    @Test
    public void test() {
        // given
        LocalDate twoYearsAgo = LocalDate.now().minusYears(2).plusMonths(1);
        int year = twoYearsAgo.getYear();
        int month = twoYearsAgo.getMonthValue();
        int day = twoYearsAgo.getDayOfMonth();

        LocalDate now = LocalDate.now();
        int nowYear = now.getYear();
        int nowMonth = now.getMonthValue();
        int nowDay = now.getDayOfMonth();

        // when
        // 현재 시간 기준으로 만 나이 계산
        int age = nowYear - year;
        if (month > nowMonth || (month == nowMonth && day > nowDay)) {
            --age;
        }

        // then
        System.out.println("age = " + age);
    }
}
