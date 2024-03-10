package com.example.productviewer;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public List<Product> data = new ArrayList<>();
    public final int PAGE_SIZE = 20;
    public boolean isLoading = false;
    RecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textMessage = findViewById(R.id.textMessage);

        /*Button buttonNextPage = findViewById(R.id.buttonNextPage);
        buttonNextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNextPage(data.size()/PAGE_SIZE+1);
            }
        });*/

        RecyclerView Recycler = findViewById(R.id.recycler);
        recyclerAdapter = new RecyclerAdapter(this, data);
        Recycler.setAdapter(recyclerAdapter);
        Recycler.setLayoutManager(new LinearLayoutManager(this));

        Recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && !isLoading) {
                    int nextPage = (totalItemCount / PAGE_SIZE) + 1;
                    isLoading=true;
                    loadNextPage(nextPage);
                    isLoading=false;
                }
            }
        });
        recyclerAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                Intent intent = new Intent(MainActivity.this, ProductActivity.class);
                intent.putExtra("product", product);
                startActivity(intent);
            }
        });

        new FetchProductsTask(0,20,new FetchProductsTask.OnProductsFetchedListener() {
            @Override
            public void onProductsFetched(List<Product> products) {
                if (products.isEmpty()) {
                    textMessage.setText("Ошибка при загрузке продуктов");
                } else {
                    textMessage.setText(null);
                    //buttonNextPage.setAlpha(1);
                    //buttonNextPage.setText("Загрузить далее");
                    data.addAll(products);
                    recyclerAdapter.notifyDataSetChanged();
                }
            }
        }).execute();

    }

    private void loadNextPage(int page) {
        new FetchProductsTask(page * PAGE_SIZE, PAGE_SIZE, new FetchProductsTask.OnProductsFetchedListener() {
            @Override
            public void onProductsFetched(List<Product> products) {
                if (!products.isEmpty()) {
                    data.addAll(products);
                    recyclerAdapter.notifyDataSetChanged();
                }
            }
        }).execute();
    }
}