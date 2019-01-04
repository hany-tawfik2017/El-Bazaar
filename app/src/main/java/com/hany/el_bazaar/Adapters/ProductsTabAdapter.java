package com.hany.el_bazaar.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hany.el_bazaar.Defaults;
import com.hany.el_bazaar.GlideApp;
import com.hany.el_bazaar.Model.Product;
import com.hany.el_bazaar.R;
import com.hany.el_bazaar.activities.ProductDetailsActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hany on 11/30/2018.
 */

public class ProductsTabAdapter extends RecyclerView.Adapter<ProductsTabAdapter.ViewHolder> {

    Context context;
    ArrayList<Product> products;
    boolean isList;
    boolean isFavoriteList;
    DatabaseReference reference;
    StorageReference storageReference;

    public ProductsTabAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductsTabAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(!isList ? R.layout.product_grid_item : R.layout.favorite_list_item, parent, false);
        return new ProductsTabAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductsTabAdapter.ViewHolder holder, final int position) {
        holder.productName.setText(products.get(position).getProductName());
        holder.productPrice.setText(products.get(position).getProductPrice());
        holder.productCurrency.setText(products.get(position).getProductCurrency());
        if (products.get(position).getBazaars() != null)
            holder.bazaarAddress.setText(products.get(position).getBazaars().get(0).get("bazaarName") + "," + products.get(position).getBazaars().get(0).get("bazaarPlace"));
        else
            holder.bazaarAddress.setText("LONGCHAMP, City Star Mall");
        if (products.get(position).getImages() != null) {
            storageReference = FirebaseStorage.getInstance().getReference().child("product/" + products.get(position).getImages().get(0));
            GlideApp.with(context).load(storageReference).into(holder.productImage);
        } else
            holder.productImage.setImageResource(R.drawable.logo);

        if (Defaults.getDefaults("userId", context) != null && products.get(position).isFavorite() && products.get(position).getFavoriteUsers() != null && products.get(position).getFavoriteUsers().contains(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
            holder.favImage.setVisibility(View.INVISIBLE);
            holder.unFavImage.setVisibility(View.VISIBLE);
        }
        if (products.get(position).getProductRate() != 0 && holder.productRate != null)
            holder.productRate.setRating(products.get(position).getProductRate());
        reference = FirebaseDatabase.getInstance().getReference("products");
        if (!isFavoriteList && Defaults.getDefaults("userId", context) != null) {
            holder.favImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final List<String> favoriteUsers;
                    holder.favImage.setVisibility(View.INVISIBLE);
                    holder.unFavImage.setVisibility(View.VISIBLE);
                    if (!products.get(position).isFavorite) {
                        products.get(position).isFavorite = true;
                        favoriteUsers = new ArrayList<>();
                        favoriteUsers.add(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    } else {
                        favoriteUsers = products.get(position).getFavoriteUsers();
                        if (!favoriteUsers.contains(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                            favoriteUsers.add(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    }
                    reference.child(products.get(position).getProductId()).child("isFavorite").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            reference.child(products.get(position).getProductId()).child("favoriteUsers").setValue(favoriteUsers).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context, "Added to Favorites", Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
            holder.unFavImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.favImage.setVisibility(View.VISIBLE);
                    holder.unFavImage.setVisibility(View.INVISIBLE);
                    if (products.get(position).getFavoriteUsers() != null) {
                        products.get(position).getFavoriteUsers().remove(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                        if (products.get(position).getFavoriteUsers().size() == 0) {
                            products.get(position).isFavorite = false;
                        }
                    }
                    reference.child(products.get(position).getProductId()).child("isFavorite").setValue(products.get(position).isFavorite())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    if (!products.get(position).isFavorite())
                                        products.get(position).getFavoriteUsers().clear();
                                    reference.child(products.get(position).getProductId()).child("favoriteUsers").setValue(products.get(position).getFavoriteUsers())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_LONG).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                }
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("productName", products.get(position).getProductName());
                intent.putExtra("productPrice", products.get(position).getProductPrice());
                intent.putExtra("productCurrency", products.get(position).getProductCurrency());
                intent.putExtra("productDesc", products.get(position).getProductDesc());
                intent.putExtra("productId", products.get(position).getProductId());
                intent.putExtra("address", holder.bazaarAddress.getText().toString());
                intent.putExtra("productImages", (Serializable) products.get(position).getImages());
                intent.putExtra("favoriteUsers", (Serializable) products.get(position).getFavoriteUsers());
                intent.putExtra("isFavorite", products.get(position).isFavorite);
                if (products.get(position).getProductRate() != 0)
                    intent.putExtra("productRate", products.get(position).getProductRate());
                if (products.get(position).getVendor() != null) {
                    intent.putExtra("vendorName", products.get(position).getVendor().get("vendorName"));
                    intent.putExtra("vendorEmail", products.get(position).getVendor().get("vendorEmail"));
                }
                if (products.get(position).getBazaars() != null) {
                    intent.putExtra("bazaarName", products.get(position).getBazaars().get(0).get("bazaarName"));
                    intent.putExtra("bazaarPlace", products.get(position).getBazaars().get(0).get("bazaarPlace"));
                }
                context.startActivity(intent);
            }
        });

    }

    public void setFavoriteList(boolean favoriteList) {
        isFavoriteList = favoriteList;
    }

    public void setList(boolean list) {
        isList = list;
    }

    @Override
    public int getItemCount() {
        if (products != null && products.size() > 0)
            return products.size();
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage, favImage, unFavImage;
        TextView productName, productPrice, bazaarAddress, productCurrency;
        RatingBar productRate;

        public ViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_img);
            favImage = itemView.findViewById(R.id.select_favourite);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productRate = itemView.findViewById(R.id.product_rate);
            bazaarAddress = itemView.findViewById(R.id.bazaar_address);
            productCurrency = itemView.findViewById(R.id.product_currency);
            unFavImage = itemView.findViewById(R.id.unselect_favourite);
        }
    }
}

