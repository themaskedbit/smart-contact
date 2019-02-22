package com.themaskedbit.tempcontact.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.themaskedbit.tempcontact.R;
import com.themaskedbit.tempcontact.model.Contact;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    private List<Contact> contactsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, number;
        public ImageView photo;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            number = (TextView) view.findViewById(R.id.number);
            photo = (ImageView) view.findViewById(R.id.photo);
        }
    }


    public ContactAdapter(List<Contact> contactsList) {
        this.contactsList = contactsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Contact contact = contactsList.get(position);
        holder.name.setText(contact.getName() );
        holder.number.setText(contact.getFirstNumber());
        if(contact.getPhoto() != null){
            holder.photo.setImageURI(Uri.parse(contact.getPhoto()));
            if(holder.photo.getDrawable() == null)
                holder.photo.setImageResource(R.mipmap.contacts);
        }else
            holder.photo.setImageResource(R.mipmap.contacts);

    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }}

}
