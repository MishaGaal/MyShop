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

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemsViewHolder> {

 private    ArrayList<Items> mItems;
  private    LayoutInflater mInflater;

    public RecyclerAdapter (Context context, ArrayList<Items> items){

        mItems = items;
        mInflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = mInflater.inflate(R.layout.item, viewGroup, false);
        return new ItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder itemsViewHolder, int i) {
        final Items item = mItems.get(i);
        itemsViewHolder.set(item);
        itemsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mListener != null){
                    mListener.onClick(item, view);
                }

            }
        });
    }

    public interface  ClickListener{
        public void onClick(Items item, View view);
    }

    private ClickListener mListener;

    public void setOnItemClickListener(ClickListener listener){
        mListener = listener;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ItemsViewHolder extends RecyclerView.ViewHolder{

        TextView textViewName, textViewDescription, textViewPrice, textViewCategor;

        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName =  itemView.findViewById(R.id.textViewName);
            textViewDescription =  itemView.findViewById(R.id.textViewDescription);
            textViewPrice =  itemView.findViewById(R.id.textViewPrice);
            textViewCategor =  itemView.findViewById(R.id.textViewCategor);
        }

        public void set (Items item){
            textViewName.setText(item.name);
            textViewDescription.setText(item.description);
            textViewCategor.setText("category:" + String.valueOf(item.category));
            textViewPrice.setText("price:" + String.valueOf(item.price));
        }
    }
}
