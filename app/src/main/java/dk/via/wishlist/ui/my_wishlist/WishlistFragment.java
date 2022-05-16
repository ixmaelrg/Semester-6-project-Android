package dk.via.wishlist.ui.my_wishlist;

import static android.content.ContentValues.TAG;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dk.via.wishlist.PreferenceConfig;
import dk.via.wishlist.R;
import dk.via.wishlist.User;

public class WishlistFragment extends Fragment {

    private WishlistViewModel mViewModel;

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://dream-bucket-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference myRef = database.getReference("users");

    public static WishlistFragment newInstance() {
        return new WishlistFragment();
    }

    ArrayList<String> arrayList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wishlist_fragment, container, false);
        return view;
    }

    private void GetWishlists() {
        String username = PreferenceConfig.getUsername(getContext());

        ListView listview = getView().findViewById(R.id.wish_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_list_item_1,
                arrayList);

        listview.setAdapter(adapter);

        myRef.child(username).child("wishlist").getRef().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String value = dataSnapshot.getKey();
                arrayList.add(value);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void GetWishesFromWishlist()
    {
        String username = PreferenceConfig.getUsername(getContext());
        ListView listview = getView().findViewById(R.id.wish_list);
        ArrayList<String> wishes = new ArrayList<>();

        ArrayAdapter<String> dialogAdapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.select_dialog_item,
                wishes);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.create();
        builder.setIcon(R.drawable.ic_wishlist);
        builder.setAdapter(dialogAdapter, null);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String wishlist = (String) adapterView.getAdapter().getItem(i);
                builder.setTitle("Wishes in " + wishlist);
                myRef.child(username).child("wishlist").child(wishlist).getRef().addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        String value = dataSnapshot.getValue(String.class);
                        if(!TextUtils.isEmpty(value)) {
                            wishes.add(value);
                            dialogAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                builder.show();
                wishes.clear();
                dialogAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(WishlistViewModel.class);
        GetWishlists();
        GetWishesFromWishlist();
        // TODO: Use the ViewModel
    }

}