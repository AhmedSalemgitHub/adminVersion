package com.developer.dreamadminpanel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;

import java.util.HashMap;

public class Dream extends AppCompatActivity {


    private String owner;
    private String dream;
    private String dreamDate;
    private int age;
    private String marriageStatus;
    private String Gender;
    private String Privacy;
    private String Reply;
    private String userEmail;
    private String replyStatus;
    private String openStatus;
    private String parentKey;

    TextView dateTextView;
    TextView ageTextView;
    TextView marriageTextView;
    TextView genderTextView;
    TextView isRepliedTextView;
    TextView userEmailTextView;
    EditText replyEditText;
    TextView ownerEditText;
    TextView dreamEditText;

    Button edit_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dream);

        ownerEditText = findViewById(R.id.owner_edit_view);
        dreamEditText = findViewById(R.id.dream_edit_view);

        dateTextView = findViewById(R.id.time_text_view);
        ageTextView = findViewById(R.id.age_text_view);
        marriageTextView = findViewById(R.id.marriage_text_view);
        genderTextView = findViewById(R.id.gender_text_view);
        isRepliedTextView = findViewById(R.id.isreplied_text_view);
        userEmailTextView = findViewById(R.id.userMail_text_view);
        replyEditText = findViewById(R.id.reply_edit_view);

        edit_button = findViewById(R.id.button_edit);
        String transferedprivacy = getIntent().getStringExtra("privacy");
        if (transferedprivacy != null) {
            Privacy = transferedprivacy ;
        }

        String transferedOwner = getIntent().getStringExtra("owner");
        if (transferedOwner != null) {
            owner = transferedOwner;
            ownerEditText.setText(transferedOwner);
        }

        String transferedDream= getIntent().getStringExtra("dreamDetails");
        if (transferedDream != null) {
            dreamEditText.setText(transferedDream);
            dream = transferedDream;
        }

        String transferedDate = getIntent().getStringExtra("date");
        if (transferedDate != null){
            dateTextView.setText(transferedDate);
            dreamDate = transferedDate;
        }

        String transferedAge= getIntent().getStringExtra("age");
        if (transferedAge != null) {
            age = Integer.parseInt(transferedAge) ;
            ageTextView.setText(transferedAge);
        }
        String transferedMarriageStatus= getIntent().getStringExtra("marriageStatus");
        if (transferedMarriageStatus != null) {
            marriageStatus = transferedMarriageStatus ;
            marriageTextView.setText(marriageStatus);
        }
        String transferedGender = getIntent().getStringExtra("gender");
        if (transferedGender != null) {
            Gender = transferedGender;
            genderTextView.setText(Gender);
        }

        String transferedReplyStatus = getIntent().getStringExtra("replystatus");
        if (transferedReplyStatus != null) {
            replyStatus = transferedReplyStatus;

        }
        String transferedOpenStatus = getIntent().getStringExtra("openedstatus");
        if (transferedOpenStatus != null) {
            openStatus = transferedOpenStatus;
        }
        String transferedUserEmail= getIntent().getStringExtra("userEmail");
        if (transferedUserEmail != null) {
            userEmail = transferedUserEmail;
            userEmailTextView.setText(userEmail);
        }
        String transferedReply = getIntent().getStringExtra("reply");
        if (transferedReply != null) {
            Reply = transferedReply;
            replyEditText.setText(Reply);
        }

        parentKey = getIntent().getStringExtra("parent");
        if(parentKey != null)
        {

        }


        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, Object> result = new HashMap<>();
                result.put("reply", replyEditText.getText().toString());
                result.put("replystatus","مفسر");
                result.put("openedstatus","مغلق");
                replyStatus = "مفسر";
                MainActivity.mMessageDataBaseReference.child(parentKey).updateChildren(result);
                finish();

            }
        });




    }

    @Override
    protected void onPause() {
        super.onPause();
        if(parentKey != null){

                HashMap<String, Object> result = new HashMap<>();

                result.put("openedstatus","مغلق");

                MainActivity.mMessageDataBaseReference.child(parentKey).updateChildren(result);
            }
        }


    @Override
    protected void onStop() {
        super.onStop();
        if(parentKey != null){

            HashMap<String, Object> result = new HashMap<>();

            result.put("openedstatus","مغلق");

            MainActivity.mMessageDataBaseReference.child(parentKey).updateChildren(result);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (parentKey != null) {

            HashMap<String, Object> result = new HashMap<>();

            result.put("openedstatus", "مغلق");

            MainActivity.mMessageDataBaseReference.child(parentKey).updateChildren(result);
        }
    }
}

