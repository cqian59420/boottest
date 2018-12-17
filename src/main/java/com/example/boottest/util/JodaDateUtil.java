package com.example.boottest.util;

import org.joda.time.LocalDate;

public class JodaDateUtil {

    public static void main(String[] args) {
        System.out.println(LocalDate.now().toString("yyyy:MM:dd"));
    }
}
