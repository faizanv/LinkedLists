package xyz.faizanv.linkedlists;

import android.content.Intent;
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

public class AddListItemActivity extends AppCompatActivity {

    EditText title;
    EditText details;
    EditText where;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        final String bucket = intent.getStringExtra("Bucket");
        getSupportActionBar().setTitle("Add to " + bucket);
        title = (EditText) findViewById(R.id.item_title);
        details = (EditText) findViewById(R.id.item_details);
        where = (EditText) findViewById(R.id.item_where);
        Button submit = (Button) findViewById(R.id.addNewItem);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject bucketList = new ParseObject("ListItem");
                bucketList.put("Title", title.getText().toString());
                bucketList.put("Details", details.getText().toString());
                bucketList.put("Bucket", bucket);
                bucketList.put("Completed", false);
                bucketList.saveInBackground();

                finish();
            }
        });
    }

}
