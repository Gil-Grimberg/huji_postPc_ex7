package com.example.ex7;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class OrdersHolder {
    final int WAITING = 1;
    final int INPROGRESS = 2;
    final int READY = 3;
    final int DONE = 4;
    final int DELETED = 5;
    private Order myOrder;
    private final Context context;
    private SharedPreferences sp;
    private final MutableLiveData<Order> ordersLiveDataMutable = new MutableLiveData<>();
    public final LiveData<Order> ordersLiveDataPublic = ordersLiveDataMutable;
    public final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public OrdersHolder(Context context) {
        this.context = context;
        this.sp = context.getSharedPreferences("local_db", Context.MODE_PRIVATE);
        initializeFromSp();
        ordersLiveDataMutable.setValue(this.myOrder);
    }
    private void initializeFromSp() {
        String orderId = this.sp.getString("orderId","noId");
        String customer_name = this.sp.getString("customerName","noCustomer");
        String pickles = this.sp.getString("pickles","none");
        boolean hummus = this.sp.getBoolean("hummus",false);
        boolean tahini  = this.sp.getBoolean("tahini",false);
        String comment = this.sp.getString("comment","noComment");
        int status = this.sp.getInt("status",0);
        this.myOrder = new Order(orderId,customer_name,pickles,hummus,tahini,comment,status);

        }
    private void updateSp() {
        SharedPreferences.Editor editor = this.sp.edit();
        editor.putString("orderId",this.myOrder.orderId);
        editor.putString("customerName",this.myOrder.customer_name);
        editor.putString("pickles",this.myOrder.pickles);
        editor.putBoolean("hummus",this.myOrder.hummus);
        editor.putBoolean("tahini",this.myOrder.tahini);
        editor.putString("comment",this.myOrder.comment);
        editor.putInt("status",this.myOrder.status);
        editor.apply();
    }

    public void addNewOrder(String customer, String picklesNum, boolean isHummus, boolean isTahini, String comment)
    {

        this.myOrder.orderId = UUID.randomUUID().toString();;
        this.myOrder.customer_name = customer;
        this.myOrder.pickles = picklesNum;
        this.myOrder.hummus = isHummus;
        myOrder.tahini = isTahini;
        myOrder.comment = comment;
        myOrder.status = WAITING;

        Map<String, Object> order = new HashMap<>();
        order.put("orderId", this.myOrder.orderId);
        order.put("customer name", this.myOrder.customer_name);
        order.put("number of pickles", this.myOrder.pickles);
        order.put("hummus", this.myOrder.hummus);
        order.put("tahini", this.myOrder.tahini);
        order.put("comment", this.myOrder.comment);
        order.put("status", this.myOrder.status);

        updateSp();
// Add a new document with a generated ID
        db.collection("orders").document(this.myOrder.orderId).set(order);

        ordersLiveDataMutable.setValue(this.myOrder);

    }



    public void editOrder(String id, String customer, String picklesNum, boolean isHummus, boolean isTahini, String comment, int status)
    {
        this.myOrder.orderId = id;
        this.myOrder.status = status;
        this.myOrder.customer_name = customer;
        this.myOrder.pickles = picklesNum;
        this.myOrder.hummus = isHummus;
        this.myOrder.tahini = isTahini;
        this.myOrder.comment = comment;

        Map<String, Object> order = new HashMap<>();
        order.put("orderId", this.myOrder.orderId);
        order.put("customer name", this.myOrder.customer_name);
        order.put("number of pickles", this.myOrder.pickles);
        order.put("hummus", this.myOrder.hummus);
        order.put("tahini", this.myOrder.tahini);
        order.put("comment", this.myOrder.comment);
        order.put("status", this.myOrder.status);

        updateSp();

        db.collection("orders").document(myOrder.orderId).set(order);

        ordersLiveDataMutable.setValue(this.myOrder);


    }

    public void updateStatus(int status)
    {

        this.myOrder.status = status;

        updateSp();

        db.collection("orders").document(myOrder.orderId).update("status",status);

        ordersLiveDataMutable.setValue(this.myOrder);
    }
    public void setOrder(Order newOrder)
    {
        this.myOrder = newOrder;
        updateSp();
        ordersLiveDataMutable.setValue(this.myOrder);

    }

    public void deleteOrder(String id)
    {
        this.myOrder.status = DELETED;

        updateSp();

        db.collection("orders").document(id).delete();

        ordersLiveDataMutable.setValue(this.myOrder);
    }
    public Order getCurrentOrder()
    {
        return this.myOrder;
    }
}

