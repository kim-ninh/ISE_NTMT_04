package com.hcmus.dreamers.foodmap;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.hcmus.dreamers.foodmap.Model.Comment;
import com.hcmus.dreamers.foodmap.adapter.CommentListAdapter;
import com.hcmus.dreamers.foodmap.common.FoodMapManager;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener, CommentListAdapter.ClickListener {

    RecyclerView lstComment;
    EditText edtComment;
    ImageView igvComment;

    List<Comment> comments;
    CommentListAdapter commentListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_comment);

        comments = new ArrayList<Comment>();
        commentListAdapter = new CommentListAdapter(CommentActivity.this,R.layout.item_comment_list,comments);
        commentListAdapter.setOnClickListener(this);
        loadDataRecyclerView();

        lstComment = (RecyclerView)findViewById(R.id.lstComment);
        lstComment.setAdapter(commentListAdapter);

        edtComment = (EditText)findViewById(R.id.edtComment);
        igvComment = (ImageView)findViewById(R.id.igvComment);

        igvComment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.igvComment){
            //
            Toast.makeText(CommentActivity.this,"Send comment", Toast.LENGTH_LONG).show();
        }
    }

    void loadDataRecyclerView(){
        Intent intent = getIntent();
        if (intent != null){
            int id_rest = intent.getIntExtra("id_rest",-1);
            if (id_rest != -1){
                comments = FoodMapManager.getComment(CommentActivity.this,id_rest);
            }
        }
    }

    ///////////// item click
    @Override
    public void onItemClick(int position, View v) {

    }

    @Override
    public void onItemLongClick(int position, View v) {

    }
}
