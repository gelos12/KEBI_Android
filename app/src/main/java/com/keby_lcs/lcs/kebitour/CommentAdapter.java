package com.keby_lcs.lcs.kebitour;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by LCS on 2017-11-09.
 */

public class CommentAdapter extends BaseAdapter {

    /* 아이템을 세트로 담기 위한 어레이 */
    private ArrayList<CommentItem> mItems;
    private Context context;

    public CommentAdapter(Context ctx, ArrayList<CommentItem> items) {
        super();
        this.context = ctx;
        this.mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public CommentItem getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ho = null ;
        /* 'listview_custom' Layout을 inflate하여 convertView 참조 획득 */
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.comment_item, parent, false);
            ho = inflater.inflate(R.layout.header,null,false);
        }

        // 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용
        CommentItem myItem = getItem(position);

        TextView textView = (TextView) convertView.findViewById(R.id.msg_content_view);
        textView.setText(myItem.getMsg());

         //'listview_custom'에 정의된 위젯에 대한 참조 획득

         //각 위젯에 세팅된 아이템을 뿌려준다
        /*if(myItem.getImage()==null){} //널일경우 이미지 안넣고
        else {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.with(context).load(myItem.getImage()).into(imageView);
            linearView.addView(imageView);
        }*/ //널이 아닌경우 이미지를 넣는다.
        // (위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..)
        return convertView;
    }

}