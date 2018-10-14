package com.keby_lcs.lcs.kebitour;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.keby_lcs.lcs.kebitour.TourAPI.TourModel.Item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by LCS on 2017-11-09.
 */

public class MyAdapter extends BaseAdapter {

    /* 아이템을 세트로 담기 위한 어레이 */
    private ArrayList<Item> mItems;
    private Context context;

    public MyAdapter(Context ctx, ArrayList<Item> items) {
        super();
        this.context = ctx;
        this.mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Item getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        /* 'listview_custom' Layout을 inflate하여 convertView 참조 획득 */
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_custom, parent, false);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        ImageView imageView = (ImageView) convertView.findViewById(R.id.iconItem);
        TextView titleView = (TextView) convertView.findViewById(R.id.dataItem01);
        TextView contentView = (TextView) convertView.findViewById(R.id.dataItem02);

         /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        Item myItem = getItem(position);


        /* 각 위젯에 세팅된 아이템을 뿌려준다 */
        if(myItem.getFirstimage()==null){} //널일경우 이미지 안넣고
        else { Picasso.with(context).load(myItem.getFirstimage()).into(imageView); } //널이 아닌경우 이미지를 넣는다.
        titleView.setText(myItem.getTitle()); //제목을 채워넣는다.
        contentView.setText(String.valueOf(myItem.getReadcount()));
        /* (위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..)  */

        return convertView;
    }

}