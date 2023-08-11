package com.example.appit.fragment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appit.R;

import java.util.ArrayList;
import java.util.LinkedList;

public class CustomGrid extends BaseAdapter {

    private Context mContext;
    private final LinkedList<Card> cards;

    public CustomGrid(Context c, LinkedList<Card> cards) {
        mContext = c;
        this.cards = cards;
    }

    public void addCardToList(ArrayList<Card> cardList) {
        ArrayList<Card> removeCards = new ArrayList<>();
        for (Card card : cardList) {
            if (card.isActive()) {
                cards.add(card);
            } else {
                for (Card old : cards) {
                    if (card.getName().equals(old.getName())) {
                        removeCards.add(old);
                    }
                }
            }
        }
        cards.removeAll(removeCards);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return cards.size();
    }

    @Override
    public Card getItem(int position) {
        // TODO Auto-generated method stub
        return cards.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//        if (convertView == null) {

//            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_item, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
//            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            String name = getItem(position).getName();
            Log.d("TAG", "getView name : " + name);
            textView.setText(name);
//        } else {
//            convertView = (View) grid;
//        }

        return grid;
    }


}

