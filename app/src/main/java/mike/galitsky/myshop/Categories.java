package mike.galitsky.myshop;

import android.os.Parcel;
import android.os.Parcelable;

public class Categories implements Parcelable {

    public static final String TABLE_NAME = "CATEGORIES";
    public static final String CATEGORY_NAME = "CATEGORY";
    public static final String COLUMN_CATEGORY_ID = "_idCategory"  ;


    public String name;
    public   int id;

    public Categories ( String name , int id) {
        this.name = name;
        this.id =  id;
    }

    public Categories() {
    }

    public String toString(){

        return  String.format("%s", name );
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.id);
    }

    protected Categories(Parcel in) {
        this.name = in.readString();
        this.id = in.readInt();
    }

    public static final Creator<Categories> CREATOR = new Creator<Categories>() {
        @Override
        public Categories createFromParcel(Parcel source) {
            return new Categories(source);
        }

        @Override
        public Categories[] newArray(int size) {
            return new Categories[size];
        }
    };
}
