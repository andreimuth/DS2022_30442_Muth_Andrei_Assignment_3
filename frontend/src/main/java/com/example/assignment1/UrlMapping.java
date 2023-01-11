package com.example.assignment1;

public class UrlMapping {
    public static final String AUTH = "/auth";
    public static final String SIGN_IN = "/sign-in";
    public static final String SIGN_UP = "/sign-up";
    public static final String USERS = "/users";
    public static final String FIND_ALL = "/find-all";
    public static final String DEVICES = "/devices";
    public static final String CREATE_DEVICE = "/create-device";
    public static final String UPDATE_DEVICE = "/update-device/{id}";
    public static final String DELETE_DEVICE = "/delete-device/{id}";
    public static final String CREATE_USER = "/create-user";
    public static final String UPDATE_USER = "/update-user/{id}";
    public static final String DELETE_USER = "/delete-user/{id}";
    public static final String ROLES = "/roles";
    public static final String FIND_BY_OWNER = "/find-by-owner/{id}";
    public static final String ENERGY_CONSUMPTION = "/energy-consumption";
    public static final String GET_CONSUMPTION_FOR_DEVICE = "/get-consumption-for-device/{id}/{date}";
    public static final String FIND_NON_ADMINS = "/find-non-admins";

}
