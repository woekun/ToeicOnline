package com.example.woekun.toeiconline;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.woekun.toeiconline.customs.CustomRequest;
import com.example.woekun.toeiconline.models.Question;
import com.example.woekun.toeiconline.models.SubQuestion;
import com.example.woekun.toeiconline.models.User;
import com.example.woekun.toeiconline.ui.fragments.QuestionFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class APIs {
    // common value
    private static final String ERROR = "error";
    private static final String MESSAGE = "message";
    private static final String FALSE = "false";

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


    public static void getQuestions(final String levelId, final DataCallBack dataCallBack) {

        final ArrayList<Question> questions = new ArrayList<>();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Const.GET_QUESTIONS_URL+levelId,
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

    public interface DataCallBack {
        void onSuccess(ArrayList<Question> questions);
        void onFailed(VolleyError error);
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
                                        password, response.getString(AVATAR),
                                        response.getString(LEVEL), null, null);
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

    public interface LoginCallBack {
        void onSuccess(User user);
        void onFailed(String message);
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

    public interface RegisterCallBack extends LoginCallBack {
    }

}
