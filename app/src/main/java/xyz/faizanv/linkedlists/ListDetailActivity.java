package xyz.faizanv.linkedlists;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;

public class ListDetailActivity extends AppCompatActivity implements RecyclerItemClickListener.OnItemClickListener {

    List<ParseObject> list;
    RecyclerView recyclerView;
    String bucket;
    SwipeRefreshLayout swipeRefreshLayout;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.activityDetailLayout);

        Intent intent = getIntent();
        bucket = intent.getStringExtra("Bucket");
        String bucketName = intent.getStringExtra("Name");
        getSupportActionBar().setTitle(bucketName);
        final Intent addIntent = new Intent(this, AddListItemActivity.class);
        addIntent.putExtra("Bucket", bucket);

        recyclerView = (RecyclerView) findViewById(R.id.detail_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        populateList();
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, this));

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh2);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                populateList();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(addIntent);
            }
        });
    }

    public void populateList() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ListItem");
        query.whereEqualTo("Bucket", bucket);
        query.whereEqualTo("Completed", false);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.d("Results", objects.size() + "");
                    list = objects;
                    recyclerView.setAdapter(new BucketListAdapter(list));
                } else {
                    Log.d("Error", e.toString());
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateList();
    }

    @Override
    public void onItemClick(View childView, final int position) {
        //Done
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to mark this item as done?").setTitle("Done?");
        builder.setPositiveButton("Yee", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                list.get(position).put("Completed", true);
                list.get(position).saveInBackground();
            }
        });
        builder.setNegativeButton("Nah", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onItemLongPress(View childView, int position) {
        //Delete
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Enter email of who you want to share this bucket list with").setTitle("Share");
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            final EditText input = new EditText(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            builder.setView(input);
            builder.setPositiveButton("Share", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String email = input.getText().toString();
                    final ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("email", email);
                    query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(final List<ParseUser> objects, ParseException e) {
                            if (e == null) {
                                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("BucketList");
                                query1.getInBackground(bucket, new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject object, ParseException e) {
                                        object.put("Shared", true);
                                        object.addAllUnique("Users", Arrays.asList(objects.get(0).getObjectId()));
                                        object.saveInBackground();
                                        Snackbar.make(coordinatorLayout, object.getString("Title") + " has been shared",
                                                Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    }
                                });
                            } else {
                                Log.e("Error", e.toString());
                            }
                        }
                    });
                }
            });
            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
