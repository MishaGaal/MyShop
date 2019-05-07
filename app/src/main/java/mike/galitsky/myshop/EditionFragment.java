package mike.galitsky.myshop;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class EditionFragment extends DialogFragment {
    private TextView editText;
    private TextView editText2;
    private TextView editText3;
    private TextView textView;


    private Items item;

    private static  final String EXTRA_ITEM = "mike.galitsky.lecture14.EXTRA.ITEM";

public static EditionFragment newInstance(Items item){

    EditionFragment editionFragment = new EditionFragment();

    Bundle args = new Bundle();
    args.putParcelable(EXTRA_ITEM, item);
    editionFragment.setArguments(args);
    return editionFragment;
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

        editText = view.findViewById(R.id.textViewName);
        editText2 = view.findViewById(R.id.textViewDescription);
        editText3 = view.findViewById( R.id.textViewPrice);
        textView = view.findViewById(R.id.textViewCategor);



        view.findViewById(R.id.buttonOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mListener != null){
                    mListener.save(item);
                }
                dismiss();
            }

        });

        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();
            }

        });
        init();
        return view;
    }

    private void init(){
        editText.setText(item.name);
        editText2.setText(item.description);
        editText3.setText("price: " + String.valueOf(item.price));
        textView.setText("category:"+ String.valueOf(item.category));
    }


    public interface ActionListener{
        void save(Items item);

    }

    private ActionListener mListener;

    public void setActionListener(ActionListener listener){
        mListener = listener;
    }

}
