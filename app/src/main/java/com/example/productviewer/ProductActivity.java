package com.example.productviewer;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;

import com.example.productviewer.databinding.ActivityProductBinding;
import com.squareup.picasso.Picasso;

public class ProductActivity extends AppCompatActivity{
    private ActivityProductBinding binding;
    Product product;
    String description=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("product")) {
            product = (Product) intent.getSerializableExtra("product");
        }

        ImageView productThumbnail = findViewById(R.id.productThumbnail);
        binding.productRating.setText(String.valueOf(product.getRating()));
        binding.productBrand.setText(product.getBrand());
        binding.productCategory.setText(product.getCategory());
        binding.productDescription.setText(product.getDescription());
        binding.productTitle.setText(product.getTitle());
        binding.productPrice.setText(String.valueOf(product.getPrice())+"$");
        Picasso.get().load(product.getThumbnailUrl()).into(productThumbnail);
    }
}