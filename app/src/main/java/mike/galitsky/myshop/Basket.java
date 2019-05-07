package mike.galitsky.myshop;

public class Basket {

    public static final String TABLE_NAME = "BASKET";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ID_ITEM = "idItem";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_DESCRIPTION = "Description";
    public static final String COLUMN_PRICE = "Price";


    public long id;
    public long idItem;

    public Basket() {
    }

    public Basket(long id, long idStudent) {
        this.id = id;
        this.idItem = idStudent;
    }
}
