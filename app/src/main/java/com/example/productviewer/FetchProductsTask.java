package com.example.productviewer;

import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FetchProductsTask extends AsyncTask<Void, Void, List<Product>> {
    private static final String API_URL = "https://dummyjson.com/products";
    private final OnProductsFetchedListener listener;
    private static final String TAG = "FetchProductsTask";
    private final int skip;
    private final int limit;

    public FetchProductsTask(int skip, int limit, OnProductsFetchedListener listener) {
        this.skip = skip;
        this.limit = limit;
        this.listener = listener;
    }

    @Override
    protected List<Product> doInBackground(Void... voids) {
        List<Product> products = new ArrayList<>();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(API_URL).newBuilder();
        urlBuilder.addQueryParameter("skip", String.valueOf(skip));
        urlBuilder.addQueryParameter("limit", String.valueOf(limit));
        String url = urlBuilder.build().toString();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .header("accept", "application/json")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String jsonData = response.body().string();
                JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
                JsonArray productsArray = jsonObject.getAsJsonArray("products");

                for (JsonElement element : productsArray) {
                    JsonObject productObject = element.getAsJsonObject();
                    Product product = new Product();
                    if (productObject.has("id") && !productObject.get("id").isJsonNull()) {
                        product.setId(productObject.get("id").getAsInt());
                    }
                    if (productObject.has("title") && !productObject.get("title").isJsonNull()) {
                        product.setTitle(productObject.get("title").getAsString());
                    }
                    if (productObject.has("description") && !productObject.get("description").isJsonNull()) {
                        product.setDescription(productObject.get("description").getAsString());
                    }
                    if (productObject.has("price") && !productObject.get("price").isJsonNull()) {
                        product.setPrice(productObject.get("price").getAsInt());
                    }
                    if (productObject.has("discountPercentage") && !productObject.get("discountPercentage").isJsonNull()) {
                        product.setDiscountPercentage(productObject.get("discountPercentage").getAsDouble());
                    }
                    if (productObject.has("rating") && !productObject.get("rating").isJsonNull()) {
                        product.setRating(productObject.get("rating").getAsDouble());
                    }
                    if (productObject.has("stock") && !productObject.get("stock").isJsonNull()) {
                        product.setStock(productObject.get("stock").getAsInt());
                    }
                    if (productObject.has("brand") && !productObject.get("brand").isJsonNull()) {
                        product.setBrand(productObject.get("brand").getAsString());
                    }
                    if (productObject.has("category") && !productObject.get("category").isJsonNull()) {
                        product.setCategory(productObject.get("category").getAsString());
                    }
                    if (productObject.has("thumbnail") && !productObject.get("thumbnail").isJsonNull()) {
                        product.setThumbnailUrl(productObject.get("thumbnail").getAsString());
                    }
                    products.add(product);
                }
            } else {
                Log.e(TAG, "Error: " + response.code() + " " + response.message());
                products = null;
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException", e);
            products = null;
        }
        return products;
    }

    @Override
    protected void onPostExecute(List<Product> products) {
        if (listener != null) {
            if (products != null) {
                listener.onProductsFetched(products);
            } else {
                listener.onProductsFetched(new ArrayList<>());
            }
        }
    }

    public interface OnProductsFetchedListener {
        void onProductsFetched(List<Product> products);
    }
}