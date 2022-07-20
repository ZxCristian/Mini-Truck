package com.example.addtotruck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.addtotruck.Adapters.SubAdapter1;
import com.example.addtotruck.Models.Ordered;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class receipt extends AppCompatActivity{



    RecyclerView recyclerView;
    SubAdapter1 subAdapter1;
    Button PlaceOrder;
    TextView total1;



    private ListView listView;

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();



    private DatabaseReference database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reciept);

        recyclerView = (RecyclerView) findViewById(R.id.Oitems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        FirebaseRecyclerOptions<Ordered> options =

                new FirebaseRecyclerOptions.Builder<Ordered>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Accounts").child(uid).child("Ordered").child("OrderedItems"), Ordered.class)
                        .build();

        database = FirebaseDatabase.getInstance().getReference("Accounts").child(uid).child("Ordered").child("OrderedItems");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int t1 = 0;

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Map<String, Object> map = (Map<String, Object>) ds.getValue();
                    Object sub = map.get("subtotal");
                    int subt1 = Integer.parseInt(String.valueOf(sub));
                    t1 += subt1;
                    total1.setText(String.valueOf(t1));


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        subAdapter1 = new SubAdapter1(options);
        recyclerView.setAdapter(subAdapter1);
        recyclerView.setItemAnimator(null);


        PlaceOrder = findViewById(R.id.PlaceOrder);
        total1 = findViewById(R.id.Total1);
        listView = findViewById(R.id.listview);


        ArrayList<String> ShipAdd = new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.ordered, ShipAdd);
        listView.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Accounts").child(uid).child("Ordered").child("Address");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ShipAdd.clear();
                for (DataSnapshot Sadd : snapshot.getChildren()){
                ShipAdd.add(Sadd.getValue().toString());

            }
                adapter.notifyDataSetChanged();
        }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        PlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        toAdmin();
                        toHistory();
                startActivity(new Intent(getApplicationContext(), shop.class));



            }
        });

    }




    private void toAdmin() {

        DatabaseReference fromPath = FirebaseDatabase.getInstance().getReference("Accounts").child(uid).child("Ordered");
        fromPath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fromPath.child(snapshot.getKey());
                FirebaseDatabase.getInstance().getReference("Accounts").child("M57IR1QPpYX8bsLD7Vpe1UJT8V33").child("Customer_Orders").push()
                        .setValue(snapshot.getValue(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                if (error == null) {


                                    Toast.makeText(receipt.this, "Your Order has been Placed", Toast.LENGTH_SHORT).show();
                                    fromPath.setValue(null);
                                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    FirebaseDatabase.getInstance().getReference("Accounts").child(uid).child("Orders").removeValue();

                                } else {
                                    Toast.makeText(receipt.this, "Error Please Try Again Later", Toast.LENGTH_SHORT).show();
                                }

                            }

                        });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(receipt.this, "Error!!!", Toast.LENGTH_SHORT).show();

            }
        });


        }
    private void toHistory(){

        DatabaseReference fromPath = FirebaseDatabase.getInstance().getReference("Accounts").child(uid).child("Ordered");
        fromPath.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fromPath.child(snapshot.getKey());
                FirebaseDatabase.getInstance().getReference("Accounts").child(uid).child("History").push()
                        .setValue(snapshot.getValue(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                if (error == null) {

                                } else {
                                    Toast.makeText(receipt.this, "Error Please Try Again Later", Toast.LENGTH_SHORT).show();
                                }

                            }

                        });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(receipt.this, "Error!!!", Toast.LENGTH_SHORT).show();

            }
        });
    }




    @Override
    protected void onStart() {
        super.onStart();
        subAdapter1.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        subAdapter1.stopListening();
    }



}



