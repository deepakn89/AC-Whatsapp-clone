package com.dnagaraj.ac_whatsapp_clone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity{
    private static final String TAG = "ChatActivity";

    private ListView chatListview;
    private ArrayList<String> arrayListChat;
    private ArrayAdapter arrayAdapterChat;

    private String selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatListview=findViewById(R.id.listviewChat);

        selectedUser=getIntent().getStringExtra("name");
        FancyToast.makeText(ChatActivity.this,"Chat with "+selectedUser.toString()+" now",Toast.LENGTH_SHORT,FancyToast.INFO,true).show();

        arrayListChat=new ArrayList<>();
        arrayAdapterChat=new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayListChat);
        chatListview.setAdapter(arrayAdapterChat);

        /** To list chat messages one after the other
         * **
          */
        try {
            ParseQuery<ParseObject> firstUserChatQuery = ParseQuery.getQuery("Chat");
            ParseQuery<ParseObject> secondUserChatQuery = ParseQuery.getQuery("Chat");

            firstUserChatQuery.whereEqualTo("sender", ParseUser.getCurrentUser().getUsername());
            firstUserChatQuery.whereEqualTo("recipient", selectedUser);

            secondUserChatQuery.whereEqualTo("sender", selectedUser);
            secondUserChatQuery.whereEqualTo("recipient", ParseUser.getCurrentUser().getUsername());

            ArrayList<ParseQuery<ParseObject>> allQueries = new ArrayList<>();
            allQueries.add(firstUserChatQuery);
            allQueries.add(secondUserChatQuery);

            ParseQuery<ParseObject> myQuery = ParseQuery.or(allQueries);
            myQuery.orderByAscending("createdAt");

            myQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size() > 0 && e == null) {
                        for (ParseObject chatObject : objects) {
                            String whatsappMessage = chatObject.get("message").toString();

                            if (chatObject.get("sender") == ParseUser.getCurrentUser().getUsername()) {
                                whatsappMessage = ParseUser.getCurrentUser().getUsername() + " : " + whatsappMessage;
                            }

                            if (chatObject.get("sender") == selectedUser) {
                                whatsappMessage = selectedUser + " : " + whatsappMessage;
                            }
                            arrayListChat.add(whatsappMessage);
                        }
                        arrayAdapterChat.notifyDataSetChanged();
                    }
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
        /**********************************************/
    }

    public void sendChatMsg(View view) {
        try {
            final EditText etChat=findViewById(R.id.etChat);;

            final ParseObject parseObject = new ParseObject("Chat");
            parseObject.put("recipient", Objects.requireNonNull(selectedUser));
            parseObject.put("sender", ParseUser.getCurrentUser().getUsername());
            parseObject.put("message",etChat.getText().toString());


            parseObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null){
                        FancyToast.makeText(ChatActivity.this, "Successfully sent msg", Toast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                        arrayListChat.add(ParseUser.getCurrentUser().getUsername()+": "+etChat.getText().toString());
                        arrayAdapterChat.notifyDataSetChanged();
                        etChat.setText("");
                    }
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}