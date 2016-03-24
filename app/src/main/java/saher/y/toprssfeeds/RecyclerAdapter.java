package saher.y.toprssfeeds;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Saher on 11/20/2015.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<RssDataParser.Item> mDataset;

    public RecyclerAdapter (Context context, ArrayList<RssDataParser.Item> dataset) {
        this.mContext = context;
        this.mDataset = dataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rss_feed_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final RssDataParser.Item item = mDataset.get(position);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(holder.cardView.getContext(), position + "", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(item.link));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        holder.rssTitle.setText(item.title);
        holder.rssDescription.setText(item.description);
//        holder.rssImg.setImageResource(item.thumbnail);
        if (item.thumbnail != null) {
            new downloadImage(holder.rssImg).execute(item.thumbnail);
        }
        else {
            holder.rssImg.setImageResource(R.drawable.rss);
        }
    }

    private class downloadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public downloadImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void clear() {
        mDataset.clear();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView rssTitle, rssDescription;
        ImageView rssImg;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view);
            rssTitle = (TextView) view.findViewById(R.id.item_title);
            rssImg = (ImageView) view.findViewById(R.id.img);
            rssDescription = (TextView) view.findViewById(R.id.item_description);
        }
    }

    public void add(int position, RssDataParser.Item item) {
        mDataset.add(position, item);
        notifyItemChanged(position);
    }

    public void remove(RssDataParser.Item item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }
}
