package dk.via.wishlist.ui.wishlist;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dk.via.wishlist.PreferenceConfig;
import dk.via.wishlist.R;
import dk.via.wishlist.User;

public class CreateWishlistFragment extends Fragment {

    private CreateWishlistViewModel mViewModel;

    public static CreateWishlistFragment newInstance() {
        return new CreateWishlistFragment();
    }

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://dream-bucket-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference myRef = database.getReference("users");

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_wishlist_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CreateWishlistViewModel.class);

        getView().findViewById(R.id.addWishlistBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    String username = PreferenceConfig.getUsername(getContext());
                    EditText wishlistName = getView().findViewById(R.id.wishlistName);
                    String wishlistString = wishlistName.getText().toString();

                    DatabaseReference ref = database.getReference().push();

                    myRef.child(username).child("wishlist").child(wishlistString).push().setValue("");

                    new AlertDialog.Builder(getContext())
                            .setTitle("Wishlist Added!")
                            .setMessage(wishlistString + " has been added as a wishlist")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                }
            }
        });
    }

}