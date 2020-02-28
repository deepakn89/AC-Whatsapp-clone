package com.dnagaraj.ac_whatsapp_clone;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class WhatsAppUsers extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView whatssappListview;
    private ArrayList<String> arrayList_whatsapp;
    private ArrayAdapter<String> arrayAdapter_whatsapp;

    androidx.swiperefreshlayout.widget.SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app_users);

        whatssappListview=findViewById(R.id.listView_whatsappUsers);
        arrayList_whatsapp=new ArrayList<>();
        arrayAdapter_whatsapp=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList_whatsapp);

        //for list to allow multiple choices
        whatssappListview.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        whatssappListview.setOnItemClickListener(this);

        try{
            ParseQuery<ParseUser> parseQuery=ParseUser.getQuery();
            parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if(e==null && objects.size()>0){
                        for(ParseUser user:objects){
                            arrayList_whatsapp.add(user.getUsername().toString());
                        }
                        whatssappListview.setAdapter(arrayAdapter_whatsapp);
                    }else{
                        FancyToast.makeText(WhatsAppUsers.this,e.getMessage(), Toast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        swipeRefreshLayout=findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ParseQuery<ParseUser> parseQuery=ParseUser.getQuery();
                parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
                parseQuery.whereNotContainedIn("username",arrayList_whatsapp);
                parseQuery.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        if(e==null && objects.size()>0){
                            for(ParseUser user:objects){
                                arrayList_whatsapp.add(user.getUsername().toString());
                            }
                            arrayAdapter_whatsapp.notifyDataSetChanged();
                            if(swipeRefreshLayout.isRefreshing())
                                swipeRefreshLayout.setRefreshing(false);
                        }else{
                            if(swipeRefreshLayout.isRefreshing())
                                swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logoutMenu){
            FancyToast.makeText(WhatsAppUsers.this, ParseUser.getCurrentUser().getUsername()+" logged out !", Toast.LENGTH_SHORT,FancyToast.INFO,true).show();
            ParseUser.getCurrentUser();
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        startActivity(new Intent(WhatsAppUsers.this,MainActivity.class));
                        finish();
                    }else{
                        FancyToast.makeText(WhatsAppUsers.this, e.getMessage(), Toast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                    }
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}