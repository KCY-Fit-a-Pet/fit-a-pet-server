package com.kcy.lib;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ListTest {
    @Test
    public void emptyListRemove() {
        // given
        List<String> list = new ArrayList<>();

        list.add("1");
        list.add("2");
        list.add("3");

        // when
        list.remove("4");

        // then
        System.out.println(list);
    }

}
