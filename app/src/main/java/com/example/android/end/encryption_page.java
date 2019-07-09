package com.example.android.end;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class encryption_page extends AppCompatActivity {
    String pathselected,fileContents,savePath,fname;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encryption_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(Build.VERSION.SDK_INT>=21){
            getWindow().setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
//        TextView preview= (TextView) findViewById(R.id.preview);
//        preview.setMovementMethod(new ScrollingMovementMethod());

    }
    public void selectFile(View view)
    {
        //Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        //intent.putExtra("file",)
        startActivityForResult(intent,1);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        if(requestCode==1)
            if(resultCode== Activity.RESULT_OK)
            {

                //Uri is stored in uri
                try
                {
                    uri = data.getData();
                    Log.v("encrypt page ", "getting the uri = "+uri.toString());
                    pathselected=uri.getPath();//file path

                }
                catch (NullPointerException e)//removed file not found exception
                {
                    e.printStackTrace();
                    Log.e("Main Activity", "\n\n\n\nFile Not Found no \n null pointer.");
                    return;
                }
                // FileDescriptor fd = fileContents2.getFileDescriptor();//now fd could be used to read file

                show_path("Path = "+pathselected);
                displayContent();
            }
    }
    public void displayContent()
    {
        Log.v("main activity encrypt","encrypt called");
        try
        {
            readContent();
        }
        catch(IOException io)
        {
            io.printStackTrace();
        }
    }
    public void readContent()throws IOException
    {
        int i=0;
        InputStream fin = getContentResolver().openInputStream(uri);
        fileContents="";
        while((i=fin.read())!=-1){
            char ch= (char) i;
            fileContents += ch;
        }
        Log.v("main activity ","\n\n\n\nfile contents"+fileContents);
        if(TextUtils.isEmpty(fileContents))
        {
            TextView write=(TextView)findViewById(R.id.preview_or_write);
            write.setText("Preview");
        }
        show_preview(fileContents);
    }
    public void encrypt_it(View view) {
        int size =0;
        if(TextUtils.isEmpty(fileContents))
        {
            Log.v("encrypt","checking text box for \n\n\n\ncontents");
            EditText t= (EditText)findViewById(R.id.preview);
            fileContents= t.getText().toString();
            if(TextUtils.isEmpty(fileContents))
            {
                Toast toast = Toast.makeText(this,"Please select a file or Write a Text",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM,0,10);
                toast.show();
                return;
            }
            pathselected=fileContents.substring(0,10);
            size=fileContents.length();
        }
        size=fileContents.length();
        int size_img = (int) Math.sqrt(size) + 1;
        int[] image_array = new int[size_img * size_img];
        int i1;
        char[] fileCon_char = fileContents.toCharArray();
        for (i1 = 0; i1 < size; i1++)
            image_array[i1] = (int) fileCon_char[i1];
        for (; i1 < size_img * size_img; i1++)
            image_array[i1] = 255;
        Bitmap bm = Bitmap.createBitmap(image_array, size_img, size_img, Bitmap.Config.ARGB_8888);
        Bitmap mutbm = bm.copy(Bitmap.Config.ARGB_8888, true);
        Log.v("main activity ", "\n\n\nbitmap made");
        bm.recycle();
        int i = 0;
        for (int k = 0; k < size_img; k++) {
            for (int j = 0; j < size_img; j++) {
                mutbm.setPixel(j, k, Color.rgb(255, 255, image_array[i]));
                i++;
            }
        }
        String name_file = "";
        char[] path_char = pathselected.toCharArray();
        for (i = pathselected.length() - 5; i > 0; i--) {
            if (path_char[i] == '/')
                break;
            name_file = name_file + path_char[i];
        }
        String name ="";
        for (int h = name_file.length()-1; h >= 0; h--)
        {
            name=name + name_file.charAt(h);
        }
        Log.v("main activity","this \n\n\n "+"new name = "+name_file);
        saveImage(mutbm,name);
    }
    private void saveImage(Bitmap finalBitmap, String image_name) {

        String root = null;
        try {
            root = get_path();
            if(TextUtils.isEmpty(root))
            {
                root=Environment.getExternalStorageDirectory().toString()+"/BSecure/Encrypted";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        File myDir = new File(root);
        myDir.mkdirs();
        fname = image_name+ "-Image.png";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        Log.i("LOAD", root + fname);
        try {
            Log.v("save image",  " t \n\n\n "+"saving the file");
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast toast = Toast.makeText(this,"File Saved at "+root,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM,0,10);
        toast.show();
    }
    public String get_path() throws IOException
    {

        String path_l="",p;
        int i=0;
        p=Environment.getExternalStorageDirectory().toString()+"/BSecure/Data/e_path.txt";
        //Uri uri;
        //uri=Uri.fromFile(new File(p));
        //InputStream fin = getContentResolver().openInputStream(uri);
        File file = new File(p);
        InputStream fin = new FileInputStream(file);
        path_l="";
        while((i=fin.read())!=-1){
            char ch= (char) i;
            path_l += ch;
        }
        return path_l;
    }
    public void show_preview(String text)
    {
        TextView prev = (TextView)findViewById(R.id.preview);
        prev.setText(text);
    }
    public void show_path(String text)
    {
        TextView textview = (TextView)findViewById(R.id.show_path);
        textview.setText(text);
    }
}
