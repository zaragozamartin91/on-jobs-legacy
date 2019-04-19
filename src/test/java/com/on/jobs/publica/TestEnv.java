package com.on.jobs.publica;

import java.util.HashMap;
import java.util.Map;

public enum TestEnv {
    INSTANCE;

    TestEnv() {
        System.out.println("building shit");
    }

    private final Map<String, Object> vars = new HashMap<>();

    public Object get(Object key) {return vars.get(key);}

    public Object put(String key, Object value) {return vars.put(key, value);}
}
