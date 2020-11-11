package com.example.dairy;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvTime;
    private TextView tvAuthor;
    private EditText editTitle;
    private EditText editText;
    private TextView edTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Button btn_save = findViewById(R.id.save_button);
        Button btn_cancel = findViewById(R.id.cancel_button);
        tvTime=findViewById(R.id.tv_time);
        tvAuthor=findViewById(R.id.tv_author);
        editTitle=findViewById(R.id.edit_title);
        editText=findViewById(R.id.edit_text);
        edTime=findViewById(R.id.tv_ed_time);
        btn_save.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        //日期设置
        SimpleDateFormat formatter =   new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
        Date curDate =  new Date(System.currentTimeMillis());
        String time= formatter.format(curDate);
        tvTime.setText("创建于："+time);
        edTime.setText("编辑于："+time);

        //作者设置
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        String author=sp.getString("loginUserName",null);
        tvAuthor.setText(author);

        //标题设置
        editTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

    }


    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.save_button:
                //标题设置
                String Title=editTitle.getText().toString();
                if(TextUtils.isEmpty(Title)){
                    Toast.makeText(AddActivity.this,"请输入标题",Toast.LENGTH_SHORT).show();
                    return;
                }
                //内容设置
                String time= tvTime.getText().toString();
                String author=tvAuthor.getText().toString();
                String title=editTitle.getText().toString();
                String note=editText.getText().toString();
                String updatetime=edTime.getText().toString();

                if(TextUtils.isEmpty(note)){
                    Toast.makeText(AddActivity.this,"您还没有输入内容呢",Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseHelper dbHelper=new DatabaseHelper(this,"Dairy.db",null,3);
                SQLiteDatabase db = dbHelper.getReadableDatabase();

                ContentValues values = new ContentValues();
                values.put("author",author);
                values.put("title",title);
                values.put("note",note);
                values.put("time",time);
                values.put("updatetime",updatetime);
                db.insert("dairy",null,values);
                dbHelper.close();
                Intent intent=new Intent(AddActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.cancel_button:
                Intent intent1=new Intent(AddActivity.this,MainActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }
}
