package com.example.dairy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindActivity extends AppCompatActivity {
    private EditText editText;
    private List<String> list= new ArrayList();//存数据
    final Map m = new HashMap();
    ListView listView=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        Button btn_search= findViewById(R.id.search_button);
        Button btn_back= findViewById(R.id.back_button);
        editText =  findViewById(R.id.edit_text);

        final DatabaseHelper dbHelper = new  DatabaseHelper(this,"Dairy.db",null,3);
        final ArrayAdapter<String> adapter = new ArrayAdapter(
                FindActivity.this,android.R.layout.simple_list_item_1,list);
        listView=findViewById(R.id.list_item1);
        listView.setAdapter(adapter);
        //点击事件-更改
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i=new Intent(FindActivity.this,UpdateActivity.class);
                i.putExtra("id",String.valueOf(m.get(position)));
                startActivity(i);//启动第二个activity并把i传递过去
            }
        });
        //按钮-查询
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i1=0,i2=0;
                String title,time,note,note1;
                // 用户
                SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
                String author=sp.getString("loginUserName",null);

                 DatabaseHelper dbHelper = new DatabaseHelper(FindActivity.this, "Dairy.db",null,3);
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = db.query("dairy",null,"author=?",new String[]{author},null,null,null);
                //输入信息
                if (cursor.moveToFirst()){
                    list.clear();
                    m.clear();
                    do{//筛选出符合搜索的笔记
                        title=cursor.getString(cursor.getColumnIndex("title"));
                        note=cursor.getString(cursor.getColumnIndex("note"));
                        note1 = editText.getText().toString();//搜索信息
                        if(note.contains(note1)||title.contains(note1)){
                            time = cursor.getString(cursor.getColumnIndex("time"));
                            list.add(title+"\n"+time);
                            m.put(i2,i1);
                            i2++;
                        }
                        i1++;
                    }while(cursor.moveToNext());
                }
                cursor.close();
                adapter.notifyDataSetChanged();
                Toast.makeText(FindActivity.this,"查询完成",
                        Toast.LENGTH_SHORT).show();
            }
        });
        //按钮-返回
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(FindActivity.this,MainActivity.class);
                startActivity(intent1);
            }
        });
    }
}

