package com.hany.el_bazaar.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hany.el_bazaar.Adapters.ProductsTabAdapter;
import com.hany.el_bazaar.Model.Product;
import com.hany.el_bazaar.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Hany on 12/23/2018.
 */

public class FavoritesActivity extends AppCompatActivity {

    RecyclerView favProducts;
    ProductsTabAdapter adapter;
    ArrayList<Product> products;
    ImageView backImage;
    DatabaseReference reference;
    SpinKitView loading;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        backImage = findViewById(R.id.back_img);
        favProducts = findViewById(R.id.fav_products);
        loading = findViewById(R.id.loading);
        reference = FirebaseDatabase.getInstance().getReference("products");
        products = new ArrayList<>();
        loading.setVisibility(View.VISIBLE);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        favProducts.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductsTabAdapter(this,products);
        adapter.setList(true);
        adapter.setFavoriteList(true);
        favProducts.setAdapter(adapter);
        setupFavoriteProducts();
    }

    private void setupFavoriteProducts() {
        Query query = reference.orderByChild("isFavorite").equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loading.setVisibility(View.INVISIBLE);
                if(dataSnapshot.exists()){
                    Map<String,Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    setProductArray(map);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loading.setVisibility(View.INVISIBLE);
                Toast.makeText(FavoritesActivity.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setProductArray(Map<String, Object> map) {
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Product product = new Product();
                product.setProductId(entry.getKey());
                if (entry.getValue() instanceof Map) {
                    Map<String, Object> mapObj = (Map<String, Object>) entry.getValue();
                    product.setFavorite((Boolean) mapObj.get("isFavorite"));
                    product.setProductCurrency((String) mapObj.get("productCurrency"));
                    product.setProductName((String) mapObj.get("productName"));
                    product.setProductPrice((String) mapObj.get("productPrice"));
                    product.setProductDesc((String) mapObj.get("productDesc"));
                    product.setImages((List<String>) mapObj.get("images"));
                    products.add(product);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }
}
