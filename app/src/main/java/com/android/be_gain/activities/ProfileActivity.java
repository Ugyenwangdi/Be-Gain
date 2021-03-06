package com.android.be_gain.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.be_gain.MyApplication;
import com.android.be_gain.R;
import com.android.be_gain.databinding.ActivityProfileBinding;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    // view binding
    private ActivityProfileBinding binding;

    // firebase auth, for loading user data using user id
    private FirebaseAuth firebaseAuth;

    private static final String TAG = "PROFILE_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // set up firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        loadUserInfo();

        // handle click, start profile edit page
        binding.profileEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ProfileEditActivity.class));
            }
        });

        // handle click, go back
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void loadUserInfo() {
        Log.d(TAG,"LoadUserInfo: Loading user info of user "+firebaseAuth.getUid());

        // get current user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null)
        {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(firebaseAuth.getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            // get all info of user here from snapshot
                            String email = ""+snapshot.child("email").getValue();
                            String name = ""+snapshot.child("name").getValue();
                            String profileImage = ""+snapshot.child("profileImage").getValue();
                            String timestamp = ""+snapshot.child("time").getValue();
                            String uid = ""+snapshot.child("uid").getValue();
                            String userType = ""+snapshot.child("userType").getValue();

                            // format date to dd/MM/yyyy
                            String formattedDate = MyApplication.formatTimestamp(Long.parseLong(timestamp));

                            // set data to ui
                            binding.emailTv.setText(email);
                            binding.nameTv.setText(name);
                            binding.memberDateTv.setText(formattedDate);
                            binding.accountTypeTv.setText(userType);

                            // set image, using glide
                            Glide.with(ProfileActivity.this)
                                    .load(profileImage)
                                    .placeholder(R.drawable.ic_person_gray)
                                    .into(binding.profileIv);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }

    }
}