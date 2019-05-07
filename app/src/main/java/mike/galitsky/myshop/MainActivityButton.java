package mike.galitsky.myshop;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivityButton extends RelativeLayout {


    public MainActivityButton(Context context) {
        super(context);
        init(context, null);
    }

    public MainActivityButton (Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        View view = LayoutInflater.from(context).inflate(R.layout.main_activity_buttons, this);

        if(attrs !=null){
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MainActivityButton, 0 , 0);

            String text = array.getString(R.styleable.MainActivityButton_butonText);
            int img = array.getResourceId(R.styleable.MainActivityButton_buttonImg, 0);

            if(text != null) {

                TextView textView = view.findViewById(R.id.textView);
                textView.setText(text);
            }
            if(img > 0){
                ImageView imageView = view.findViewById(R.id.imageView);
                imageView.setImageResource(img);
            }

        }
    }
}

