package com.avanza.testing.Views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.avanza.testing.Models.Article;
import com.avanza.testing.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public interface ItemClickListener {
        void onItemClick(Article item);
    }

    private final ArrayList<Article> mValues;
    private final Context mContext;
    private final ItemClickListener mListener;

    public RecyclerViewAdapter(ArrayList<Article> articles, Context context,ItemClickListener listener) {
        mValues = articles;
        mContext = context;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bind(mValues.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView published_date;
        final TextView title;
        final TextView short_description;
        final TextView publisher;
        final ImageView thumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            published_date = (TextView) itemView.findViewById(R.id.published_date);
            short_description = (TextView) itemView.findViewById(R.id.short_description);
            title = (TextView) itemView.findViewById(R.id.title);
            publisher = (TextView) itemView.findViewById(R.id.publisher);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);

        }

        public void bind(final Article item, final ItemClickListener listener) {
            published_date.setText("Published Date\n"+item.getPublishedDate());
            title.setText(item.getTitle());
            short_description.setText(item.getShortDescription());
            publisher.setText(item.getPublisher());
            Glide.with(mContext).load(item.getThumbnail()).apply(bitmapTransform(new RoundedCornersTransformation(400, 3))).into(thumbnail);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
