package com.dudefinanceindia.meraghar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavPanelListAdapter extends BaseAdapter {

    private LayoutInflater inflater = null;

    private String[] menus = {"import", "gallery", "slideshow", "tools", "share", "send"};

    private int[] menuImg = {

            R.drawable.ic_dealer,
            R.drawable.ic_house,
            R.drawable.ic_logout,
            R.drawable.ic_automobile,
            R.drawable.ic_delivery,
            R.drawable.ic_ep_email

    };

            NavPanelListAdapter(Context con, String[] menus) {
                this.menus = menus;
            inflater = LayoutInflater.from(con);
            }

    @Override
    public int getCount() {
            return menus.length;
            }

    @Override
    public Object getItem(int i) {
            return menus.length;
            }

    @Override
    public long getItemId(int i) {
            return i;
            }



        @Override
    public View getView(int i, View convertview, ViewGroup viewGroup) {
            ViewHolder holder;
            if(convertview == null) {

            convertview =inflater.inflate(R.layout.nav_list_template,null);
            holder = new ViewHolder();
            holder.imgMenu = (ImageView) convertview.findViewById(R.id.menu_img);
            holder.txtMenu = (TextView) convertview.findViewById(R.id.mene_id);
            convertview.setTag(holder);

            } else {
            holder = (ViewHolder) convertview.getTag();
            }

            holder.txtMenu.setText(menus[i]);
            holder.imgMenu.setImageResource(menuImg[i]);
            return convertview;
            }

    public class ViewHolder {

        ImageView imgMenu;
        TextView txtMenu;

    }
}