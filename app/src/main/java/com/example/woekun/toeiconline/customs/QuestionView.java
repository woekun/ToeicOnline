package com.example.woekun.toeiconline.customs;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.woekun.toeiconline.AppController;
import com.example.woekun.toeiconline.Const;
import com.example.woekun.toeiconline.R;
import com.example.woekun.toeiconline.models.Progress;
import com.example.woekun.toeiconline.models.Question;
import com.example.woekun.toeiconline.models.SubQuestion;
import com.example.woekun.toeiconline.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class QuestionView extends LinearLayout {

    public static String TEST = "test_mode";
    public static String TRAINING = "train_mode";

    private AppController appController;
    private CardView paragraphView;
    private LinearLayout subQuestionsView;
    private View card;

    private boolean canCollapse = false;
    private boolean hasParagraph = false;
    private boolean isChecked = false;
    private String mode = TRAINING;

    public QuestionView(Context context) {
        super(context);
        initView(context);
    }

    public QuestionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public QuestionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public QuestionView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        appController = AppController.getInstance();

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.question_view, this, true);

        paragraphView = (CardView) findViewById(R.id.paragraph_view);
        subQuestionsView = (LinearLayout) findViewById(R.id.subQuestion_container);

    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setQuestion(Question question) {
        setParagraph(question);
        addSubQuestionContent(question);
    }

    private void setParagraph(Question question) {
        TextView paragraph = (TextView) paragraphView.findViewById(R.id.paragraph);
        if (!question.getParagraph().equals("")) {
            if (question.getPart() == 3 || question.getPart() == 4) {
                paragraph.setVisibility(View.GONE);
            } else {
                paragraphView.setVisibility(View.VISIBLE);
                paragraph.setText(question.getParagraph());
            }
            hasParagraph = true;
        } else {
            hasParagraph = false;
            paragraphView.setVisibility(View.GONE);
        }
    }

    public void setPosition(int position) {
        TextView positionSubQuestion = (TextView) card.findViewById(R.id.question_position);
        if (hasParagraph) {
            TextView positionsParagraph = (TextView) paragraphView.findViewById(R.id.question_position_paragraph);
            positionsParagraph.setText(String.format("Question %d", position + 1));
            positionSubQuestion.setVisibility(View.GONE);
        } else {
            positionSubQuestion.setText(String.format("Question %d", position + 1));
            positionSubQuestion.setVisibility(View.VISIBLE);
        }
    }

    public void setCollapse(boolean canCollapse) {
        this.canCollapse = canCollapse;
    }

    private void addSubQuestionContent(Question question) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ArrayList<SubQuestion> subQuestions = question.getSubQuestionList();

        for (int i = 0; i < subQuestions.size(); i++) {
            SubQuestion subQuestion = subQuestions.get(i);

            card = inflater.inflate(R.layout.sub_question_item, null, true);
            setPosition(i);
            setImageQuestion(question);
            setAnswerField(subQuestion, question.getPart());
            setContent(subQuestion);

            subQuestionsView.addView(card);
        }
    }

    private void setImageQuestion(Question question) {
        if (!question.getImage().equals(""))
            Picasso.with(getContext())
                    .load(Const.BASE_IMAGE_URL + question.getImage() + ".jpg")
                    .into((ImageView) card.findViewById(R.id.image_question));
    }

    private void setAnswerField(final SubQuestion subQuestion, final int part) {


        final RadioGroup answerField = (RadioGroup) card.findViewById(R.id.list_answer_radio);
        final ArrayList<String> answerList = subQuestion.getAnswerList();

        for (int i = 0; i < answerList.size(); i++) {
            if (i == 3 && part == 2)
                answerField.getChildAt(i).setVisibility(View.GONE);
            else
                ((RadioButton) answerField.getChildAt(i)).setText(answerList.get(i));
        }

        final int[] answerPicked = {subQuestion.getAnswerPicked()};
        if (answerPicked[0] >= 1) {
            ((RadioButton) answerField.getChildAt(answerPicked[0] - 1)).setChecked(true);
        }

        answerField.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.option1:
                        answerPicked[0] = 1;
                        break;
                    case R.id.option2:
                        answerPicked[0] = 2;
                        break;
                    case R.id.option3:
                        answerPicked[0] = 3;
                        break;
                    case R.id.option4:
                        answerPicked[0] = 4;
                        break;
                    default:
                        answerPicked[0] = -1;
                        break;
                }

                String email = appController.getSharedPreferences().getString(Const.EMAIL, null);
                if (mode.equals(TRAINING)) {
                    appController.getDatabaseHelper().setProgress(
                            new Progress(email, subQuestion.getSubQuestionID(), part, answerPicked[0]));
                } else if (mode.equals(TEST)) {
                    appController.getDatabaseHelper().setProgressTest(
                            new Progress(email, subQuestion.getSubQuestionID(), part, answerPicked[0]));
                }
            }
        });

        ImageButton check = (ImageButton) card.findViewById(R.id.check);
        if (!mode.equals(TEST)) {
            check.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isChecked){
                        isChecked = false;
                        if (answerPicked[0] > 0)
                            (answerField.getChildAt(answerPicked[0] - 1)).setBackgroundColor(Color.WHITE);

                        (answerField.getChildAt(subQuestion.getResult() - 1)).setBackgroundColor(Color.WHITE);
                    }else {
                        isChecked=true;
                        if (answerPicked[0] > 0)
                            (answerField.getChildAt(answerPicked[0] - 1)).setBackgroundColor(Color.RED);

                        (answerField.getChildAt(subQuestion.getResult() - 1)).setBackgroundColor(Color.GREEN);
                    }
                }
            });
        } else {
            check.setVisibility(View.GONE);
        }
    }

    private void setContent(SubQuestion subQuestion) {
        final View line = card.findViewById(R.id.line);
        final TextView content = (TextView) card.findViewById(R.id.text_question);
        final RadioGroup answerField = (RadioGroup) card.findViewById(R.id.list_answer_radio);
        content.setText(subQuestion.getContent());
        content.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canCollapse) {
                    if (answerField.getVisibility() == View.GONE && line.getVisibility() == View.GONE) {
                        answerField.setVisibility(View.VISIBLE);
                        line.setVisibility(View.VISIBLE);
                    } else {
                        answerField.setVisibility(View.GONE);
                        line.setVisibility(View.GONE);
                    }
                }
            }
        });
    }
}
