package com.careeranna.careeranna.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.careeranna.careeranna.R;

import java.util.ArrayList;

public class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> mPdfs;

    public PdfAdapter(Context mContext, ArrayList<String> mPdfs) {
        this.mContext = mContext;
        this.mPdfs = mPdfs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pdf_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        viewHolder.pdfText.setText(mPdfs.get(i));

    }

    @Override
    public int getItemCount() {
        return mPdfs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView pdfText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pdfText = itemView.findViewById(R.id.pdfText);
        }
    }
}
