package mike.galitsky.myshop;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ActionFragment extends Fragment {

    private Items item;

    private static  final String EXTRA_ITEM = "mike.galitsky.lecture14.EXTRA.ITEM";

    public static ActionFragment newInstance(Items item){

        ActionFragment actionFragment = new ActionFragment();

        Bundle args = new Bundle();
        args.putParcelable(EXTRA_ITEM, item);
        actionFragment.setArguments(args);
        return actionFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        item = getArguments().getParcelable(EXTRA_ITEM);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edition, container, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final Boolean nmod = preferences.getBoolean("switch_preference_1", false);
        if(nmod == true){
            view.setBackgroundColor(getResources().getColor(R.color.NightModeBackground));
        }


        return view;
    }

    private void init(){

    }


    public interface ActionListener{
        void save(Items item);

    }

    private EditionFragment.ActionListener mListener;

    public void setActionListener(EditionFragment.ActionListener listener){
        mListener = listener;
    }

}

