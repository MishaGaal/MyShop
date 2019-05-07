package mike.galitsky.myshop;


import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class CategoryItemsFragment extends Fragment {

    private RecyclerAdapter adapter;
    private RecyclerView listView;
    public ArrayList<Items> items;



    private static final String EXTRA_ITEMS = "mike.galitsky.myshop.EXTRA.ITEMS";


    public static CategoryItemsFragment newInstance(ArrayList<Items> items) {
        CategoryItemsFragment categoryItemsFragment = new CategoryItemsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(EXTRA_ITEMS, items);
        categoryItemsFragment.setArguments(args);

        return categoryItemsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items = getArguments().getParcelableArrayList(EXTRA_ITEMS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_items, container, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final Boolean nmod = preferences.getBoolean("switch_preference_1", false);
        if (nmod == true) {
            view.setBackgroundColor(getResources().getColor(R.color.NightModeBackground));
        }

        listView = view.findViewById(R.id.listView);
        listView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        init();

        return view;

    }

    private void init() {
        adapter = new RecyclerAdapter(
                getContext(),
                items
        );
        listView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecyclerAdapter.ClickListener() {
            @Override
            public void onClick(Items item, View view) {
                EditionFragment editionFragment = EditionFragment.newInstance(item);
                editionFragment.setActionListener(new EditionFragment.ActionListener() {
                    @Override
                    public void save(Items item) {
                        mListener.buy(item);
                        Toast.makeText(getContext(), "Added to basket!", Toast.LENGTH_SHORT).show();
                    }
                });
                editionFragment.show(getActivity().getSupportFragmentManager(), "dialog");
            }
        });

    }

    public interface ActionListener {
        void buy(Items item);

    }

    private ActionListener mListener;

    public void setActionListener(ActionListener listener) {
        mListener = listener;
    }


}
