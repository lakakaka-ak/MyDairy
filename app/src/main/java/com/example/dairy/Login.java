package com.example.dairy;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    private Button btn_login;//登录按钮
    private TextView tv_Register;//注册
    private String userName,userPwd,spPwd;//获取用户名，密码
    private EditText et_userName,et_userPwd;//编辑框

    protected void onCreate(Bundle savedInstanceState) {//Activity的生命周期法
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //按钮
        btn_login = findViewById(R.id.btn_login);//登录
        tv_Register=findViewById(R.id.tv_Register);//注册
        et_userName=findViewById(R.id.et_userName);
        et_userPwd=findViewById(R.id.et_userPwd);

        //立即注册控件的点击事件
        tv_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,Register.class);
                startActivityForResult(intent, 1);

            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开始登录，获取用户名和密码
                userName=et_userName.getText().toString().trim();
                userPwd=et_userPwd.getText().toString().trim();

                //对当前用户输入的密码进行MD5加密再进行比对判断
                String md5Pwd= MD5.md5(userPwd);
                // md5Pwd ; spPsw 为 根据从SharedPreferences中用户名读取密码
                // 定义方法 readPsw为了读取用户名，得到密码
                spPwd=readPwd(userName);

                //用户名为空  提示
                if(TextUtils.isEmpty(userName)){
                    Toast.makeText(Login.this,"请输入用户名",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(TextUtils.isEmpty(userPwd)){
                    Toast.makeText(Login.this,"请输入密码",Toast.LENGTH_SHORT).show();
                    return;

                }
                // md5Psw.equals(); 判断，输入的密码加密后，是否与保存在SharedPreferences中一致
                else if(md5Pwd.equals(spPwd)){
                    //一致登录成功
                    Toast.makeText(Login.this, "登录成功", Toast.LENGTH_SHORT).show();
                    //保存登录状态，在界面保存登录的用户名 定义个方法 saveLoginStatus boolean 状态 , userName 用户名;
                    saveLoginStatus(true, userName);
                    //登录成功后关闭此页面进入主页
                    Intent data=new Intent();
                    //传值
                    data.putExtra("isLogin",true);
                    //RESULT_OK为Activity系统常量，状态码为-1
                    // 表示此页面下的内容操作成功将data返回到上一页面
                    setResult(RESULT_OK,data);
                    Login.this.finish(); //销毁登录界面
                    //跳转到主界面，登录成功的状态传递到 MainActivity 中
                    startActivity(new Intent(Login.this, MainActivity.class));
                    return;
                }
                //密码不一致
                else if((spPwd!=null&&!TextUtils.isEmpty(spPwd)&&!md5Pwd.equals(spPwd))){
                    Toast.makeText(Login.this, "输入的用户名和密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Toast.makeText(Login.this, "此用户名不存在", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     *从SharedPreferences中根据用户名读取密码
     */
    private String readPwd(String userName){
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        return sp.getString(userName , "");
    }

    /**
     *保存登录状态和登录用户名到SharedPreferences中
     */
    private void saveLoginStatus(boolean status,String userName){
        //saveLoginStatus(true, userName);
        //loginInfo表示文件名  SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        //获取编辑器
        SharedPreferences.Editor editor=sp.edit();
        //存入boolean类型的登录状态
        editor.putBoolean("isLogin", status);
        //存入登录状态时的用户名
        editor.putString("loginUserName", userName);
        //提交修改
        editor.commit();
    }
    /**
     * 注册成功的数据返回至此
     */
    @Override
    //显示数据， onActivityResult
    //startActivityForResult(intent, 1); 从注册界面中获取数据
    //int requestCode , int resultCode , Intent data
    // Login -> startActivityForResult -> onActivityResult();
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            //是获取注册界面回传过来的用户名
            String userName=data.getStringExtra("userName");
            if(!TextUtils.isEmpty(userName)){
                //设置用户名到 et_userName 控件
                et_userName.setText(userName);
                et_userName.setSelection(userName.length());
            }
        }
        String userName=data.getStringExtra("userName");
        et_userName.setText(userName);
        et_userName.setSelection(userName.length());
    }
}