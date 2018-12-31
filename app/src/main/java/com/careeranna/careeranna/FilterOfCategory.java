package com.careeranna.careeranna;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.careeranna.careeranna.adapter.SubCategoryAdapter;
import com.careeranna.careeranna.data.SubCategory;

import java.util.ArrayList;

public class FilterOfCategory extends AppCompatActivity implements SubCategoryAdapter.OnItemClickListener{

    RecyclerView recyclerView;

    ArrayList<SubCategory> subCategories;

    ArrayList<SubCategory> chosenSub;

    Button cancel, apply;

    CheckBox free, paid;

    Boolean free1, paid1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_of_category);

        recyclerView = findViewById(R.id.subCategory);

        subCategories = (ArrayList<SubCategory>) getIntent().getSerializableExtra("categories");

        chosenSub = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SubCategoryAdapter subCategoryAdapter = new SubCategoryAdapter(subCategories, this);

        subCategoryAdapter.setOnItemClicklistener(this);
        recyclerView.setAdapter(subCategoryAdapter);

        free = findViewById(R.id.free);

        paid = findViewById(R.id.paid);

        apply = findViewById(R.id.apply);

        cancel = findViewById(R.id.cancel);

        free.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                free1 = isChecked;
            }
        });


        paid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                paid1 = isChecked;
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("category", chosenSub);
                intent.putExtra("free", free1);
                intent.putExtra("paid", paid1);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chosenSub.clear();
                Intent intent = new Intent();
                intent.putExtra("category", chosenSub);
                setResult(Activity.RESULT_OK, intent);
                finish();

            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, subCategories.get(position).getEXAM_NAME(), Toast.LENGTH_SHORT).show();

        chosenSub.add(subCategories.get(position));

    }

}

