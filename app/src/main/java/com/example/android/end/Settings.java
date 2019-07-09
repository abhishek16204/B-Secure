package com.example.android.end;

import android.content.Context;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

public class Settings extends AppCompatActivity {

    EditText encrypt_path,decrypt_path;
    String e_path,d_path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        encrypt_path=(EditText)findViewById(R.id.Encrypt_path);
        decrypt_path=(EditText)findViewById(R.id.Decrypt_path);

        try {
            show_default();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void encrypt_ok(View view)
    {

        e_path=encrypt_path.getText().toString();
        if(TextUtils.isEmpty(e_path))
        {
            Toast toast = Toast.makeText(this,"Enter a path for encrypt",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM,0,15);
            toast.show();
            return;
        }
        e_save(e_path);
        encrypt_path.setText("");
        show_hint_e(e_path);
        Toast toast = Toast.makeText(this,"Path Changed",Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM,0,15);
        toast.show();

    }
    public void decrypt_ok(View view)
    {
        d_path=decrypt_path.getText().toString();
        if(TextUtils.isEmpty(d_path))
        {
            Toast toast = Toast.makeText(this,"Enter a path for decrypt",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM,0,15);
            toast.show();
            return;
        }
        d_save(d_path);
        decrypt_path.setText("");
        show_hint_d(d_path);
        Toast toast = Toast.makeText(this,"Path Changed",Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM,0,15);
        toast.show();
    }
    public void reset(View view)
    {
        d_path=Environment.getExternalStorageDirectory().toString()+"/BSecure/Decrypted/";
        e_path=Environment.getExternalStorageDirectory().toString()+"/BSecure/Encrypted/";
        e_save(e_path);
        d_save(d_path);
        try {
            show_default();

        } catch (IOException e) {
            e.printStackTrace();
        }
        encrypt_path.setText("");
        decrypt_path.setText("");
        Toast toast = Toast.makeText(this,"Default saved",Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM,0,15);
        toast.show();
    }


    public void e_save(String content)
    {
        String root = Environment.getExternalStorageDirectory().toString()+"/BSecure/Data";
        File myDir = new File(root);
        myDir.mkdirs();
        String fname = "e_path.txt";

        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        Log.i("LOAD", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            //finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.write(content.getBytes());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void d_save(String content)
    {
        String root = Environment.getExternalStorageDirectory().toString()+"/BSecure/Data";

        File myDir = new File(root);
        myDir.mkdirs();
        String fname = "d_path.txt";

        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        Log.i("LOAD", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            //finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.write(content.getBytes());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void show_hint_e(String data)
    {
        encrypt_path.setHint(data);
    }
    public void show_hint_d(String data)
    {
        decrypt_path.setHint(data);
    }
    public void show_default() throws IOException
    {

        String hint_e="",p,hint_d;
        int i=0;
        p=Environment.getExternalStorageDirectory().toString()+"/BSecure/Data/e_path.txt";
        //Uri uri;
        //uri=Uri.fromFile(new File(p));
        //InputStream fin = getContentResolver().openInputStream(uri);
        File file = new File(p);
        InputStream fin = new FileInputStream(file);
        hint_e="";
        while((i=fin.read())!=-1){
            char ch= (char) i;
            hint_e += ch;
        }

        show_hint_e(hint_e);


        i=0;
        p=Environment.getExternalStorageDirectory().toString()+"/BSecure/Data/d_path.txt";
        //Uri uri;
        //uri=Uri.fromFile(new File(p));
        //InputStream fin = getContentResolver().openInputStream(uri);
        File file2 = new File(p);
        InputStream fin2 = new FileInputStream(file2);
        hint_d="";
        while((i=fin2.read())!=-1){
            char ch= (char) i;
            hint_d += ch;
        }

        show_hint_d(hint_d);
    }
}
