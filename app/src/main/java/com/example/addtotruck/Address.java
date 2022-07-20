package com.example.addtotruck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Address extends AppCompatActivity {

    EditText fullname, phonenum, RDC, street, landmark;
    Button delivery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        fullname = findViewById(R.id.EditFname);
        phonenum = findViewById(R.id.EditPhoneNum);
        RDC = findViewById(R.id.EditRCD);
        street = findViewById(R.id.EditStreet);
        landmark = findViewById(R.id.EditLandMark);
        delivery = findViewById(R.id.delivery);



        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pangalan,numero,reg,stret,marka;

                pangalan=fullname.getText().toString().trim();
                numero=phonenum.getText().toString().trim();
                reg=RDC.getText().toString().trim();
                stret=street.getText().toString().trim();
                marka=landmark.getText().toString().trim();

                if(pangalan.isEmpty()) {
                    fullname.setError("Fullname is Required");
                    fullname.requestFocus();
                    return;
                }
                if(numero.isEmpty()) {
                    phonenum.setError("Phone Number is Required");
                    phonenum.requestFocus();
                    return;
                }
                if(numero.length() == 10) {
                    phonenum.setError("Please Put a valid Phone Number");
                    phonenum.requestFocus();
                    return;
                }
                if(reg.isEmpty()) {
                    RDC.setError("Please Fill up properly");
                    RDC.requestFocus();
                    return;
                }


                if(stret.isEmpty()) {
                    street.setError("Please Fill up properly");
                    street.requestFocus();
                    return;
                }
                if(marka.isEmpty()) {
                    landmark.setError("Please Fill up properly");
                    landmark.requestFocus();
                    return;
                }
                shipdata();
                getall();


                startActivity(new Intent(getApplicationContext(), receipt.class));


            }
        });


    }

    private void shipdata() {

        HashMap<String, String> ShipAdd = new HashMap<>();

        ShipAdd.put("afullname", fullname.getText().toString());
        ShipAdd.put("bphonenum", phonenum.getText().toString());
        ShipAdd.put("crdc", RDC.getText().toString());
        ShipAdd.put("dstreet", street.getText().toString());
        ShipAdd.put("elandmark", landmark.getText().toString());

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("Accounts").child(uid).child("Ordered").child("Address")

                .setValue(ShipAdd)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(Address.this, "Please Double Check Your Order", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Address.this, "Error While Inserting Data", Toast.LENGTH_SHORT).show();
                    }
                });


    }

private void getall(){
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference fromPath =FirebaseDatabase.getInstance().getReference("Accounts").child(uid).child("Orders");
    fromPath.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            fromPath.child(snapshot.getKey());
            FirebaseDatabase.getInstance().getReference("Accounts").child(uid).child("Ordered").child("OrderedItems")
              .setValue(snapshot.getValue(), new DatabaseReference.CompletionListener() {
                  @Override
                  public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                      if (error == null) {



                      }

                      else{
                          Toast.makeText(Address.this, "Error Please Try Again Later", Toast.LENGTH_SHORT).show();
                      }

                  }

              });


        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Toast.makeText(Address.this, "Error!!!", Toast.LENGTH_SHORT).show();

        }
    });
}

}
