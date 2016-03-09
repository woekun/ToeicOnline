package com.example.woekun.toeiconline.adapters;

import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.woekun.toeiconline.AppController;
import com.example.woekun.toeiconline.Const;
import com.example.woekun.toeiconline.DatabaseHelper;
import com.example.woekun.toeiconline.R;
import com.example.woekun.toeiconline.models.Part;

import java.util.ArrayList;
import java.util.List;

public class PartListAdapter extends RecyclerView.Adapter<PartListAdapter.ViewHolder> {

    private AppController appController = AppController.getInstance();

    private List<Part> partList;

    private String[] partNameList = {
            "Photographs", "Question - Response", "Short Conversations", "Talks",
            "Incomplete Sentences", "Text Completion", "Reading Comprehension"};

    private int[] imgRes = {
            R.mipmap.ic_category_photo,
            R.mipmap.ic_category_question_response,
            R.mipmap.ic_category_short_conversations,
            R.mipmap.ic_category_talks,
            R.mipmap.ic_category_incompleted_sentences,
            R.mipmap.ic_category_text_completion,
            R.mipmap.ic_category_reading_comprehension};


    public PartListAdapter() {
        super();
        DatabaseHelper databaseHelper = appController.getDatabaseHelper();
        SharedPreferences sharedPreferences = appController.getSharedPreferences();
        String email = sharedPreferences.getString(Const.EMAIL, null);
        String level = sharedPreferences.getString(Const.LEVEL, "1");

        partList = new ArrayList<>();
        for (int i = 0; i < imgRes.length; i++) {
            int numberQuestions = (int) databaseHelper.getSubQuestionSize(level, String.valueOf((i + 1)));
            int progress = (int) databaseHelper.getProgressSize(email, String.valueOf((i + 1)));

            partList.add(new Part(
                    imgRes[i], partNameList[0], progress, (i + 1), numberQuestions));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.part_item_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Part part = partList.get(position);

        int numberQuestions = part.getNumberQuestion();
        int progress = part.getProgress();
        String progressText = String.format("(%d/%d) %.0f%%",
                progress, numberQuestions, (float) progress / numberQuestions * 100);

        holder.partImage.setImageResource(part.getPartResIdImage());
        holder.partNumber.setText(part.getPartNumber());
        holder.partName.setText(part.getPartName());
        holder.progressText.setText(progressText);
        holder.progressBar.setProgress(part.getProgress());
        holder.progressBar.setMax(numberQuestions);
    }

    @Override
    public int getItemCount() {
        return partList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView partImage;
        private TextView partNumber;
        private TextView partName;
        private TextView progressText;
        private ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);

            partImage = (ImageView) itemView.findViewById(R.id.part_img);
            partNumber = (TextView) itemView.findViewById(R.id.part_num);
            partName = (TextView) itemView.findViewById(R.id.part_name);
            progressText = (TextView) itemView.findViewById(R.id.progress_text);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
        }
    }
}
