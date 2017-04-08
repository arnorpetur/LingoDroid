package com.MJ.Lingo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.MJ.Lingo.models.UserCloudHandler;
import com.MJ.Lingo.models.WordCloudHandler;

public class MainActivity extends AppCompatActivity {
    private UserCloudHandler userCloudHandler;
    private WordCloudHandler wordCloudHandler;
    private Button mButton;
    private EditText mEditText;
    //private Button mButton2;
    public static Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wordCloudHandler = new WordCloudHandler();
        wordCloudHandler.getWords(this);
        userCloudHandler = new UserCloudHandler();
        userCloudHandler.getTopUsers(this);
        ctx = getApplicationContext();

        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText = (EditText) findViewById(R.id.editText);
                UnityController.addUserUnity(mEditText.getText().toString());
                Intent intent = new Intent(v.getContext(), UnityPlayerActivity.class);
                startActivity(intent);
            }

        });

    }


}
