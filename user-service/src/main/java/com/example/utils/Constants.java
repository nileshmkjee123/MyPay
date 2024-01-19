package com.example.utils;

public class Constants {
    //kafka related constant
    public static  final  String USER_CREATED_TOPIC = "user_created";
    //redis related constant
    public static  final  String USER_REDIS_KEY_PREFIX = "usr::";
    public static  final  Long USER_REDIS_KEY_EXPIRY = 86400L;
    //constants related to spring security authorities
    public static final String USER_AUTHORITY="usr";
    public static final String SERVICE_AUTHORITY="srv";
    public static final String AUTHORITIES_DELIMITER=":";
}
