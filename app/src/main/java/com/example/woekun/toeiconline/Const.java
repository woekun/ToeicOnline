package com.example.woekun.toeiconline;

public class Const {

    // API URL
    public static final String BASE_URL = "http://toeic-woekun.rhcloud.com/api";
    public static final String REGISTER_URL = BASE_URL + "/v1/register";
    public static final String LOGIN_URL = BASE_URL + "/v1/login";
    public static final String GET_QUESTIONS_URL = BASE_URL + "/v1/questions/";
    public static final String UPLOAD_URL = BASE_URL + "/v1/user/avatar";
    public static final String UPDATE_USER = BASE_URL + "/v1/user";

    // Resource URL
    public static final String BASE_IMAGE_URL = BASE_URL + "/asset/image/image_part1/";
    public static final String BASE_AVATAR_URL = BASE_URL + "/asset/image/avatar/";
    public static final String BASE_AUDIO_URL = BASE_URL + "/asset/audio/";

    // Common values
    public static final int REQUEST = 1;
    public static final int PICK_IMAGE_REQUEST = 2;
    public static final String EMAIL = "email";
    public static final String TYPE = "type";
    public static final String INFO = "info";
    public static final String RANK = "rank";
    public static final String TRAIN = "training";
    public static final String TEST = "test";
    public static final String LEVEL = "level";
    public static final String POSITION = "position";
}
