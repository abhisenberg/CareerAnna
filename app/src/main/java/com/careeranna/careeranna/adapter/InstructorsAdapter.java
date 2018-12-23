package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.careeranna.careeranna.R;
import com.careeranna.careeranna.data.Instructor;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class InstructorsAdapter extends RecyclerView.Adapter<InstructorsAdapter.InstructorView> implements View.OnClickListener {

    Context context;
    ArrayList<Instructor> instructorsList;

    public InstructorsAdapter(Context context, ArrayList<Instructor> instructorsList){
        this.context = context;
        this.instructorsList = instructorsList;
    }

    @NonNull
    @Override
    public InstructorView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_instructor_details, viewGroup, false);

        return new InstructorView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructorView instructorView, int i) {
        Glide.with(context)
//                .load(instructorsList.get(i).getImage_url())
                .load(R.drawable.ic_profile)
                .into(instructorView.instr_image);
        instructorView.instr_name.setText(instructorsList.get(i).getName());
        instructorView.instr_desc.setText(instructorsList.get(i).getDesc());
        instructorView.instr_email.setText(instructorsList.get(i).getEmail());
        instructorView.follow_instr.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return instructorsList.size();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_instructor_follow:
                Toast.makeText(context, "Successfully followed!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public class InstructorView extends RecyclerView.ViewHolder {

        CircleImageView instr_image;
        TextView instr_name, instr_desc, instr_email;
        Button follow_instr;

        public InstructorView(@NonNull View itemView) {
            super(itemView);
            instr_image = itemView.findViewById(R.id.iv_instructor_image);
            instr_desc = itemView.findViewById(R.id.tv_instructor_desc);
            instr_name = itemView.findViewById(R.id.tv_instructor_name);
            follow_instr = itemView.findViewById(R.id.bt_instructor_follow);
            instr_email = itemView.findViewById(R.id.tv_instructor_email);
        }
    }
}
