package mike.galitsky.myshop;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import java.util.ArrayList;


public class BasketFragment extends Fragment {

    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<Items> items;

    private static  final String EXTRA_ITEMS = "mike.galitsky.myshop.EXTRA.ITEMS";



    public static BasketFragment newInstance(ArrayList<Items> items){

        BasketFragment basketFragment = new BasketFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList(EXTRA_ITEMS, items);

        basketFragment.setArguments(args);

        return basketFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items = getArguments().getParcelableArrayList(EXTRA_ITEMS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_basket, container, false);

        recyclerView = view.findViewById(R.id.recycler);
       // LinearLayoutManager layout = new LinearLayoutManager(getContext());
        TranslateAnimation animation = new TranslateAnimation(0, 0, -1000, 0);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        view.setAnimation(animation);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        init();
        return view;
    }

    private void init(){

        adapter = new RecyclerAdapter(
                getContext(),
                items
        );
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecyclerAdapter.ClickListener() {
            @Override
            public void onClick(Items item, View view) {
                if(actionListener != null){
                    actionListener.onClick(item, view);
                }
            }
        });
    }

    ActionListener actionListener;

    public void setActionListener(ActionListener listener){
        actionListener = listener ;
    }

    public interface ActionListener{
        void onClick(Items item, View view);
    }

    ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            final int fromPos = viewHolder.getAdapterPosition();
            final int toPos = target.getAdapterPosition();

            Items item = items.get(fromPos);
            items.remove(fromPos);
            items.add(toPos, item);

            adapter.notifyItemMoved(fromPos, toPos);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            final int fromPos = viewHolder.getAdapterPosition();
            items.remove(fromPos);
            adapter.notifyItemRemoved(fromPos);
        }
    };
}
