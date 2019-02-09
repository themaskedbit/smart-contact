package com.themaskedbit.tempcontact;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {

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


    public ContactsAdapter(List<Contact> contactsList) {
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
        if(contact.getName() == null) {
            holder.name.setText("Unknown Number");
        }
        else
            holder.name.setText(contact.getName());
        holder.number.setText(contact.getNumber());
//        if(contact.getPhoto()== null){
//            holder.photo.setImageResource(R.drawable.ic_launcher_foreground);
//        }
//        else
//            holder.photo.setImageURI(Uri.parse(contact.getPhoto()));
        holder.photo.setImageResource(R.mipmap.contacts);
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }
}
