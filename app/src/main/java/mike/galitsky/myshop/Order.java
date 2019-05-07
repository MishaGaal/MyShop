package mike.galitsky.myshop;

import java.util.ArrayList;

public class Order {

    public String  Name;
    public String Phone;
    public String Comment;
    public String Adress;
    public String Email;
    public String City;
    public String Provinsi;
   public ArrayList<Items> OrderList;

    public Order() {
    }

    public Order (String name, String phone, String comment, String adress, String email, String city, String provinsi, ArrayList<Items> orderList) {
        this.Name = name;
        this.Phone = phone;
        this.Comment = comment;
        this.Adress = adress;
        this. Email = email;
        this.City = city;
        this. Provinsi = provinsi;
        this. OrderList = orderList;
    }

}
