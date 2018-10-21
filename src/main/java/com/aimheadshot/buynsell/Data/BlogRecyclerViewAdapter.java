package com.aimheadshot.buynsell.Data;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.Tag;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aimheadshot.buynsell.Model.items;
import com.aimheadshot.buynsell.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import static android.Manifest.permission.CALL_PHONE;

/**
 * Created by vipul on 22/9/18.
 */

public class BlogRecyclerViewAdapter extends RecyclerView.Adapter<BlogRecyclerViewAdapter.ViewHolder>{

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 11;
    private Context context;
    private List<items> itemList;

    public BlogRecyclerViewAdapter(Context context, List<items> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_row,parent,false);


        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        items item=itemList.get(position);
        String imageUrl=null;

        holder.itemName.setText(item.getItem_name());
        holder.itemName.setMovementMethod(new ScrollingMovementMethod());
        holder.itemPrice.setText(item.getItem_price());
        holder.itemDescription.setText(item.getItem_desc());
        holder.itemDescription.setMovementMethod(new ScrollingMovementMethod());
        //holder.sellerNumber.setText(item.getSeller_number());


        imageUrl=item.getImage();


        Picasso.with(context).load(imageUrl).into(holder.itemImage);
       // Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(),R.drawable.profilepic);
       // holder.itemImage.setImageBitmap(bitmap);

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView itemName;
        public TextView itemPrice;
        public TextView itemDescription;
        public TextView sellerNumber;
        public ImageView itemImage;
        public ImageView callButton;
        public ImageView messageButton;




        public ViewHolder(View view,Context ctx) {
            super(view);
            context=ctx;

            itemName=(TextView)view.findViewById(R.id.itemNameId);
            itemPrice=(TextView)view.findViewById(R.id.itemPriceId);
            itemDescription=(TextView)view.findViewById(R.id.itemDescId);
            sellerNumber=(TextView)view.findViewById(R.id.SellerNumberId);
            itemImage=(ImageView)view.findViewById(R.id.postImageList);
            callButton=(ImageView)view.findViewById(R.id.callButtonId);
            messageButton=(ImageView)view.findViewById(R.id.messageButtonId);



            callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"Calling",Toast.LENGTH_LONG).show();
                    Intent callIntent=new Intent(Intent.ACTION_CALL);
                    final items item=itemList.get(getAdapterPosition());
                    callIntent.setData(Uri.parse("tel:+"+item.getSeller_number()));
                    if(ContextCompat.checkSelfPermission(context,CALL_PHONE)== PackageManager.PERMISSION_GRANTED){
                        context.startActivity(callIntent);
                        itemList.clear();
                    }
                    else{
                        //Toast.makeText(context,"Failed",Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{Manifest.permission.CALL_PHONE},
                                MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    }

                }
            });
            messageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"Messaging",Toast.LENGTH_LONG).show();
                   final items item=itemList.get(getAdapterPosition());
                    Uri uri=Uri.parse("smsto:"+item.getSeller_number());
                    Intent messageIntent=new Intent(Intent.ACTION_SENDTO,uri);
                    context.startActivity(messageIntent);
                    itemList.clear();
                }
            });


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Snackbar.make(v,"Go to next Activity",Snackbar.LENGTH_LONG).setAction("Action",null).show();
                }
            });
        }
    }
}
