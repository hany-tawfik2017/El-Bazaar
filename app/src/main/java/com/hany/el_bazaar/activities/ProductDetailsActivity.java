package com.hany.el_bazaar.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hany.el_bazaar.Adapters.ImagesPagerAdapter;
import com.hany.el_bazaar.Adapters.ReviewsAdapter;
import com.hany.el_bazaar.Defaults;
import com.hany.el_bazaar.Model.Review;
import com.hany.el_bazaar.R;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by Hany on 12/15/2018.
 */

public class ProductDetailsActivity extends AppCompatActivity {

    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("reviews");
    TextView actionBarTitle, productName, productPrice, bazaarAddress, productDesc, organizerName, postReview;
    ImageView facebookImage, googleImage, instagramImage, backImage;
    RecyclerView reviewsList;
    ReviewsAdapter reviewsAdapter;
    ArrayList<Review> reviews;
    EditText writeReview;
    RatingBar reviewRate;
    long rate;
    RelativeLayout reviewsLayout;
    SpinKitView loading;
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        actionBarTitle = findViewById(R.id.action_bar_title);
        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
        bazaarAddress = findViewById(R.id.bazaar_address);
        productDesc = findViewById(R.id.desc_text);
        organizerName = findViewById(R.id.organizer_name);
        facebookImage = findViewById(R.id.facebook_contact);
        googleImage = findViewById(R.id.google_contact);
        instagramImage = findViewById(R.id.instagram_contact);
        backImage = findViewById(R.id.back_img);
        reviewsList = findViewById(R.id.reviews);
        writeReview = findViewById(R.id.write_review);
        reviewRate = findViewById(R.id.review_rate);
        postReview = findViewById(R.id.post_review);
        reviewsLayout = findViewById(R.id.reviews_layout);
        loading = findViewById(R.id.loading);
        reviews = new ArrayList<>();
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        sliderDotspanel = (LinearLayout) findViewById(R.id.slider_dots);
        ArrayList<String> imagesList = (ArrayList<String>) getIntent().getSerializableExtra("productImages");

        if (imagesList != null) {
            if (imagesList.size() < 3) {
                if (imagesList.size() > 0)
                    for (int i = imagesList.size(); i < 3; i++)
                        imagesList.add("");
                else
                    for (int i = 0; i < 3; i++)
                        imagesList.add("");
            }
        } else {
            imagesList = new ArrayList<>();
            for (int i = 0; i < 3; i++)
                imagesList.add("");
        }

        ImagesPagerAdapter viewPagerAdapter = new ImagesPagerAdapter(this, true, imagesList);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        reviewsList.setLayoutManager(layoutManager);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        writeReview.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    reviewRate.setVisibility(View.VISIBLE);
                else
                    reviewRate.setVisibility(View.GONE);
            }
        });
        writeReview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0)
                    postReview.setVisibility(View.VISIBLE);
                else
                    postReview.setVisibility(View.GONE);
            }
        });
        reviewRate.setIsIndicator(false);
        reviewRate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rate = (long) rating;
            }
        });
        postReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReview();
            }
        });
        actionBarTitle.setText((CharSequence) getIntent().getExtras().get("productName"));
        productName.setText((CharSequence) getIntent().getExtras().get("productName"));
        productPrice.setText((CharSequence) getIntent().getExtras().get("productPrice"));
        bazaarAddress.setText((CharSequence) getIntent().getExtras().get("address"));
        productDesc.setText((CharSequence) getIntent().getExtras().get("productDesc"));
        getReviews();
        reviewsAdapter = new ReviewsAdapter(this, reviews);
        if (reviews.size() == 0)
            reviewsLayout.setVisibility(View.GONE);
        reviewsList.setAdapter(reviewsAdapter);
    }

    private void setReview() {
        String reviewId = reference.push().getKey();
        Date date = new Date();
        String format = new SimpleDateFormat("dd-MMM-yyyy").format(date);
        Review review = new Review(Defaults.getDefaults("userName", this), writeReview.getText().toString(), format, rate, getIntent().getExtras().getString("productName"), null);
        reference.child(reviewId).setValue(review).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                writeReview.setText("");
                writeReview.clearFocus();
                reviewRate.setVisibility(View.GONE);
                reviewRate.setRating(0);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProductDetailsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getReviews() {
        Query query = reference.orderByChild("productName").equalTo(getIntent().getExtras().getString("productName"));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (loading.getVisibility() == View.VISIBLE)
                    loading.setVisibility(View.GONE);
                reviews.clear();
                if (dataSnapshot.exists()) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    setupReviews(map);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loading.setVisibility(View.GONE);
                Toast.makeText(ProductDetailsActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupReviews(Map<String, Object> map) {
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Review review = new Review();

                if (entry.getValue() instanceof Map) {
                    Map<String, Object> mapObj = (Map<String, Object>) entry.getValue();
                    review.setReviewDate((String) mapObj.get("reviewDate"));
                    review.setReviewText((String) mapObj.get("reviewText"));
                    review.setUserName((String) mapObj.get("userName"));
                    review.setUserRate((Long) mapObj.get("userRate"));
                    reviews.add(review);
                }
            }
            reviewsLayout.setVisibility(View.VISIBLE);
            reviewsAdapter.notifyDataSetChanged();
        }

    }


}
