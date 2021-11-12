package com.android.be_gain.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.android.be_gain.adapters.AdapterCategory;
import com.android.be_gain.databinding.ActivityDashboardAdminBinding;
import com.android.be_gain.models.ModelCategory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashboardAdminActivity extends AppCompatActivity
{
    // view binding
    private ActivityDashboardAdminBinding binding;

    // firebase auth
    private FirebaseAuth firebaseAuth;

    // arraylist to store category
    private ArrayList<ModelCategory> categoryArrayList;

    // adapter
    private AdapterCategory adapterCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();
        loadCategories();

        // edit text change listener, search
        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // called as an when use type each letter
                try {
                    adapterCategory.getFilter().filter(s);
                }
                catch (Exception e)
                {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // handle click, logout
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DashboardAdminActivity.this);
                builder.setTitle("Logout");
                builder.setMessage("Are you sure you want to logout?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        firebaseAuth.signOut();
                        checkUser();

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        checkUser();
                    }
                });
                builder.create().show();

//                firebaseAuth.signOut();
//                checkUser();
            }
        });

        // handle click, start category and screen
        binding.addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardAdminActivity.this, CategoryAddActivity.class));
            }
        });

        // handle click, start pdf add screen
        binding.addPdfFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardAdminActivity.this, PdfAddActivity.class));
            }
        });

        // hancle click, open profile
        binding.profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardAdminActivity.this, ProfileActivity.class));
            }
        });
    }

    private void loadCategories() {
        //init arraylist
        categoryArrayList = new ArrayList<>();

        // get all categories from firebase > Categories
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // clear arraylist before adding data into it
                categoryArrayList.clear();
                for (DataSnapshot ds: snapshot.getChildren())
                {
                    // get data
                    ModelCategory model = ds.getValue(ModelCategory.class);

                    // add to arraylist
                    categoryArrayList.add(model);
                }
                // setup adapter
                adapterCategory = new AdapterCategory(DashboardAdminActivity.this, categoryArrayList);

                // set adapter to recyclerview
                binding.categoriesRv.setAdapter(adapterCategory);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkUser()
    {
        // get current user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null)
        {
            // not logged in, go to main screen
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        else
        {
            // logged in, get user info
            String email = firebaseUser.getEmail();

            // set in textView of toolbar
            binding.subTitleTv.setText(email);
        }
    }
}