package com.tahmid78.flashchatnewfirebase;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by Tahmid78 on 1/5/2018.
 */

public class ChatListAdapter extends BaseAdapter {

    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private String mDsiplayName;
    private ArrayList<DataSnapshot> mSnapshotList;
    private ChildEventListener mListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            mSnapshotList.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public ChatListAdapter(Activity activity, DatabaseReference databaseReference, String dsiplayName) {
        mActivity = activity;
        mDatabaseReference = databaseReference.child("messages");
        mDatabaseReference.addChildEventListener(mListener);
        mDsiplayName = dsiplayName;
        mSnapshotList=new ArrayList<>();
    }

    static class ViewHolder{
        TextView authorName;
        TextView bodyM;
        LinearLayout.LayoutParams params;


    }

    @Override
    public int getCount() {
        return mSnapshotList.size();
    }

    @Override
    public InstantMessages getItem(int position) {

        DataSnapshot snapshot = mSnapshotList.get(position);
        return snapshot.getValue(InstantMessages.class);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            LayoutInflater inflater= (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chat_msg_row , parent, false);
            final ViewHolder holder = new ViewHolder();
            holder.authorName = (TextView) convertView.findViewById(R.id.author);
            holder.bodyM = (TextView) convertView.findViewById(R.id.message);
            holder.params = (LinearLayout.LayoutParams) holder.authorName.getLayoutParams();
            convertView.setTag(holder);

        }

        final  InstantMessages messages = getItem(position);
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        boolean isMe = messages.getAuthor().equals(mDsiplayName);
        setChatRowAppearance(isMe,holder);

        String author = messages.getAuthor();
        holder.authorName.setText(author);

        String messgage = messages.getMessages();
        holder.bodyM.setText(messgage);


        return convertView;
    }

    private void setChatRowAppearance(boolean isItMe, ViewHolder holder){
        if (isItMe){
            holder.params.gravity = Gravity.END;
            holder.authorName.setTextColor(Color.GREEN);
            holder.bodyM.setBackgroundResource(R.drawable.bubble2);
        }else {
             holder.params.gravity = Gravity.START;
             holder.authorName.setTextColor(Color.BLUE);
            holder.bodyM.setBackgroundResource(R.drawable.bubble1);
        }

        holder.authorName.setLayoutParams(holder.params);
        holder.bodyM.setLayoutParams(holder.params);
    }

    public void cleanUp(){
        mDatabaseReference.removeEventListener(mListener);
    }
}
