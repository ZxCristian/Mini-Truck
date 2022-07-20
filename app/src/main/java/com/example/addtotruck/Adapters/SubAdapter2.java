package com.example.addtotruck.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.addtotruck.Models.Historymodel;
import com.example.addtotruck.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class SubAdapter2 extends FirebaseRecyclerAdapter<Historymodel,SubAdapter2.myViewHolder2> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public SubAdapter2(@NonNull FirebaseRecyclerOptions<Historymodel> options) { super(options); }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder2 holder, int position, @NonNull Historymodel model) {
        holder.HName.setText(model.getName());
        holder.HPrice.setText(model.getPrice());
        holder.HQuantity.setText(model.getQuantity());
        holder.HSubtotal.setText(model.getSubtotal());


        Glide.with(holder.HimageName.getContext())
                .load(model.getImageurl())
                .placeholder(R.drawable.common_google_signin_btn_icon_dark_normal)
                .error(R.drawable.common_google_signin_btn_icon_dark)
                .into(holder.HimageName);
    }



    @NonNull
    @Override
    public myViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rhistory,parent,false);
        return new myViewHolder2(view);
    }

    class myViewHolder2 extends RecyclerView.ViewHolder{

        ImageView HimageName;
        TextView HName,HPrice,HQuantity,HSubtotal;

        public myViewHolder2(@NonNull View itemView){
            super(itemView);

            HimageName = (ImageView) itemView.findViewById(R.id.HimageName);
            HName = (TextView) itemView.findViewById(R.id.HName);
            HPrice = (TextView) itemView.findViewById(R.id.HPrice);
            HQuantity = (TextView) itemView.findViewById(R.id.HQuantity);
            HSubtotal = (TextView) itemView.findViewById(R.id.HSubtotal);

        }
    }

}
