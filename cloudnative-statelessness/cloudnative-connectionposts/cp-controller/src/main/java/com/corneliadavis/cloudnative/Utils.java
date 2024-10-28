package com.corneliadavis.cloudnative;

public interface Utils {

    public static String ipTag(String ip, String port) {
        return "[" + ip + ":" + port + "] ";
    }

    static final String POST_AGGREGATOR_QUEUE = "POST_AGGREGATOR_QUEUE";
}
