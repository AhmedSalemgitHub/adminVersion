package com.developer.dreamadminpanel;


import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

        public static MyAdapter adapter;
        public ListView listView;
        public static ArrayList<myDreamItems> mainDreamList;
        public static DatabaseReference mMessageDataBaseReference;
        Query query;

        /* initialize fire base variables
        * Param: mFirebaseDataBase*/
        private FirebaseDatabase mFirebaseDataBase; //the database
        private ChildEventListener mChildEventListener; // detect the changes in the data base

        @Override
        protected void onCreate(Bundle savedInstanceState) {


            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            mFirebaseDataBase = FirebaseDatabase.getInstance();
            mMessageDataBaseReference = mFirebaseDataBase.getReference().child("dream");
            query= mMessageDataBaseReference.orderByChild("replystatus").equalTo("غير مفسر");
            Log.i("test",mMessageDataBaseReference.toString());
            // Initialize message ListView and its adapter
            mainDreamList = new ArrayList<myDreamItems>();
            adapter = new MyAdapter(this, mainDreamList);
            attachedDataBaseReadListener();

            listView = findViewById(R.id.list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    final myDreamItems currentSelected = mainDreamList.get(position);
                    DatabaseReference CurrentKey = mMessageDataBaseReference.child(currentSelected.getParentKey()).child("openedstatus");
                    Log.i("ahmedtest",currentSelected.toString());
                    CurrentKey.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String openstatus = dataSnapshot.getValue().toString();

                            if (openstatus.equals("مفتوح")){
                                Toast.makeText(MainActivity.this,"هذا الحلم مفتوح من احد المستخدمين",Toast.LENGTH_LONG).show();

                            }else{
                                Intent openDream = new Intent(MainActivity.this, Dream.class);

                                openDream.putExtra("owner", currentSelected.getOwner());
                                openDream.putExtra("dreamDetails", currentSelected.getDreamDetails());
                                openDream.putExtra("age",String.valueOf(currentSelected.getAge()));
                                openDream.putExtra("marriageStatus",currentSelected.getMaritalStatus());
                                openDream.putExtra("gender",currentSelected.getGender());
                                openDream.putExtra("date",currentSelected.getDreamTime());
                                openDream.putExtra("openedstatus",currentSelected.getOpenedstatus());
                                openDream.putExtra("replystatus",currentSelected.getReplystatus());
                                openDream.putExtra("userEmail",currentSelected.getOwnerEmail());
                                openDream.putExtra("reply",currentSelected.getReply());
                                openDream.putExtra("parent",currentSelected.getParentKey());
                                openDream.putExtra("mode", "view");

                                HashMap<String, Object> result = new HashMap<>();
                                result.put("openedstatus","مفتوح");
                                mMessageDataBaseReference.child(currentSelected.getParentKey()).updateChildren(result);
                                adapter.notifyDataSetChanged();

                                startActivity(openDream);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            });
        }

        private void attachedDataBaseReadListener() {
            adapter.clear();
            if (mChildEventListener == null) {
                mChildEventListener = new ChildEventListener() {

                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        myDreamItems dreams = dataSnapshot.getValue(myDreamItems.class);
                        dreams.setParentKey(dataSnapshot.getRef().getKey());
                        mainDreamList.add(dreams);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        String key = dataSnapshot.getKey();
                        for (myDreamItems dream : mainDreamList){
                            if(key.equals(dream.getParentKey())){
                                mainDreamList.remove(dream);
                                adapter.notifyDataSetChanged();
                                break;
                            }
                        }
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };
                mMessageDataBaseReference.addChildEventListener(mChildEventListener);
            }
        }

        private void detachedDataBaseReadListener() {
            adapter.clear();
            if (mChildEventListener != null) {
                mMessageDataBaseReference.removeEventListener(mChildEventListener);
                mChildEventListener = null;
            }
        }

        @Override
        protected void onResume() {
            super.onResume();
            Locale locale = new Locale("Ar");
            Locale.setDefault(locale);
            Configuration config = getBaseContext().getResources().getConfiguration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            attachedDataBaseReadListener();
        }

        @Override
        protected void onPause() {
            super.onPause();
            adapter.notifyDataSetChanged();
            detachedDataBaseReadListener();
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.querymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        Intent query = new Intent(MainActivity.this, not_translated.class);
//        startActivity(query);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();
                for (DataSnapshot queryDream : dataSnapshot.getChildren()){
                    myDreamItems qDream = queryDream.getValue(myDreamItems.class);
                    qDream.setParentKey(queryDream.getRef().getKey());
                    adapter.add(qDream);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return super.onOptionsItemSelected(item);
    }
}

