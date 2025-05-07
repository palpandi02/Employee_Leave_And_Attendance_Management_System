package com.example.demo.util;

import java.util.HashMap;
import java.util.Map;

public class LeaveTypes {

    public static Map<String, Integer> leaves() {
        Map<String, Integer> map = new HashMap<>();
        map.put("Sick Leave", 10);
        map.put("Casual Leave", 8);
        map.put("Earned Leave", 15);
        return map;
    }
}