package com.example.addtotruck;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.addtotruck.Adapters.MainAdapter;
import com.example.addtotruck.Models.Items;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class shop extends AppCompatActivity {


    RecyclerView recyclerView;
    MainAdapter mainAdapter;

    FirebaseAuth FbAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_shop);


        recyclerView = findViewById(R.id.items);
        int numberOfColumns = 2;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, numberOfColumns );
        recyclerView.setLayoutManager(mLayoutManager);

        FirebaseRecyclerOptions<Items> options =
                new FirebaseRecyclerOptions.Builder<Items>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Items"), Items.class)
                        .build();




        mainAdapter = new MainAdapter(options);
        recyclerView.setAdapter(mainAdapter);

        recyclerView.setItemAnimator(null);



        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_home){
                    Toast.makeText(shop.this, "shop", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), shop.class));

                }
               else if(item.getItemId() == R.id.nav_gallery) {
                    Toast.makeText(shop.this, "cart", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), cart.class));
                }

                else if(item.getItemId() == R.id.nav_slideshow) {
                    Toast.makeText(shop.this, "Order History", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), History.class));
                }

                else if(item.getItemId() == R.id.logout) {
                    Toast.makeText(shop.this, "Login Page", Toast.LENGTH_SHORT).show();
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
        mainAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainAdapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                txtSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                txtSearch(query);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void txtSearch(String str)
    {

        FirebaseRecyclerOptions<Items> options =
                new FirebaseRecyclerOptions.Builder<Items>()

                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Items").orderByChild("name").startAt(str).endAt(str +"~"), Items.class)
                        .build();

        mainAdapter = new MainAdapter(options);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);
    }






}
