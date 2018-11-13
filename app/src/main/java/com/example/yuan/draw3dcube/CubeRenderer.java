package com.example.yuan.draw3dcube;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class CubeRenderer implements GLSurfaceView.Renderer {


    private  ByteBuffer vertices;
    private ByteBuffer triangles;
    private  ByteBuffer colors;

    private float anglex = 0f;
    private  float angley = 0f;
    private  float anglez = 0f;

    private float[] data_vertices={
            1, 1, 1,
            1, -1, 1,
            -1, -1, 1,
            -1, 1, 1,
            1, 1, -1,
            1, -1, -1,
            -1, -1, -1,
            -1, 1, -1,
    };

    private  float[] data_colors = {
            1, 1, 0, 1,
            1, 0, 1, 1,
            0, 1, 1, 1,
            1, 0, 0, 1,
            0, 0, 0, 1,
            0, 0, 1, 1,
            0, 1, 0, 1,
            1, 1, 1, 1,
    };

    private byte[] data_triangles = {
            0, 1, 2,
            0, 2, 3,
            0, 3, 7,
            0, 7, 4,
            0, 4, 5,
            0, 5, 1,
            6, 5, 4,
            6, 4, 7,
            6, 7, 3,
            6, 3, 2,
            6, 2, 1,
            6, 1, 5
    };
    public CubeRenderer(Draw3DCubeActivity main){
        createBuffers();
    }

    private void createBuffers() {
        // 创建顶点缓冲，顶点数组使用 float 类型，每个 float 长4个字节
        vertices = ByteBuffer.allocateDirect(data_vertices.length * 4);
        // 设置字节顺序为本机顺序
        vertices.order(ByteOrder.nativeOrder());
        // 通过一个 FloatBuffer 适配器，将 float 数组写入 ByteBuffer 中
        vertices.asFloatBuffer().put(data_vertices);
        // 重置Buffer的当前位置
        vertices.position(0);

        //创建颜色缓冲
        colors = ByteBuffer.allocateDirect(data_colors.length * 4);
        colors.order(ByteOrder.nativeOrder());
        colors.asFloatBuffer().put(data_colors);
        colors.position(0);

        // 创建三角形顶点索引缓冲，索引使用byte类型，所以无需设置字节顺序，也无需写入适配。
        triangles = ByteBuffer.allocateDirect(data_triangles.length * 2);
        triangles.put(data_triangles);
        triangles.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glDisable(GL10.GL_DITHER); // 颜色抖动据说可能严重影响性能，禁用
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);// 设置清除颜色缓冲区时用的RGBA颜色值

        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glClearDepthf(1f);
    }

    /**
     *
     * 当GLSurfaceView大小改变时，对应的Surface大小也会改变
     */

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // 宽高比
        float aspect = (float) width / (float) (height == 0 ? 1 : height);

        // 设置视口
        gl.glViewport(0, 0, width, height);

        // 设置当前矩阵堆栈为投影矩阵，并将矩阵重置为单位矩阵
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();

        GLU.gluPerspective(gl, 45.0f, aspect, 0.1f, 200.0f);
        GLU.gluLookAt(gl, 5f, 5f, 5f, 0f, 0f, 0f, 0, 1, 0);
    }

    /**
     *
     * 画图
     * @param gl
     */
        @Override
        public void onDrawFrame(GL10 gl) {
            // 清除颜色缓冲
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

            // 设置当前矩阵堆栈为模型堆栈，并重置堆栈，
            // 即随后的矩阵操作将应用到要绘制的模型上
            gl.glMatrixMode(GL10.GL_MODELVIEW);

            gl.glLoadIdentity();

            gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, new float[]{5, 5, 5, 1}, 0);

            // 将旋转矩阵应用到当前矩阵堆栈上，即旋转模型
            gl.glRotatef(anglez, 0, 0, 1);
            gl.glRotatef(angley, 0, 1, 0);
            gl.glRotatef(anglex, 1, 0, 0);
            anglex += 0.1; // 递增角度值以便每次以不同角度绘制
            angley += 0.2;
            anglez += 0.3;

            // 启用顶点数组、法向量、颜色数组
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

            // 设置顶点数组指针为 ByteBuffer 对象 vertices
            // 第一个参数为每个顶点包含的数据长度（以第二个参数表示的数据类型为单位）
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertices);
            gl.glColorPointer(4, GL10.GL_FLOAT, 0, colors);

            // 绘制 triangles 表示的三角形
            gl.glDrawElements(GL10.GL_TRIANGLES, triangles.remaining(), GL10.GL_UNSIGNED_BYTE, triangles);

            // 禁用顶点、法向量、颜色数组
            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        }
}
