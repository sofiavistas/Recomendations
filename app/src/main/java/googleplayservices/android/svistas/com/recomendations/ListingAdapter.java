package googleplayservices.android.svistas.com.recomendations;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.plus.PlusOneButton;
import com.google.android.gms.plus.PlusShare;
import com.squareup.picasso.Picasso;

import googleplayservices.android.svistas.com.recomendations.api.Etsy;
import googleplayservices.android.svistas.com.recomendations.google.GoogleServicesHelper;
import googleplayservices.android.svistas.com.recomendations.model.ActiveListings;
import googleplayservices.android.svistas.com.recomendations.model.Listing;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by svistas on 14/12/16.
 */

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ListingHolder> implements Callback<ActiveListings>, GoogleServicesHelper.GoogleServicesListener {


    public static final int REQUEST_CODE_PLUS_ONE = 10;
    private static final int REQUEST_CODE_SHARE = 11 ;
    private boolean isGooglePlayServicesAvailable;

    private LayoutInflater inflater;
    private ActiveListings activeListings;
    private MainActivity activity;
    private boolean isPlayAvailable;


    public ListingAdapter(MainActivity activity){
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        this.isPlayAvailable = false;
    }

    @Override
    public ListingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ListingHolder(inflater.inflate(R.layout.layout_listing, parent, false));
    }

    @Override
    public void onBindViewHolder(ListingHolder holder, int position) {
        final Listing listing = activeListings.results[position];
        holder.titleView.setText(listing.title);
        holder.priceView.setText(listing.price);
        holder.shopNameView.setText(listing.Shop.shop_name);

        if(isGooglePlayServicesAvailable){
            holder.plusOneButton.setVisibility(View.VISIBLE);
            holder.plusOneButton.initialize(listing.url, REQUEST_CODE_PLUS_ONE);
            holder.plusOneButton.setAnnotation(PlusOneButton.ANNOTATION_NONE);

            holder.shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new PlusShare.Builder(activity)
                            .setType("text/plain")
                            .setText("Check this item on Etsy " + listing.title)
                            .setContentUrl(Uri.parse(listing.url))
                            .getIntent();

                    activity.startActivityForResult(intent, REQUEST_CODE_SHARE);
                }
            });

        } else {
            holder.plusOneButton.setVisibility(View.GONE);

            holder.shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, "Checkout this item on Etsy "
                            + listing.title + " " + listing.url );
                    intent.setType("text/plain");

                    activity.startActivityForResult(Intent.createChooser(intent, "Share"), REQUEST_CODE_SHARE );

                }
            });
        }

        Picasso.with(holder.imageView.getContext())
                .load(listing.Images[0].url_570xN)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {

        if(activeListings == null)
        return 0;

        if (activeListings.results == null)
            return 0;

        return activeListings.results.length;
    }

    @Override
    public void success(ActiveListings activeListings, Response response) {
        this.activeListings = activeListings;
        notifyDataSetChanged();
        this.activity.showList();
    }

    @Override
    public void failure(RetrofitError error) {

    }

    public ActiveListings getActiveListings(){
        return activeListings;
    }

    @Override
    public void onConnected() {

        if(getItemCount() == 0){
            Etsy.getActiveListings(this);
        }

        isGooglePlayServicesAvailable = true;
        notifyDataSetChanged();
    }

    @Override
    public void onDisconnected() {
        if(getItemCount() == 0){
            Etsy.getActiveListings(this);
        }

        isGooglePlayServicesAvailable = false;
        notifyDataSetChanged();
    }

    public class ListingHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView titleView;
        public TextView shopNameView;
        public TextView priceView;
        public PlusOneButton plusOneButton;
        public ImageButton shareButton;

        public ListingHolder(View itemView) {
            super(itemView);

            //TODO: replace with ButterKnife
            imageView = (ImageView) itemView.findViewById(R.id.listing_image);
            titleView = (TextView) itemView.findViewById(R.id.listing_title);
            shopNameView = (TextView) itemView.findViewById(R.id.listing_shop_name);
            priceView = (TextView) itemView.findViewById(R.id.listing_price);
            plusOneButton = (PlusOneButton) itemView.findViewById(R.id.listing_plus_one_btn);
            shareButton = (ImageButton) itemView.findViewById(R.id.listing_share_btn);
        }
    }
}
