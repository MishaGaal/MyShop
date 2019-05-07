package mike.galitsky.myshop;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter2 extends RecyclerView.Adapter<RecyclerAdapter2.CardItemsViewHolder> {

    private ArrayList<Items> mItems;
    private LayoutInflater mInflater;

    public RecyclerAdapter2 (Context context, ArrayList<Items> items){

        mItems = items;
        mInflater = LayoutInflater.from(context);

    }




    public interface  ClickListener2{
        public void onClick2(Items item, View view);
    }

    private RecyclerAdapter2.ClickListener2 mListener;

    public void setOnItemClickListener2(ClickListener2 listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public CardItemsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = mInflater.inflate(R.layout.card_item, viewGroup, false);
        return new CardItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardItemsViewHolder cardItemsViewHolder, int i) {
        final Items item = mItems.get(i);
        cardItemsViewHolder.set(item);

        cardItemsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mListener != null){
                    mListener.onClick2(item, view);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class CardItemsViewHolder extends RecyclerView.ViewHolder{

        TextView textViewName, textViewDescription, textViewPrice, textViewCategor;

        public CardItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName =  itemView.findViewById(R.id.textViewName);
            textViewDescription =  itemView.findViewById(R.id.textViewDescription);
            textViewPrice =  itemView.findViewById(R.id.textViewPrice);
            textViewCategor =  itemView.findViewById(R.id.textViewCategor);
        }

        public void set (final Items item){
            textViewName.setText(item.name);
            textViewDescription.setText(item.description);
            textViewCategor.setText("category:" + String.valueOf(item.category));
            textViewPrice.setText("price:" + String.valueOf(item.price));
        }
    }
}
