package com.example.addtotruck.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.addtotruck.Models.Orders;
import com.example.addtotruck.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SubAdapter extends FirebaseRecyclerAdapter<Orders,SubAdapter.ViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public SubAdapter(@NonNull FirebaseRecyclerOptions<Orders> options) {
        super(options);
    }

    private DatabaseReference database;


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull Orders model) {
        holder.Name.setText(model.getName());
        holder.Price.setText(model.getPrice());
        holder.Quantity.setText(model.getQuantity());
        holder.Subtotal.setText(model.getSubtotal());


        Glide.with(holder.imageView.getContext())
                .load(model.getImageurl())
                .placeholder(R.drawable.common_google_signin_btn_icon_dark_normal)
                .error(R.drawable.common_google_signin_btn_icon_dark)
                .into(holder.imageView);



        holder.itemUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus =DialogPlus.newDialog(holder.imageView.getContext())
                .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.update_item))
                        .setExpanded(true,800)
                        .create();

                //dialogPlus.show();

                View view = dialogPlus.getHolderView();

                TextView name = view.findViewById(R.id.ItemName);
                TextView price = view.findViewById(R.id.ItemPrice);
                EditText quantity = view.findViewById(R.id.Quantity);
                TextView subtotal = view.findViewById(R.id.sub1);


                Button Update = view.findViewById(R.id.Update);

                name.setText(model.getName());
                price.setText(model.getPrice());
                quantity.setText(model.getQuantity());
                subtotal.setText(model.getSubtotal());


                dialogPlus.show();

                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                database = FirebaseDatabase.getInstance().getReference("Accounts").child(uid).child("Orders");

                database.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int sum =0;

                        for (DataSnapshot ds : snapshot.getChildren()){
                            Map<String,Object>map = (Map<String, Object>) ds.getValue();
                            Object price = map.get("price");
                            int pri =Integer.parseInt(String.valueOf(price));
                            Object quantity = map.get("quantity");
                            int qua =Integer.parseInt(String.valueOf(quantity));
                            sum = pri*qua;
                            subtotal.setText(String.valueOf(sum));
                            map.put("subtotal",subtotal.getText().toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




                Update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int sum =0;
                        Map<String,Object> map = new HashMap<>();
                        map.put("quantity",quantity.getText().toString());
                        map.put("price",price.getText().toString());
                        Object price = map.get("price");
                        int pri =Integer.parseInt(String.valueOf(price));
                        Object quantity = map.get("quantity");
                        int qua =Integer.parseInt(String.valueOf(quantity));
                        sum = pri*qua;
                        subtotal.setText(String.valueOf(sum));
                        map.put("subtotal",subtotal.getText().toString());



                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        FirebaseDatabase.getInstance().getReference("Accounts").child(uid).child("Orders")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.Quantity.getContext(), "Quantity has been modified Sucessfully.",Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.Quantity.getContext(), "Error, Quantity has not changed.",Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
            }
        });

        holder.itemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.Name.getContext());
                builder.setTitle("Are You Sure?");
                builder.setMessage("Deleted data can't be Undo.");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        FirebaseDatabase.getInstance().getReference("Accounts").child(uid).child("Orders")
                                .child(Objects.requireNonNull(getRef(position).getKey())).removeValue();
                        Toast.makeText(holder.Name.getContext(), "Successfully Deleted", Toast.LENGTH_SHORT).show();

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(holder.Name.getContext(),"Cancelled",Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout,parent,false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView Name, Price, Quantity, Subtotal;
        Button itemUpdate,itemDelete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            imageView = (ImageView)itemView.findViewById(R.id.imageName);
            Name = (TextView) itemView.findViewById(R.id.Name);
            Price = (TextView) itemView.findViewById(R.id.Price);
            Quantity = (TextView) itemView.findViewById(R.id.Quantity);
            Subtotal = (TextView) itemView.findViewById(R.id.Subtotal);


            itemUpdate = (Button) itemView.findViewById(R.id.itemUpdate);
            itemDelete = (Button) itemView.findViewById(R.id.itemDelete);



        }

    }

}
