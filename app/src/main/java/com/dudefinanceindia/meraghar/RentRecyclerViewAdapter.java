package com.dudefinanceindia.meraghar;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class RentRecyclerViewAdapter extends RecyclerView.Adapter<RentRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<DataForRecyclerView> UploadInfoList;

    RentRecyclerViewAdapter(Context context, List<DataForRecyclerView> TempList) {

        this.UploadInfoList = TempList;

        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewlist_rent, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final DataForRecyclerView info = UploadInfoList.get(position);

        String url = info.getProperty_image_1();
        Picasso.with(context)
                .load(url)
                .fit()
                .centerCrop()
                .into(holder.property_image_iv);

        holder.price_tv.setText(addComma(info.getPrice()));
        holder.price_tv.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.ic_rupee_black), null, null, null);
        holder.type_tv.setText(info.getType());
        holder.address_tv.setText(info.getAddress());
        String carpet_area = info.getCarpet_area();
        if (!TextUtils.isEmpty(carpet_area)){
            holder.carpet_area_tv.setText(carpet_area+" sq ft");
        }
        else {
            holder.carpet_area.setVisibility(View.GONE);
        }


    }

    private String addComma(String number){
        try {
            NumberFormat formatter = new DecimalFormat("#,###");
            return formatter.format(Integer.parseInt(number));
        }
        catch (Exception e){
            return "N.A";
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return UploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView property_image_iv;
        private TextView price_tv, address_tv, carpet_area_tv, type_tv, carpet_area;

        ViewHolder(View itemView) {
            super(itemView);

            property_image_iv = itemView.findViewById(R.id.vr_property_image_iv);
            price_tv = itemView.findViewById(R.id.vr_price_tv);
            address_tv = itemView.findViewById(R.id.vr_address_tv);
            carpet_area_tv = itemView.findViewById(R.id.vr_carpet_area_tv);
            carpet_area = itemView.findViewById(R.id.vr_carpet_area);
            type_tv = itemView.findViewById(R.id.vr_type_tv);

        }
    }
//end
}