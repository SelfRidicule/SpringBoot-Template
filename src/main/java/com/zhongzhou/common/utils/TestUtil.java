package com.zhongzhou.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestUtil {

    public static void main(String[] args) {

        List<Apple> list = new ArrayList<>();
        list.add(new Apple("苹果1" , "100"));
        list.add(new Apple("苹果1" , "101"));
        list.add(new Apple("苹果2" , "102"));
        list.add(new Apple("苹果2" , "103"));


        Map<String, List<Apple>> collect = list.stream().collect(Collectors.groupingBy(Apple::getName));
        System.out.println(collect);
    }

}

class Apple {
    public Apple(String name, String pice) {
        this.name = name;
        this.pice = pice;
    }

    String name;
    String pice;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPice() {
        return pice;
    }

    public void setPice(String pice) {
        this.pice = pice;
    }
}
