package com.hcmus.dreamers.foodmap;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hcmus.dreamers.foodmap.Model.Owner;

public class ManageAccountActivity extends AppCompatActivity {

    ImageView ic_avatar;
    TextView lblChangeAvatar;
    EditText txtUserName;
    EditText txtRealName;
    EditText txtPhoneNumber;
    EditText txtEmail;
    Button buttonChangePass;
    EditText txtCurrentPass;
    EditText txtNewPass;
    LinearLayout passwordSection;

    Owner owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        takeReferenceFromResource();
        owner = Owner.getInstance();
        //putDataToViews();                  //TODO uncomment this when the data is ready

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        handleClickEvent();
    }

    private void handleClickEvent() {
        buttonChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int isVisible = passwordSection.getVisibility();
                if (isVisible == View.VISIBLE)
                    passwordSection.setVisibility(View.INVISIBLE);
                else
                    passwordSection.setVisibility(View.VISIBLE);
            }
        });

        lblChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "change avatar button clicked", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void putDataToViews() {
        //ic_avatar.setImageURI(owner.getImagePath());          //TODO consider add image path?
        txtUserName.setText(owner.getUsername());
        txtRealName.setText(owner.getName());
        txtPhoneNumber.setText(owner.getPhoneNumber());
        txtEmail.setText(owner.getPhoneNumber());
    }

    private void takeReferenceFromResource() {
        ic_avatar = (ImageView) findViewById(R.id.ic_avatar);
        lblChangeAvatar = (TextView) findViewById(R.id.lblChangeAvatar);
        txtUserName = (EditText) findViewById(R.id.txtUserName);
        txtRealName = (EditText) findViewById(R.id.txtRealName);
        txtPhoneNumber = (EditText) findViewById(R.id.txtPhoneNumber);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        buttonChangePass = (Button) findViewById(R.id.buttonChangePass);
        txtCurrentPass = (EditText) findViewById(R.id.txtCurrentPass);
        txtNewPass = (EditText) findViewById(R.id.txtNewPass);
        passwordSection = (LinearLayout) findViewById(R.id.passwordSection);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_delete:
                Toast.makeText(ManageAccountActivity.this, "action delete selected",
                        Toast.LENGTH_LONG).show();
                return true;

            case R.id.action_done:
                Toast.makeText(ManageAccountActivity.this, "action done selected",
                        Toast.LENGTH_LONG).show();

                if (checkInputValid() == false)
                {
                    Toast.makeText(ManageAccountActivity.this, "There's something wrong",
                            Toast.LENGTH_LONG).show();
                    return true;
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_item_menu,menu);
        return true;
    }

    private boolean checkInputValid() {
        return true;
    }
}
