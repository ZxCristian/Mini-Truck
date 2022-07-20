package com.example.addtotruck.Adapters;

import android.annotation.SuppressLint;
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
import com.example.addtotruck.Models.Items;
import com.example.addtotruck.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class MainAdapter extends FirebaseRecyclerAdapter<Items,MainAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MainAdapter(@NonNull FirebaseRecyclerOptions<Items> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull Items model) {
        holder.Name.setText(model.getName());
        holder.Price.setText(model.getPrice());


        Glide.with(holder.imageView.getContext())
                .load(model.getImageurl())
                .placeholder(R.drawable.common_google_signin_btn_icon_dark_normal)
                .fitCenter()
                .into(holder.imageView);

        holder.btnBuy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.imageView.getContext())
                        .setContentHolder(new ViewHolder(R.layout.buyitem))
                        .setExpanded(true,1200)
                        .create();

                //dialogPlus.show();

                View view = dialogPlus.getHolderView();


                TextView name = view.findViewById(R.id.ItemName);
                TextView desc = view.findViewById(R.id.ItemDesc);
                TextView price = view.findViewById(R.id.ItemPrice);
                TextView url = view.findViewById(R.id.txtUrl);
                EditText quantity = view.findViewById(R.id.Quantity);

                EditText subtotal = view.findViewById(R.id.sub);

                Button btnUpdate = view.findViewById(R.id.btnUpdate);

                name.setText(model.getName());
                desc.setText(model.getDesc());
                price.setText(model.getPrice());
                url.setText(model.getImageurl());





                dialogPlus.show();



                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int sum =0;

                        Map<String,Object> map = new HashMap<>();
                        map.put("name",name.getText().toString());
                        map.put("price",price.getText().toString());
                        map.put("quantity",quantity.getText().toString());
                        map.put("imageurl",url.getText().toString());
                        Object price = map.get("price");
                        int pri =Integer.parseInt(String.valueOf(price));
                        Object quantity = map.get("quantity");
                        int qua =Integer.parseInt(String.valueOf(quantity));
                        sum = pri*qua;
                        subtotal.setText(String.valueOf(sum));
                        map.put("subtotal",subtotal.getText().toString());




                        /*DatabaseReference orders = FirebaseDatabase.getInstance().getReference().child("Orders");
                        orders.runTransaction(new Transaction.Handler() {
                            @Override
                            public Transaction.Result doTransaction(MutableData mutableData) {
                                Items map = mutableData.getValue(Items.class);
                                if (map == null) {
                                    mutableData.setValue(1);
                                    Toast.makeText(holder.Name.getContext(), "The Item has Added to Truck.", Toast.LENGTH_SHORT).show();
                                    dialogPlus.dismiss();
                                } else {
                                    mutableData.setValue(map);
                                    Toast.makeText(holder.Name.getContext(), "Error!!!", Toast.LENGTH_SHORT).show();
                                    dialogPlus.dismiss();
                                }

                                return Transaction.success(mutableData);
                            }

                            @Override
                            public void onComplete(
                                    DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                                Toast.makeText(holder.Name.getContext(), "Error!!!", Toast.LENGTH_SHORT).show();
                                System.out.println("Transaction completed");
                            }
                        }); */
                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                Task<Void> orders =  FirebaseDatabase.getInstance().getReference("Accounts").child(uid).child("Orders")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(unused -> {

                                    Toast.makeText(holder.Name.getContext(), "The Item has Added to Truck.", Toast.LENGTH_SHORT).show();
                                    dialogPlus.dismiss();
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.Name.getContext(), "Error!!!", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
            }
        });



    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_items,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView Name,Price,pricetext;

        Button btnBuy;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.imageName);
            Name = (TextView)itemView.findViewById(R.id.Name);
            Price = (TextView)itemView.findViewById(R.id.Price);
            pricetext = (TextView)itemView.findViewById(R.id.pricetext);



            btnBuy = (Button)itemView.findViewById(R.id.btnBuy);


        }


    }
}
