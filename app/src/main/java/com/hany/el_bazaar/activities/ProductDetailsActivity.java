package com.hany.el_bazaar.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hany.el_bazaar.Adapters.ReviewsAdapter;
import com.hany.el_bazaar.Model.Review;
import com.hany.el_bazaar.R;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

/**
 * Created by Hany on 12/15/2018.
 */

public class ProductDetailsActivity extends AppCompatActivity {

    SliderLayout imagesLayout;
    TextView actionBarTitle,productName,productPrice,bazaarAddress,productDesc,organizerName;
    ImageView facebookImage, googleImage,instagramImage,backImage;
    RecyclerView reviewsList;
    ReviewsAdapter reviewsAdapter;
    ArrayList<Review> reviews;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        imagesLayout = findViewById(R.id.image_slider);
        actionBarTitle = findViewById(R.id.action_bar_title);
        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
        bazaarAddress  =findViewById(R.id.bazaar_address);
        productDesc = findViewById(R.id.desc_text);
        organizerName  = findViewById(R.id.organizer_name);
        facebookImage = findViewById(R.id.facebook_contact);
        googleImage = findViewById(R.id.google_contact);
        instagramImage = findViewById(R.id.instagram_contact);
        backImage = findViewById(R.id.back_img);
        reviewsList = findViewById(R.id.reviews);
        reviews = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        reviewsList.setLayoutManager(layoutManager);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imagesLayout.setIndicatorAnimation(SliderLayout.Animations.FILL);
        imagesLayout.setScrollTimeInSec(3);
        setSliderViews();
        actionBarTitle.setText((CharSequence) getIntent().getExtras().get("productName"));
        productName.setText((CharSequence)getIntent().getExtras().get("productName"));
        productPrice.setText((CharSequence)getIntent().getExtras().get("productPrice"));
        setupReviews();
        reviewsAdapter = new ReviewsAdapter(this,reviews);
        reviewsList.setAdapter(reviewsAdapter);
    }

    private void setupReviews() {
        reviews.add(new Review("Hany123","this is awesome product","20 Oct 2018",4f));
        reviews.add(new Review("Mohsen445","this is bad product","25 Oct 2018",2f));
        reviews.add(new Review("Hussien415","this is beautiful product","20 Oct 2018",5f));
    }


    private void setSliderViews(){
        for (int i = 0; i < 3; i++) {

            SliderView sliderView = new SliderView(this);

            switch (i) {
                case 0:
                    sliderView.setImageDrawable(R.drawable.big_perfume_icon);
                    break;
                case 1:
                    sliderView.setImageDrawable(R.drawable.default_user_image);
                    break;
                case 2:
                    sliderView.setImageDrawable(R.drawable.big_perfume_icon);
                    break;
            }

            sliderView.setImageScaleType(ImageView.ScaleType.FIT_CENTER);
            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    Toast.makeText(ProductDetailsActivity.this,"slider click",Toast.LENGTH_LONG).show();
                }
            });

            //at last add this view in your layout :
            imagesLayout.addSliderView(sliderView);
        }
    }
}
