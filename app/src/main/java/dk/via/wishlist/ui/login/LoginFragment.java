package dk.via.wishlist.ui.login;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dk.via.wishlist.PreferenceConfig;
import dk.via.wishlist.R;
import dk.via.wishlist.User;
import dk.via.wishlist.databinding.LoginFragmentBinding;

public class LoginFragment extends Fragment {

    private LoginFragmentBinding binding;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = LoginFragmentBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    FirebaseDatabase database = FirebaseDatabase.getInstance("https://dream-bucket-default-rtdb.europe-west1.firebasedatabase.app/");
    DatabaseReference myRef = database.getReference("users");

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.signin.setText("Sign In");
        binding.loginButton.setText("Login");
        binding.signupButton.setText("Sign Up");
        binding.username.setHint("Username");
        binding.password.setHint("Password");

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = binding.username.getText().toString();
                String password = binding.password.getText().toString();

                myRef.child(username).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            User user = dataSnapshot.getValue(User.class);
                            if(password.equals(user.password))
                            {
                                binding.password.clearFocus();
                                PreferenceConfig.updateLoginState(getContext(), username, true);
                                Toast.makeText(getContext(), "Welcome " + username + "!", Toast.LENGTH_LONG).show();
                                Navigation.findNavController(getView()).navigate(R.id.nav_home);
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Incorrect password. Please try again", Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Username not recognized. Please try again", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(TAG,"Error while reading data");
                    }
                });
            }
        });

        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    String username = binding.username.getText().toString();
                    String password = binding.password.getText().toString();

                    User user = new User(username, password);

                    DatabaseReference ref = database.getReference().push();

                    myRef.child(username).setValue(user);

                    binding.username.setText("");
                    binding.password.setText("");
                    binding.password.clearFocus();

                    new AlertDialog.Builder(getContext())
                            .setTitle("Thank you for signing up")
                            .setMessage("Please login to continue")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                }
            }
        });
    }
}