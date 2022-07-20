package com.example.addtotruck;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.addtotruck.Adapters.SubAdapter;
import com.example.addtotruck.Models.Orders;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class cart extends AppCompatActivity {

    RecyclerView recyclerView;
    SubAdapter subAdapter;
    private DatabaseReference database;
    TextView total;
    FirebaseAuth FbAuth;


    Button checkout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_cart);


        recyclerView = (RecyclerView) findViewById(R.id.orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseRecyclerOptions<Orders> options =

                new FirebaseRecyclerOptions.Builder<Orders>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Accounts").child(uid).child("Orders"), Orders.class)
                        .build();

        database = FirebaseDatabase.getInstance().getReference("Accounts").child(uid).child("Orders");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int t =0;

                for (DataSnapshot ds : snapshot.getChildren()){
                    Map<String,Object> map = (Map<String, Object>) ds.getValue();
                    Object sub = map.get("subtotal");
                    int subt =Integer.parseInt(String.valueOf(sub));
                    t += subt;
                    total.setText(String.valueOf(t));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        subAdapter = new SubAdapter(options);
        recyclerView.setAdapter(subAdapter);
        recyclerView.setItemAnimator(null);


        checkout = (Button) findViewById(R.id.checkout);
        total = findViewById(R.id.Total);



        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),Address.class));
            }
        });


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_home){
                    Toast.makeText(cart.this, "shop", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), shop.class));

                }
                else if(item.getItemId() == R.id.nav_gallery) {
                    Toast.makeText(cart.this, "cart", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), cart.class));
                }

                else if(item.getItemId() == R.id.nav_slideshow) {
                    Toast.makeText(cart.this, "Order History", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), History.class));
                }
                else if(item.getItemId() == R.id.logout) {
                    Toast.makeText(cart.this, "Login Page", Toast.LENGTH_SHORT).show();
                    FbAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        subAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        subAdapter.stopListening();
    }



}