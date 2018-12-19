package com.hany.el_bazaar.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hany.el_bazaar.Model.Review;
import com.hany.el_bazaar.R;

import java.util.ArrayList;

/**
 * Created by Hany on 12/15/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Review> reviews;

    public ReviewsAdapter(Context context,ArrayList<Review> reviews){
        this.context =context;
        this.reviews = reviews;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.byUser.setText(context.getResources().getString(R.string.by_user)+reviews.get(position).getUserName());
        holder.reviewText.setText(reviews.get(position).getReviewText());
        holder.reviewDate.setText(reviews.get(position).getReviewDate());
        holder.userRate.setRating(reviews.get(position).getUserRate());
    }

    @Override
    public int getItemCount() {
        if(reviews!=null&&reviews.size()>0)
            return reviews.size();
        return 0;
    }

     class ViewHolder extends RecyclerView.ViewHolder{

        TextView byUser,reviewText,reviewDate;
        RatingBar userRate;
        public ViewHolder(View itemView) {
            super(itemView);
            byUser = itemView.findViewById(R.id.by_user);
            reviewDate = itemView.findViewById(R.id.review_date);
            reviewText = itemView.findViewById(R.id.review_text);
            userRate = itemView.findViewById(R.id.product_rate);

        }
    }
}
