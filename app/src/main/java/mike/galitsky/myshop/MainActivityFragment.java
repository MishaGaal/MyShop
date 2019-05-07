package mike.galitsky.myshop;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;


public class MainActivityFragment extends Fragment {


    private RecyclerAdapter adapter;
    private RecyclerAdapter2 adapter2;
    private RecyclerView recyclerView, recyclerView2;
    private ArrayList<Items> items;
    private BottomNavigationView navigationView;

    private static  final String EXTRA_ITEMS = "mike.galitsky.myshop.EXTRA.ITEMS";



    public static MainActivityFragment newInstance(ArrayList<Items> items){

    MainActivityFragment mainActivityFragment = new MainActivityFragment();

    Bundle args = new Bundle();
    args.putParcelableArrayList(EXTRA_ITEMS, items);

    mainActivityFragment.setArguments(args);

    return mainActivityFragment;
}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items = getArguments().getParcelableArrayList(EXTRA_ITEMS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view  = inflater.inflate(R.layout.fragment_main_activity, container, false);



        navigationView = view.findViewById(R.id.bottom_navigation);


        recyclerView = view.findViewById(R.id.recycler);
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        layout.setOrientation(LinearLayout.HORIZONTAL);
        recyclerView.setLayoutManager(layout);



        recyclerView2 = view.findViewById(R.id.recycler2);
        LinearLayoutManager layout2 = new LinearLayoutManager(getContext());
        layout2.setOrientation(LinearLayout.VERTICAL);
        recyclerView2.setLayoutManager(layout2);


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        navigationView.setSelectedItemId(R.id.action_home);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.action_home: {


                        break;
                    }
                    case R.id.action_category: {

                        startActivity(new Intent(getContext(), CategoriesActivity.class), ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                        break;
                    }

                    case R.id.action_basket: {

                        Intent intent = new Intent(getContext(), BasektActivity.class);
                        startActivity(intent);
                        break;
                    }

                    case R.id.action_info: {
                        startActivity(new Intent(getContext(), Info.class),
                                ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                        break;

                    }

                }

                return true;
            }
        });

        init();
        return view;
    }

    private void init(){

       adapter = new RecyclerAdapter(
                getContext(),
                items
        );

       adapter2 = new RecyclerAdapter2(
               getContext(),
               items
       );
        recyclerView.setAdapter(adapter);



        recyclerView2.setAdapter(adapter2);
        adapter.setOnItemClickListener(new RecyclerAdapter.ClickListener() {
            @Override
            public void onClick(Items item, View view) {
                if(actionListener != null){
                    actionListener.onClick(item, view);
                }
            }
        });

        adapter2.setOnItemClickListener2(new RecyclerAdapter2.ClickListener2() {
            @Override
            public void onClick2(final Items item, View view) {
                if(actionListener2 != null){
                    actionListener2.onClick2(item, view);
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



    ActionListener2 actionListener2;

    public void setActionListener2 (ActionListener2 listener){
        actionListener2 = listener ;
    }

    public interface ActionListener2{
        void onClick2(Items item, View view);
    }








    ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,
            ItemTouchHelper.UP| ItemTouchHelper.DOWN) {
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
