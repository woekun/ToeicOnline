package com.example.woekun.toeiconline;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.woekun.toeiconline.models.Progress;
import com.example.woekun.toeiconline.models.Question;
import com.example.woekun.toeiconline.models.SubQuestion;
import com.example.woekun.toeiconline.models.User;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final static int DB_VERSION = 1;
    private final static String DB_NAME = "TOEIC.db";

    // shared column
    private final static String ID = "_id";
    private final static String LEVEL = "level";
    private final static String EMAIL = "email";

    // Table User columns
    private final static String TABLE_USER = "user";
    private final static String NAME = "name";
    private final static String AVATAR = "avatar";
    private static final String PHONE = "phone";
    private static final String ADDRESS = "address";

    // Table Question columns
    private final static String TABLE_QUESTION = "question";
    private final static String PART = "part";
    private final static String PARAGRAPH = "paragraph";
    private final static String IMAGE = "image";
    private final static String AUDIO = "audio";

    // Table SubQuestion columns
    private final static String TABLE_SUB_QUESTION = "sub_question";
    private final static String QUESTION_ID = "question_id";
    private final static String CONTENT = "content";
    private final static String A = "A";
    private final static String B = "B";
    private final static String C = "C";
    private final static String D = "D";
    private final static String RESULT = "result";
    private final static String EXPLAIN_ANSWER = "explain_answer";

    // Table Progress train/test
    private final static String TABLE_PROGRESS_TEST = "progress_test";
    private final static String TABLE_PROGRESS = "progress";
    private final static String SUB_QUESTION_ID = "sub_question_id";
    private final static String ANSWER_PICKED = "answer_picked";
    private final static String TRUE = "true";

    // Table Score
    private final static String TABLE_SCORE = "score_table";
    private final static String SCORE_PART1 = "score_part1";
    private final static String SCORE_PART2 = "score_part2";
    private final static String SCORE_PART3 = "score_part3";
    private final static String SCORE_PART4 = "score_part4";
    private final static String SCORE_PART5 = "score_part5";
    private final static String SCORE_PART6 = "score_part6";
    private final static String SCORE_PART7 = "score_part7";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EMAIL + " TEXT,"
                + NAME + " TEXT,"
                + AVATAR + " TEXT,"
                + LEVEL + " INTEGER,"
                + ADDRESS + " TEXT,"
                + PHONE + " TEXT)";

        String CREATE_QUESTION_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_QUESTION + "("
                + ID + " INTEGER PRIMARY KEY,"
                + LEVEL + " INTEGER,"
                + PART + " INTEGER,"
                + PARAGRAPH + " TEXT,"
                + IMAGE + " TEXT,"
                + AUDIO + " TEXT)";

        String CREATE_SUB_QUESTION_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_SUB_QUESTION + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + QUESTION_ID + " INTEGER,"
                + CONTENT + " TEXT,"
                + A + " TEXT,"
                + B + " TEXT,"
                + C + " TEXT,"
                + D + " TEXT,"
                + RESULT + " TEXT,"
                + EXPLAIN_ANSWER + " TEXT)";

        String CREATE_PROGRESS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PROGRESS + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EMAIL + " TEXT,"
                + PART + " INTEGER,"
                + SUB_QUESTION_ID + " INTEGER UNIQUE,"
                + ANSWER_PICKED + " INTEGER)";


        String CREATE_PROGRESS_TEST_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PROGRESS_TEST + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PART + " INTEGER,"
                + SUB_QUESTION_ID + " INTEGER UNIQUE,"
                + ANSWER_PICKED + " INTEGER,"
                + TRUE + " INTEGER DEFAULT 0)";

        String CREATE_SCORE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_SCORE + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EMAIL + " TEXT,"
                + SCORE_PART1 + " INTEGER,"
                + SCORE_PART2 + " INTEGER,"
                + SCORE_PART3 + " INTEGER,"
                + SCORE_PART4 + " INTEGER,"
                + SCORE_PART5 + " INTEGER,"
                + SCORE_PART6 + " INTEGER,"
                + SCORE_PART7 + " INTEGER)";


        String CREATE_INDEX_QUESTION = "CREATE INDEX " + ID + " ON " + TABLE_SUB_QUESTION + "(" + QUESTION_ID + ")";

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_QUESTION_TABLE);
        db.execSQL(CREATE_SUB_QUESTION_TABLE);
        db.execSQL(CREATE_INDEX_QUESTION);
        db.execSQL(CREATE_PROGRESS_TABLE);

        db.execSQL(CREATE_PROGRESS_TEST_TABLE);
        db.execSQL(CREATE_SCORE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop old table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUB_QUESTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRESS_TEST);
        // recreate database
        onCreate(db);
    }

    /**
     * Table User methods
     */
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, user.getName());
        values.put(PHONE, user.getPhone());
        values.put(EMAIL, user.getEmail());
        values.put(AVATAR, user.getAvatar());
        values.put(ADDRESS, user.getAddress());
        values.put(LEVEL, user.getLevel());

        db.insert(TABLE_USER, null, values);
    }

    public User getUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, new String[]{EMAIL, NAME, AVATAR, LEVEL, ADDRESS, PHONE},
                EMAIL + "=?", new String[]{email}, null, null, null, null);
        User user = null;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            user = new User(cursor.getString(0),
                    cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4),
                    cursor.getString(5));
            cursor.close();
        }
        return user;
    }

    public ArrayList<String> getAllUsersEmail() {
        ArrayList<String> emailList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, new String[]{EMAIL},
                null, null, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    // Adding contact to list
                    emailList.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return emailList;
    }

    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, user.getName());
        values.put(AVATAR, user.getAvatar());
        values.put(LEVEL, user.getLevel());
        values.put(ADDRESS, user.getAddress());
        values.put(PHONE, user.getPhone());
        return db.update(TABLE_USER, values, EMAIL + " = ?",
                new String[]{user.getEmail()});
    }

    /**
     * Table Question methods
     */
    public void addQuestion(ArrayList<Question> questions) {
        SQLiteDatabase db = this.getWritableDatabase();
        onUpgrade(db, 1, 1);

        for (Question question : questions) {
            ContentValues values = new ContentValues();
            values.put(ID, question.getQuestionID());
            values.put(LEVEL, question.getLevel());
            values.put(PART, question.getPart());
            values.put(PARAGRAPH, question.getParagraph());
            values.put(IMAGE, question.getImage());
            values.put(AUDIO, question.getAudio());

            db.insert(TABLE_QUESTION, null, values);
            addSubQuestion(question.getSubQuestionList());
        }
    }

    public ArrayList<Question> getQuestions(String level, String part) {
        ArrayList<Question> questions = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_QUESTION, new String[]{ID, PARAGRAPH, IMAGE, AUDIO},
                LEVEL + "=? AND " + PART + "=?", new String[]{level, part}, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Question question = new Question();
                    question.setQuestionID(cursor.getInt(0));
                    question.setLevel(Integer.parseInt(level));
                    question.setPart(Integer.parseInt(part));
                    question.setParagraph(cursor.getString(1));
                    question.setImage(cursor.getString(2));
                    question.setAudio(cursor.getString(3));
                    question.setSubQuestionList(getSubQuestion(String.valueOf(cursor.getInt(0))));
                    questions.add(question);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return questions;
    }

    public ArrayList<Question> getQuestionsForTest(String level, String part) {
        ArrayList<Question> questions = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_QUESTION, new String[]{ID, PARAGRAPH, IMAGE, AUDIO},
                LEVEL + "<=? AND " + PART + "=?", new String[]{level, part}, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Question question = new Question();
                    question.setQuestionID(cursor.getInt(0));
                    question.setLevel(Integer.parseInt(level));
                    question.setPart(Integer.parseInt(part));
                    question.setParagraph(cursor.getString(1));
                    question.setImage(cursor.getString(2));
                    question.setAudio(cursor.getString(3));
                    question.setSubQuestionList(getSubQuestion(String.valueOf(cursor.getInt(0))));
                    questions.add(question);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return questions;
    }

    /**
     * Table SubQuestion methods
     */
    public void addSubQuestion(ArrayList<SubQuestion> subQuestions) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (SubQuestion subQuestion : subQuestions) {
            ContentValues values = new ContentValues();
            values.put(QUESTION_ID, subQuestion.getQuestionID());
            values.put(CONTENT, subQuestion.getContent());

            ArrayList<String> answerList = subQuestion.getAnswerList();
            values.put(A, answerList.get(0));
            values.put(B, answerList.get(1));
            values.put(C, answerList.get(2));
            values.put(D, answerList.get(3));

            values.put(RESULT, subQuestion.getResult());
            values.put(EXPLAIN_ANSWER, subQuestion.getExplainAnswer());

            db.insert(TABLE_SUB_QUESTION, null, values);
        }
    }

    public ArrayList<SubQuestion> getSubQuestion(String questionID) {
        ArrayList<SubQuestion> subQuestions = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SUB_QUESTION, new String[]{"*"},
                QUESTION_ID + "= ?", new String[]{questionID}, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    SubQuestion subQuestion = new SubQuestion();
                    subQuestion.setSubQuestionID(cursor.getInt(0));
                    subQuestion.setQuestionID(cursor.getInt(1));
                    subQuestion.setContent(cursor.getString(2));
                    ArrayList<String> answerList = new ArrayList<>();
                    answerList.add(cursor.getString(3));
                    answerList.add(cursor.getString(4));
                    answerList.add(cursor.getString(5));
                    answerList.add(cursor.getString(6));
                    subQuestion.setAnswerList(answerList);
                    subQuestion.setResult(Integer.valueOf(cursor.getString(7)));
                    subQuestion.setExplainAnswer(cursor.getString(8));

                    subQuestions.add(subQuestion);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return subQuestions;
    }

    public long getSubQuestionSizeOfQuestion(String questionId) {
        return DatabaseUtils.queryNumEntries(
                this.getReadableDatabase(),
                TABLE_SUB_QUESTION, QUESTION_ID + "=?",
                new String[]{questionId});
    }

    public long getSubQuestionSize(String level, String part) {
        ArrayList<Integer> questionIds = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from " + TABLE_QUESTION
                + " left join " + TABLE_SUB_QUESTION
                + " on " + TABLE_SUB_QUESTION + "." + QUESTION_ID + "=" + TABLE_QUESTION + "." + ID
                + " where " + LEVEL + "<= ? and " + PART + "= ?", new String[]{level, part});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                return cursor.getCount();
            }
            cursor.close();
        }
        return 0;
    }

    /**
     * Table Progress methods
     */
    public void setProgress(Progress progress) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EMAIL, progress.getEmail());
        values.put(PART, progress.getPart());
        values.put(SUB_QUESTION_ID, progress.getSubQuestionID());
        values.put(ANSWER_PICKED, progress.getAnswerPicked());

        db.replace(TABLE_PROGRESS, null, values);
    }

    public Progress getProgress(String email, String subQuestionId) {
        Progress progress = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PROGRESS, new String[]{SUB_QUESTION_ID, ANSWER_PICKED},
                EMAIL + "=? AND " + SUB_QUESTION_ID + " =?", new String[]{email, subQuestionId},
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            progress = new Progress(cursor.getInt(0), cursor.getInt(1));
            cursor.close();
        }
        return progress;
    }

    public long getProgressSize(String email, String part) {
        return DatabaseUtils.queryNumEntries(
                this.getReadableDatabase(),
                TABLE_PROGRESS, EMAIL + "=? AND " + PART + "=?",
                new String[]{email, part});
    }

    /**
     * Table Progress Test methods
     */
    public void setProgressTest(Progress progress) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PART, progress.getPart());
        values.put(SUB_QUESTION_ID, progress.getSubQuestionID());
        values.put(ANSWER_PICKED, progress.getAnswerPicked());
        values.put(TRUE, progress.getIsTrue());

        db.replace(TABLE_PROGRESS_TEST, null, values);
    }

    public Progress getProgressTest(String subQuestionId) {
        Progress progress = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PROGRESS_TEST, new String[]{SUB_QUESTION_ID, ANSWER_PICKED},
                SUB_QUESTION_ID + " =?", new String[]{subQuestionId},
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            progress = new Progress(cursor.getInt(0), cursor.getInt(1));
            cursor.close();
        }
        return progress;
    }

    public long getTrueQuantityByPart(String part) {
        return DatabaseUtils.queryNumEntries(
                this.getReadableDatabase(),
                TABLE_PROGRESS_TEST, PART + "=? AND " + TRUE + "=?",
                new String[]{part, "1"});
    }

    public void dropTableTestProgress() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRESS_TEST);
    }

    /**
     * Table Score methods
     */
    public void addScore(String email, long score1, long score2, long score3, long score4, long score5, long score6, long score7) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EMAIL, email);
        values.put(SCORE_PART1, score1);
        values.put(SCORE_PART2, score2);
        values.put(SCORE_PART3, score3);
        values.put(SCORE_PART4, score4);
        values.put(SCORE_PART5, score5);
        values.put(SCORE_PART6, score6);
        values.put(SCORE_PART7, score7);

        db.insert(TABLE_SCORE, null, values);
    }

    public ArrayList<Integer> getScore(String email) {
        ArrayList<Integer> scores = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SCORE, new String[]{"*"},
                EMAIL + "=?", new String[]{email}, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    scores.add(cursor.getInt(0));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return scores;
    }
}
