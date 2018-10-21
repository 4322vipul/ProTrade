package com.aimheadshot.buynsell.Data;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aimheadshot.buynsell.Model.ExchangeItemInformation;
import com.aimheadshot.buynsell.Model.items;
import com.aimheadshot.buynsell.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by vipul on 28/9/18.
 */

public class exchangeRecyclerViewAdapter extends RecyclerView.Adapter<exchangeRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<ExchangeItemInformation> exchangeItemList;

    public exchangeRecyclerViewAdapter(Context context, List<ExchangeItemInformation> exchangeItemList) {
        this.context = context;
        this.exchangeItemList = exchangeItemList;
    }

    @Override
    public exchangeRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row,parent,false);

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(exchangeRecyclerViewAdapter.ViewHolder holder, int position) {

        ExchangeItemInformation exchangeItemInformation=exchangeItemList.get(position);
        String imageUrl=null;
        holder.exchange_item_name.setText(exchangeItemInformation.getItem_name());
        holder.exchange_item_price.setText(exchangeItemInformation.getItem_price());
        holder.exchange_item_desc.setText(exchangeItemInformation.getItem_desc());
      //  holder.exchange_seller_number.setText(exchangeItemInformation.getSeller_number());
        imageUrl= exchangeItemInformation.getImage();

        Picasso.with(context).load(imageUrl).into(holder.exchange_item_image);


    }

    @Override
    public int getItemCount() {
        return exchangeItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView exchange_item_name;
        public TextView exchange_item_price;
        public ImageView exchange_item_image;
        public TextView exchange_item_desc;
        public TextView exchange_seller_number;
        public ImageView exchange_call_button;
        public ImageView exchange_message_button;


        public ViewHolder(View view,Context ctx) {
            super(view);
            context=ctx;

            exchange_item_name=(TextView)view.findViewById(R.id.itemNameId);
            exchange_item_price=(TextView)view.findViewById(R.id.itemPriceId);
            exchange_item_desc=(TextView)view.findViewById(R.id.itemDescId);
            exchange_seller_number=(TextView)view.findViewById(R.id.SellerNumberId);
            exchange_item_image=(ImageView)view.findViewById(R.id.postImageList);
            exchange_call_button=(ImageView)view.findViewById(R.id.callButtonId);
            exchange_message_button=(ImageView)view.findViewById(R.id.messageButtonId);

            exchange_call_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"Calling",Toast.LENGTH_LONG).show();
                    Intent callIntent=new Intent(Intent.ACTION_CALL);
                    final ExchangeItemInformation exchangeItemInformation=exchangeItemList.get(getAdapterPosition());
                    callIntent.setData(Uri.parse("tel:+"+exchangeItemInformation.getSeller_number()));
                    context.startActivity(callIntent);
                  //  exchangeItemList.clear();

                }
            });

            exchange_message_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"Messaging",Toast.LENGTH_LONG).show();
                    ExchangeItemInformation exchangeItemInformation=exchangeItemList.get(getAdapterPosition());
                    Uri uri=Uri.parse("smsto:"+exchangeItemInformation.getSeller_number());
                    Intent messageIntent=new Intent(Intent.ACTION_SENDTO,uri);
                    context.startActivity(messageIntent);
                   // exchangeItemList.clear();
                }
            });


        }
    }
}
