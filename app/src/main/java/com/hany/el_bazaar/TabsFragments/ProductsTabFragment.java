package com.hany.el_bazaar.TabsFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hany.el_bazaar.Adapters.ProductsTabAdapter;
import com.hany.el_bazaar.Defaults;
import com.hany.el_bazaar.Model.Product;
import com.hany.el_bazaar.R;
import com.hany.el_bazaar.activities.PostProductActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Hany on 10/19/2018.
 */

public class ProductsTabFragment extends Fragment {

    RecyclerView productsList;
    TextView productCount;
    ArrayList<Product> products;
    DatabaseReference reference;
    SpinKitView loading;
    ImageView convertList, convertGrid;
    ProductsTabAdapter adapter;
    FloatingActionButton addProduct;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.products_tab_fragment, container, false);
        productsList = view.findViewById(R.id.products);
        productCount = view.findViewById(R.id.item_count);
        loading = view.findViewById(R.id.loading);
        convertList = view.findViewById(R.id.convert_list_img);
        convertGrid = view.findViewById(R.id.convert_grid_img);
        addProduct = view.findViewById(R.id.add_product);
        products = new ArrayList<>();
        if (Defaults.getDefaults("userType", getActivity()) != null && Defaults.getDefaults("userType", getActivity()).equals("Vendor"))
            addProduct.setVisibility(View.VISIBLE);
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PostProductActivity.class);
                intent.putExtra("isOrganizer", false);
                startActivity(intent);
                getActivity().finish();
            }
        });
        loading.setVisibility(View.VISIBLE);
        setupDefaultProducts();
        adapter = new ProductsTabAdapter(getActivity(), products);
        productsList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        productsList.setAdapter(adapter);
        convertList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertList.setVisibility(View.INVISIBLE);
                convertGrid.setVisibility(View.VISIBLE);
                adapter.setList(true);
                productsList.setLayoutManager(new LinearLayoutManager(getActivity()));
                productsList.setAdapter(adapter);
            }
        });
        convertGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertList.setVisibility(View.VISIBLE);
                convertGrid.setVisibility(View.INVISIBLE);
                adapter.setList(false);
                productsList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                productsList.setAdapter(adapter);
            }
        });
        return view;
    }

    private void setupDefaultProducts() {
        reference = FirebaseDatabase.getInstance().getReference("products");
//        if (Defaults.getDefaults("userType", getActivity()) != null && Defaults.getDefaults("userType", getActivity()).equals("Vendor")) {
//            Query query = reference.orderByChild("vendor/vendorName").equalTo(Defaults.getDefaults("userName",getActivity()));
//            query.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    loading.setVisibility(View.GONE);
//                    if (dataSnapshot.exists()) {
//                        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
//                        setProductArray(map);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    loading.setVisibility(View.GONE);
//                    Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
//                }
//            });
//        } else
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loading.setVisibility(View.GONE);
                products.clear();
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                setProductArray(map);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loading.setVisibility(View.GONE);
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
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
                    product.setBazaars((List<Map<String, String>>) mapObj.get("bazaars"));
                    product.setVendor((Map<String, String>) mapObj.get("vendor"));
                    product.setImages((List<String>) mapObj.get("images"));
                    product.setFavoriteUsers((List<String>) mapObj.get("favoriteUsers"));
                    if (mapObj.get("productRate") != null)
                        product.setProductRate((Long) mapObj.get("productRate"));
                    products.add(product);
                }
            }
            adapter.notifyDataSetChanged();
            productCount.setText("" + adapter.getItemCount() + " items");
        }
    }
}
