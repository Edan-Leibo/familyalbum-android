package com.example.adima.familyalbumproject.Model.SQL;

import android.util.Log;

import com.example.adima.familyalbumproject.Model.Entities.Comment.Comment;
import com.example.adima.familyalbumproject.Model.Firebase.ModelFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by adima on 04/03/2018.
 */

public class CommentFirebase {
    CommentFirebase(){

    }


    public interface Callback<T>{
        void onComplete(T data);
    }

    public static void getAllCommentsAndObserve(String albumId,final Callback<List<Comment>> callback){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("comments").child(albumId);


        ValueEventListener listener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    List<Comment> list = new LinkedList<Comment>();

                for(DataSnapshot snap:dataSnapshot.getChildren()){

                        Comment comment = snap.getValue(Comment.class);
                    Log.d("TAG",comment.getText());
                    list.add(comment);
                }

                callback.onComplete(list);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG","error in db");

                callback.onComplete(null);

            }
        });
    }

    public static void getAllCommentsAndObserve(String albumId,long lastUpdate,final Callback<List<Comment>> callback){
        Log.d("TAG", "getAllCommentsAndObserve " + lastUpdate);
        Log.d("TAG", "getAllCommentsAndObserve");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("comments");
        DatabaseReference myRef = database.getReference("comments").child(albumId);

        Query query = myRef.orderByChild("lastUpdated").startAt(lastUpdate);
        Log.d("TAG","the query is ok");

        ValueEventListener listener = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("TAG","the data changed");
                List<Comment> list = new LinkedList<Comment>();

                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Log.d("TAG","got the children");

                    Comment comment = snap.getValue(Comment.class);
                    Log.d("TAG","got the data in comment repository"+comment.getAlbumId());
                    Log.d("TAG","got the data in comment repository"+comment.getCommentId());
                    Log.d("TAG","got the data in comment repository"+comment.getText());

                    Log.d("TAG","got the data in comment repository"+comment.getUserId());

                    list.add(comment);
                }
                callback.onComplete(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onComplete(null);
            }
        });
    }

    public interface OnCreationComment{
        public void onCompletion(boolean success);
    }

    public static void addComment(String albumId, Comment comment, final OnCreationComment listener){
        Log.d("TAG", "add comment to firebase");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String key = database.getReference("comments").child(albumId).push().getKey();
       comment.setCommentId(key);

        HashMap<String, Object> json = comment.toJson();
        json.put("lastUpdated", ServerValue.TIMESTAMP);

        Log.d("TAG","the command id is:"+comment.getCommentId());
        DatabaseReference ref = database.getReference("comments").child(albumId).child(comment.getCommentId());
        ref.setValue(json, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e("TAG", "Error: Comment could not be saved "
                            + databaseError.getMessage());
                    listener.onCompletion(false);
                } else {
                    Log.e("TAG", "Success : Comment saved successfully.");
                    listener.onCompletion(true);

                }
            }
        });

    }

    public static void removeComment(Comment comment, final ModelFirebase.OnRemove listener){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("comments").child(comment.getAlbumId()).child(comment.getCommentId());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap: dataSnapshot.getChildren()) {
                    snap.getRef().removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError!=null){
                                listener.onCompletion(false);
                            }
                            else{
                                listener.onCompletion(true);
                            }

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });


    }

}