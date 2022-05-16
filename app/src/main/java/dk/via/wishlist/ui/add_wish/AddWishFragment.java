package dk.via.wishlist.ui.add_wish;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dk.via.wishlist.MainActivity;
import dk.via.wishlist.PreferenceConfig;
import dk.via.wishlist.R;
import dk.via.wishlist.User;

public class AddWishFragment extends Fragment {

    private AddWishViewModel mViewModel;

    public static AddWishFragment newInstance() {
        return new AddWishFragment();
    }

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://dream-bucket-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference myRef = database.getReference("users");
    ArrayList<String> arrayList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_wish_fragment, container, false);

        ImageButton imageButton = (ImageButton)  view.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });

        return view;
    }

    private void GetWishlists() {
        String username = PreferenceConfig.getUsername(getContext());

        Spinner spinner = (Spinner) getView().findViewById(R.id.chooseWishlist);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                arrayList);

        spinner.setAdapter(adapter);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            ImageButton imageButton = (ImageButton)  getView().findViewById(R.id.imageButton);
            imageButton.setImageURI(selectedImage);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddWishViewModel.class);
        GetWishlists();

        Button addWishBtn = getView().findViewById(R.id.addWishBtn);
        addWishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    Spinner spinner = (Spinner) getView().findViewById(R.id.chooseWishlist);
                    EditText wishName = getView().findViewById(R.id.wishNameTitle);
                    String wish = wishName.getText().toString();
                    String wishlist = spinner.getSelectedItem().toString();
                    String username = PreferenceConfig.getUsername(getContext());

                    DatabaseReference ref = database.getReference().push();

                    myRef.child(username).child("wishlist").child(wishlist).push().setValue(wish);

                    new AlertDialog.Builder(getContext())
                            .setTitle("Wish Added!")
                            .setMessage(wish + " has been added to " + wishlist)
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                }
            }
        });
    }

}