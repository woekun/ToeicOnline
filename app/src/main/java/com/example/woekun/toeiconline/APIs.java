package com.example.woekun.toeiconline;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.woekun.toeiconline.customs.CustomRequest;
import com.example.woekun.toeiconline.models.Question;
import com.example.woekun.toeiconline.models.SubQuestion;
import com.example.woekun.toeiconline.models.User;
import com.example.woekun.toeiconline.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;


public class APIs {
    // common value
    private static final String ERROR = "error";
    private static final String MESSAGE = "message";
    private static final String FALSE = "false";
    private static final String IMAGE_NAME = "image_name";

    /**
     * get(../v1/questions/level/part)
     */
    private static final String QUESTIONS = "questions";
    private static final String ID = "id";
    private static final String PART = "part";
    private static final String PARAGRAPH = "paragraph";
    private static final String AUDIO = "audio";
    private static final String IMAGE = "image";
    private static final String SUB_QUESTIONS = "subQuestions";
    private static final String A = "A";
    private static final String B = "B";
    private static final String C = "C";
    private static final String D = "D";
    private static final String EXPLAIN_ANSWER = "explain_answer";
    private static final String CONTENT = "content";
    private static final String ANSWER = "answer";

    /**
     * post(../v1/login)
     */
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String AVATAR = "avatar";
    private static final String LEVEL = "level";
    private static final String NAME = "name";
    private static final String ADDRESS = "address";
    private static final String PHONE = "phone";


    public static void getQuestions(final String levelId, final DataCallBack dataCallBack) {

        final ArrayList<Question> questions = new ArrayList<>();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Const.GET_QUESTIONS_URL + levelId,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsArray = response.getJSONArray(QUESTIONS);
                            for (int i = 0; i < jsArray.length(); i++) {
                                JSONObject jsOb = jsArray.getJSONObject(i);

                                int questionID = Integer.valueOf(jsOb.getString(ID));
                                String partId = jsOb.getString(PART);
                                String paragraph = jsOb.getString(PARAGRAPH);
                                String audioPath = jsOb.getString(AUDIO);
                                String imagePath = jsOb.getString(IMAGE);
                                JSONArray subQuests = jsOb.getJSONArray(SUB_QUESTIONS);

                                ArrayList<SubQuestion> subQuestions = new ArrayList<>();
                                for (int j = 0; j < subQuests.length(); j++) {
                                    JSONObject subQuestion = subQuests.getJSONObject(j);

                                    ArrayList<String> answerList = new ArrayList<>();
                                    answerList.add("A. " + subQuestion.getString(A));
                                    answerList.add("B. " + subQuestion.getString(B));
                                    answerList.add("C. " + subQuestion.getString(C));
                                    answerList.add("D. " + subQuestion.getString(D));

                                    String explainAnswer = subQuestion.getString(EXPLAIN_ANSWER);

                                    subQuestions.add(
                                            new SubQuestion(questionID, subQuestion.getString(CONTENT),
                                                    answerList, Integer.valueOf(subQuestion.getString(ANSWER)), explainAnswer));
                                }
                                questions.add(new Question(questionID,
                                        Integer.valueOf(levelId),
                                        Integer.valueOf(partId),
                                        paragraph, subQuestions, imagePath, audioPath));
                            }
                            dataCallBack.onSuccess(questions);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dataCallBack.onFailed(error);
                    }
                });
        AppController.getInstance().addToRequestQueue(request);
    }

    public static void login(final String email, final String password, final LoginCallBack loginCallBack) {
        Map<String, String> params = new HashMap<>();
        params.put(EMAIL, email);
        params.put(PASSWORD, password);

        CustomRequest stringRequest = new CustomRequest(Request.Method.POST, Const.LOGIN_URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String error = response.getString(ERROR);
                            if (error.equals(FALSE)) {
                                User user = new User(email, response.getString(NAME),
                                        response.getString(AVATAR),
                                        response.getString(LEVEL),
                                        response.getString(ADDRESS),
                                        response.getString(PHONE));
                                loginCallBack.onSuccess(user);

                            } else
                                loginCallBack.onFailed(response.getString(MESSAGE));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loginCallBack.onFailed(e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        loginCallBack.onFailed(error.getMessage());
                    }
                });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public static void register(final String email, String password, final RegisterCallBack registerCallBack) {
        Map<String, String> params = new HashMap<>();
        params.put(EMAIL, email);
        params.put(PASSWORD, password);

        CustomRequest stringRequest = new CustomRequest(Request.Method.POST, Const.REGISTER_URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String error = response.getString(ERROR);
                            if (error.equals(FALSE)) {
                                User user = new User(email, "1");
                                registerCallBack.onSuccess(user);
//
                            } else
                                registerCallBack.onFailed(response.getString(MESSAGE));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        registerCallBack.onFailed(error.getMessage());
                    }
                });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public static void uploadImage(final String filePath, final String email, final UploadCallback uploadCallback) {
        final String image = Utils.getStringImage(BitmapFactory.decodeFile(filePath));
        final String subNameImage = "_avatar_" + new Random().nextInt();

        Map<String, String> params = new Hashtable<>();
        params.put(EMAIL, email);
        params.put(IMAGE_NAME, email + subNameImage);
        params.put(IMAGE, image);

        CustomRequest stringRequest = new CustomRequest(Request.Method.POST, Const.UPLOAD_URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            uploadCallback.onSuccess(response.getString("notify"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        uploadCallback.getImageLink(Const.BASE_AVATAR_URL + email + subNameImage + ".png");

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                });

        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    public static void updateUser(final Context context, final User user) {
        Map<String, String> params = new Hashtable<>();
        params.put(EMAIL, user.getEmail());
        params.put(NAME, user.getName());
        params.put(LEVEL, user.getLevel());
        params.put(ADDRESS, user.getAddress());
        params.put(PHONE, user.getPhone());

        CustomRequest stringRequest = new CustomRequest(Request.Method.POST, Const.UPDATE_USER, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject s) {
                        Toast.makeText(context, s.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public interface DataCallBack {
        void onSuccess(ArrayList<Question> questions);

        void onFailed(VolleyError error);
    }

    public interface LoginCallBack {
        void onSuccess(User user);

        void onFailed(String message);
    }

    public interface UploadCallback {
        void onSuccess(String message);

        void getImageLink(String link);

    }

    public interface RegisterCallBack extends LoginCallBack {
    }



}
