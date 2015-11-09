package xyz.faizanv.linkedlists;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyListsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyListsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyListsFragment extends Fragment implements RecyclerItemClickListener.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    List<ParseObject> bucketListList;
    RecyclerView bucketListRecyclerView;
    SwipeRefreshLayout swipeRefreshLayout;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyListsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyListsFragment newInstance(String param1, String param2) {
        MyListsFragment fragment = new MyListsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MyListsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_lists, container, false);

        bucketListRecyclerView = (RecyclerView) root.findViewById(R.id.my_recycler_view);
        bucketListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        populateList();
        bucketListRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), this));
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                populateList();
            }
        });
        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(View childView, int position) {
        Intent intent = new Intent(getContext(), ListDetailActivity.class);
        intent.putExtra("Bucket", bucketListList.get(position).getObjectId());
        intent.putExtra("Name", bucketListList.get(position).getString("Title"));
        startActivity(intent);
    }

    @Override
    public void onItemLongPress(View childView, final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to delete this bucket?").setTitle("Delete bucket?");
        builder.setNegativeButton("Nah", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Yee", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                bucketListList.get(position).put("Completed", true);
                bucketListList.get(position).saveInBackground();
            }
        });
        builder.show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public void populateList() {
        ParseUser user = ParseUser.getCurrentUser();
        if (user != null) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("BucketList");
            query.whereEqualTo("Users", user.getObjectId());
            query.whereEqualTo("Completed", false);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        Log.d("Results", objects.size() + "");
                        bucketListList = objects;
                        bucketListRecyclerView.setAdapter(new BucketListAdapter(bucketListList));
                    } else {
                        Log.d("Error", e.toString());
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        populateList();
    }
}
