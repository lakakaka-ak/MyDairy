package com.example.dairy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {
    private Button btn_register;//注册按钮
    //用户名，密码，再次输入的密码的控件
    private EditText user_name, user_pwd, user_pwd1;
    //用户名，密码，再次输入的密码的控件的获取值
    private String name, pwd, pwd1;

    protected void onCreate(Bundle savedInstanceState) {//Activity的生命周期法
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn_register=findViewById(R.id.btn_register);
        user_name=findViewById(R.id.user_name);
        user_pwd=findViewById(R.id.user_pwd);
        user_pwd1=findViewById(R.id.user_pwd1);

        //注册按钮
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入在相应控件中的字符串
                getEditString();
                //判断输入框内容
                if(TextUtils.isEmpty(name)){
                    Toast.makeText(Register.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(pwd)){
                    Toast.makeText(Register.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(pwd1)){
                    Toast.makeText(Register.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!pwd.equals(pwd1)){
                    Toast.makeText(Register.this, "输入两次的密码不一样", Toast.LENGTH_SHORT).show();
                    return;
                    /**
                     *从SharedPreferences中读取输入的用户名，判断SharedPreferences中是否有此用户名
                     */
                }else if(isExistUserName(name)){
                    Toast.makeText(Register.this, "此账户名已经存在", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
                    //把账号、密码保存到sp里面
                    /**
                     * 保存账号和密码到SharedPreferences中
                     */
                    saveRegisterInfo(name, pwd);
                    //注册成功后把账号传递到Login中
                    // 返回值到loginActivity显示
                    Intent data = new Intent();
                    data.putExtra("userName", name);
                    setResult(RESULT_OK, data);
                    //RESULT_OK为Activity系统常量，状态码为-1，
                    // 表示此页面下的内容操作成功将data返回到上一页面，如果是用back返回过去的则不存在用setResult传递data值
                    Register.this.finish();
                }
            }
        });
    }

    //获取控件中的字符串
    private void getEditString(){
        name=user_name.getText().toString().trim();
        pwd=user_pwd.getText().toString().trim();
        pwd1=user_pwd1.getText().toString().trim();
    }


    //从SharedPreferences中读取输入的用户名，判断SharedPreferences中是否有此用户名
    private boolean isExistUserName(String userName){
        boolean has_userName=false;
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        //获取密码
        String spPwd=sp.getString(userName, "");//传入用户名获取密码
        //如果密码不为空则确实保存过这个用户名
        if(!TextUtils.isEmpty(spPwd)) {
            has_userName=true;
        }
        return has_userName;
    }

    //保存账号和密码到SharedPreferences中SharedPreferences
    private void saveRegisterInfo(String userName,String userPwd){
        String md5Pwd = MD5.md5(userPwd);//把密码用MD5加密
        //loginInfo表示文件名, mode_private SharedPreferences sp = getSharedPreferences( );
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        //获取编辑器， SharedPreferences.Editor  editor -> sp.edit();
        SharedPreferences.Editor editor=sp.edit();
        //以用户名为key，密码为value保存在SharedPreferences中
        //key,value,如键值对，editor.putString(用户名，密码）;
        editor.putString(userName, md5Pwd);
        //提交修改 editor.commit();
        editor.commit();
    }
}


