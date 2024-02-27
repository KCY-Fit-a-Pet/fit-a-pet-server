package kr.co.fitapet.common;

import org.junit.jupiter.api.Test;

public class StringFormatTest {
    @Test
    public void plainFormatter() {
        String format = "안녕하세요, %s님. %s에 오신 것을 환영합니다.";
        String name = "홍길동";
        String time = "오늘";
        String result = String.format(format, name, time);
        System.out.println(result);
    }

    @Test
    public void noneMatchingArgument() {
        String format = "안녕하세요, %s님. 오신 것을 환영합니다.";
        String name = "홍길동";
        String time = "오늘";
        String result = String.format(format, name, time);
        System.out.println(result);
    }
}
