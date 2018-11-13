package com.example.yuan.draw3dcube;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Draw3DCubeActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//设置窗体为全屏模式
       getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


       //创建一个GLSurfaceView用于绘制表面
        GLSurfaceView GLview = new GLSurfaceView(this);

        //设置Renderer用于执行实际的绘制工作
        GLview.setRenderer(new CubeRenderer(this));

        //设置绘制模式为持续绘制
        GLview.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        setContentView(GLview);

    }
}
