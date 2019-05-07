package mike.galitsky.myshop;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class Items implements Parcelable {

    public static final String TABLE_NAME = "ShopDataBase";
    public static final String COLUMN_ID = "_id"   ;
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_DESCRIPTION = "Description";
    public static final String COLUMN_PRICE = "Price";


    @SerializedName("id")
    public long id ;
    @SerializedName("name")
    public String name;
    @SerializedName("description")
    public String description;
    @SerializedName("price")
    public int price;
    @SerializedName("category")
    public int category;
   // public int count;


    public Items(String name, String description, int price, int category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }

    public Items() {
    }


    public  String  toString(){
        return String.format("Name : %s, %s, Price: %d, Category: %d" , name, description, price, category);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeInt(this.price);
        dest.writeInt(this.category);
       // dest.writeInt(this.count);
    }

    protected Items(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.description = in.readString();
        this.price = in.readInt();
        this.category = in.readInt();
        //this.count = in.readInt();
    }

    public static final Creator<Items> CREATOR = new Creator<Items>() {
        @Override
        public Items createFromParcel(Parcel source) {
            return new Items(source);
        }

        @Override
        public Items[] newArray(int size) {
            return new Items[size];
        }
    };
}
