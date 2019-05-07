package mike.galitsky.myshop;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.transition.Slide;
import android.transition.Visibility;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;


public class OrderFragment extends DialogFragment {


  private   EditText  edtComment, edtCity, edtProvinsi;
  private RequiredEditText edtName, edtAdress, edtPhone, edtEmail;
  private static  final String EXTRA_ITEMS = "mike.galitsky.lecture14.EXTRA.ITEMS";


  private ArrayList<Items> items;
   private ArrayList<String> orders;
   private Order order = new Order();


    public static OrderFragment newInstance(ArrayList<Items> items){

        OrderFragment orderFragment = new OrderFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList(EXTRA_ITEMS, items);


        orderFragment.setArguments(args);

        return orderFragment ;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items = getArguments().getParcelableArrayList(EXTRA_ITEMS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final Boolean nmod = preferences.getBoolean("switch_preference_1", false);
        if(nmod == true){
            view.setBackgroundColor(getResources().getColor(R.color.NightModeBackground));
        }

        edtName = view.findViewById(R.id.edtName);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtPhone = view.findViewById(R.id.edtPhone);
        edtComment = view.findViewById(R.id.edtComment);
        edtAdress = view.findViewById(R.id.edtAdress);
        edtCity = view.findViewById(R.id.edtCity);
        edtProvinsi = view.findViewById(R.id.edtProvinsi);

        edtName.setText(preferences.getString("edit_text_name", null));
        edtEmail.setText(preferences.getString("edit_text_email", null));
        edtPhone.setText(preferences.getString("edit_text_phone", null));


        view.findViewById(R.id.buttonSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (mListener != null ) {
                       if(edtName.validate() & edtAdress.validate() & edtPhone.validate() & edtEmail.validate() & items.size() > 0 ) {
                            mListener.save(init());
                            dismiss();
                        }else if(items.size() == 0 ){
                               Toast.makeText(getContext()," Empty Basket", Toast.LENGTH_LONG).show();
                       }
                    }
            }

        });

        return view;
    }

    private Order init(){
            order.Name = (edtName.getText().toString());
            order.Email = (edtEmail.getText().toString());
            order.Phone = (edtPhone.getText().toString());
            order.Comment = (edtComment.getText().toString());
            order.Adress = (edtAdress.getText().toString());
            order.City = (edtCity.getText().toString());
            order.Provinsi = (edtProvinsi.getText().toString());
            order.OrderList = items;
            return order;
    }

    public interface ActionListener{
        void save(Order order);

    }

    private ActionListener mListener;

    public void setActionListener(ActionListener listener){
        mListener = listener;
    }


}


