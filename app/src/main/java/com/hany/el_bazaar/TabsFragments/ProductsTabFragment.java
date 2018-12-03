package com.hany.el_bazaar.TabsFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hany.el_bazaar.Adapters.ProductsTabAdapter;
import com.hany.el_bazaar.Model.Product;
import com.hany.el_bazaar.Model.Vendor;
import com.hany.el_bazaar.R;

import java.util.ArrayList;

/**
 * Created by Hany on 10/19/2018.
 */

public class ProductsTabFragment extends Fragment {

    RecyclerView productsList;
    TextView productCount;
    ArrayList<Product> products;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.products_tab_fragment,container,false);
        productsList = view.findViewById(R.id.products);
        productCount = view.findViewById(R.id.item_count);
        products = new ArrayList<>();
        setupDefaultProducts();
        ProductsTabAdapter adapter = new ProductsTabAdapter(getActivity(),products);
        productsList.setLayoutManager(new GridLayoutManager(getActivity(),2));
        productsList.setAdapter(adapter);
        productCount.setText(""+adapter.getItemCount()+" items");
        return view;
    }

    private void setupDefaultProducts(){
        products.add(new Product("Necklace Classic Design","249.99 EGP",false));
        products.add(new Product("Perfume Classic Design","249.99 EGP",true));
        products.add(new Product("Necklace Classic Design","249.99 EGP",false));
        products.add(new Product("Perfume Classic Design","249.99 EGP",false));
        products.add(new Product("Necklace Classic Design","249.99 EGP",true));
        products.add(new Product("Necklace Classic Design","249.99 EGP",false));
    }
}
