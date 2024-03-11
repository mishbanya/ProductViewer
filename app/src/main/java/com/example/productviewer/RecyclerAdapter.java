package com.example.productviewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private static OnItemClickListener clickListener;
    private static List<Product> dataList;

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.itemproduct, parent, false);
        return new ViewHolder(view);
    }
    public RecyclerAdapter(Context context, List<Product> dataList) {
        this.inflater = LayoutInflater.from(context);
        this.dataList = dataList;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Product data = dataList.get(position);

        holder.textViewTitle.setText(data.getTitle());
        holder.textViewDescription.setText(data.getDescription());
        holder.textViewPrice.setText(String.valueOf(data.getPrice()+"$"));
        Picasso.get().load(data.getThumbnail()).into(holder.imageViewThumbnail);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public void setData(List<Product> newData) {
        dataList.clear();
        dataList.addAll(newData);
        notifyDataSetChanged();
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        clickListener = listener;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewPrice;
        TextView textViewDescription;
        ImageView imageViewThumbnail;

        Button button;
        ViewHolder(View itemView) {
            super(itemView);
            imageViewThumbnail = itemView.findViewById(R.id.imageViewThumbnail);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            button = itemView.findViewById(R.id.buttonProduct);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && clickListener != null) {
                        clickListener.onItemClick(dataList.get(position));
                    }
                }
            });
        }
    }
}
