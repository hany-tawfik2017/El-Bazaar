package com.hany.el_bazaar.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hany.el_bazaar.Adapters.ReviewsAdapter;
import com.hany.el_bazaar.Model.Review;
import com.hany.el_bazaar.R;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

/**
 * Created by Hany on 12/18/2018.
 */

public class BazaarDetailsActivity extends AppCompatActivity {

    ImageView backimg,facebookImage,googleImage,instagramImage;
    TextView actionTitle,organizerName,bazaarName,vendorsNumbers,descText,vendorName,brandName,bazaarPlace;
    SliderLayout imagesLayout;
    RecyclerView reviewsList;
    RatingBar bazaarRate;
    ArrayList<Review> reviews;
    ReviewsAdapter reviewsAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bazaar_details);
        backimg = findViewById(R.id.back_img);
        facebookImage = findViewById(R.id.facebook_contact);
        googleImage = findViewById(R.id.google_contact);
        instagramImage = findViewById(R.id.instagram_contact);
        actionTitle = findViewById(R.id.action_bar_title);
        organizerName = findViewById(R.id.organizer_name);
        bazaarName = findViewById(R.id.bazaar_name);
        vendorsNumbers = findViewById(R.id.vendors_numbers);
        descText  =findViewById(R.id.desc_text);
        vendorName = findViewById(R.id.vendor_name);
        brandName = findViewById(R.id.brand_name);
        imagesLayout = findViewById(R.id.image_slider);
        bazaarRate = findViewById(R.id.bazaar_rate);
        reviewsList = findViewById(R.id.reviews);
        bazaarPlace = findViewById(R.id.bazaar_address);
        backimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        reviews = new ArrayList<>();
        imagesLayout.setIndicatorAnimation(SliderLayout.Animations.FILL);
        imagesLayout.setScrollTimeInSec(3);
        setSliderViews();
        actionTitle.setText((CharSequence) getIntent().getExtras().get("bazaarName"));
        bazaarName.setText((CharSequence) getIntent().getExtras().get("bazaarName"));
        organizerName.setText((CharSequence) getIntent().getExtras().get("organizerName"));
        vendorsNumbers.setText(""+getIntent().getExtras().get("vendorsNumbers")+" Vendors");
        bazaarPlace.setText((CharSequence) getIntent().getExtras().get("bazaarPlace"));
        reviewsList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        setupReviews();
        reviewsAdapter = new ReviewsAdapter(this,reviews);
        reviewsList.setAdapter(reviewsAdapter);
    }


    private void setSliderViews(){
        for (int i = 0; i < 3; i++) {

            SliderView sliderView = new SliderView(this);

            switch (i) {
                case 0:
                    sliderView.setImageDrawable(R.drawable.bazaar_big_img);
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
                    Toast.makeText(BazaarDetailsActivity.this,"slider click",Toast.LENGTH_LONG).show();
                }
            });

            //at last add this view in your layout :
            imagesLayout.addSliderView(sliderView);
        }
    }
    private void setupReviews() {
        reviews.add(new Review("Hany123","this is awesome product","20 Oct 2018",4f));
        reviews.add(new Review("Mohsen445","this is bad product","25 Oct 2018",2f));
        reviews.add(new Review("Hussien415","this is beautiful product","20 Oct 2018",5f));
    }
}
