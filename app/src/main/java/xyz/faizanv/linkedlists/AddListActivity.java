package xyz.faizanv.linkedlists;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Arrays;

public class AddListActivity extends AppCompatActivity {

    Button submitButton;
    EditText title;
    EditText details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create New List");
        submitButton = (Button) findViewById(R.id.addNewList);
        title = (EditText) findViewById(R.id.title);
        details = (EditText) findViewById(R.id.details);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser user = ParseUser.getCurrentUser();
                ParseObject bucketList = new ParseObject("BucketList");
                bucketList.put("Title", title.getText().toString());
                bucketList.put("Details", details.getText().toString());
                bucketList.addAll("Users", Arrays.asList(user.getObjectId()));
                bucketList.put("Shared", false);
                bucketList.put("Completed", false);
                bucketList.saveInBackground();

                finish();
            }
        });

    }

}
