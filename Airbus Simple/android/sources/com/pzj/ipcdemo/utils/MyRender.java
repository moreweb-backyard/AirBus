package com.pzj.ipcdemo.utils;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import org.apache.commons.io.IOUtils;

/* loaded from: classes.dex */
public class MyRender implements GLSurfaceView.Renderer {
    int mHeight = 0;
    ByteBuffer mUByteBuffer = null;
    ByteBuffer mVByteBuffer = null;
    int mWidth = 0;
    ByteBuffer mYByteBuffer = null;
    FloatBuffer positionBuffer = null;
    int positionSlot = 0;
    int programHandle = 0;
    int texRangeSlot = 0;
    int[] texture = new int[3];
    int[] textureSlot = new int[3];
    int vertexShader = 0;
    int yuvFragmentShader = 0;
    byte[] yuvData = null;
    FloatBuffer textCoodBuffer = null;
    boolean bNeedSleep = true;
    final float[] textCoodBufferData = {0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f};
    final float[] positionBufferData = {-1.0f, 1.0f, 0.0f, 1.0f, -1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, -1.0f, 0.0f, 1.0f};

    public MyRender(GLSurfaceView gLSurfaceView) {
        gLSurfaceView.setEGLContextClientVersion(2);
    }

    public static int compileShader(String str, int i) {
        int iGlCreateShader = GLES20.glCreateShader(i);
        if (iGlCreateShader == 0) {
            return iGlCreateShader;
        }
        int[] iArr = new int[1];
        GLES20.glShaderSource(iGlCreateShader, str);
        GLES20.glCompileShader(iGlCreateShader);
        GLES20.glGetShaderiv(iGlCreateShader, 35713, iArr, 0);
        if (iArr[0] != 0) {
            return iGlCreateShader;
        }
        Log.e("compileShader", "compile shader err:" + GLES20.glGetProgramInfoLog(iGlCreateShader));
        GLES20.glDeleteShader(iGlCreateShader);
        return 0;
    }

    public long createShaders() {
        String str = (((((((((((((((((("uniform sampler2D Ytex;\nuniform sampler2D Utex;\n") + "uniform sampler2D Vtex;\n") + "precision mediump float;  \n") + "varying vec4 VaryingTexCoord0; \n") + "vec4 color;\n") + "void main()\n") + "{\n") + "float yuv0 = (texture2D(Ytex,VaryingTexCoord0.xy)).r;\n") + "float yuv1 = (texture2D(Utex,VaryingTexCoord0.xy)).r;\n") + "float yuv2 = (texture2D(Vtex,VaryingTexCoord0.xy)).r;\n") + IOUtils.LINE_SEPARATOR_UNIX) + "color.r = yuv0 + 1.4022 * yuv2 - 0.7011;\n") + "color.r = (color.r < 0.0) ? 0.0 : ((color.r > 1.0) ? 1.0 : color.r);\n") + "color.g = yuv0 - 0.3456 * yuv1 - 0.7145 * yuv2 + 0.53005;\n") + "color.g = (color.g < 0.0) ? 0.0 : ((color.g > 1.0) ? 1.0 : color.g);\n") + "color.b = yuv0 + 1.771 * yuv1 - 0.8855;\n") + "color.b = (color.b < 0.0) ? 0.0 : ((color.b > 1.0) ? 1.0 : color.b);\n") + "gl_FragColor = color;\n") + "}\n";
        int[] iArr = new int[1];
        int iCompileShader = compileShader((((((("uniform mat4 uMVPMatrix;   \nattribute vec4 vPosition;  \n") + "attribute vec4 myTexCoord; \n") + "varying vec4 VaryingTexCoord0; \n") + "void main(){               \n") + "VaryingTexCoord0 = myTexCoord; \n") + "gl_Position = vPosition; \n") + "}  \n", 35633);
        this.vertexShader = iCompileShader;
        if (iCompileShader == 0) {
            Log.e("createShaders", "failed when compileShader(vertex)");
        }
        int iCompileShader2 = compileShader(str, 35632);
        this.yuvFragmentShader = iCompileShader2;
        if (iCompileShader2 == 0) {
            Log.e("createShaders", "failed when compileShader(fragment)");
        }
        this.programHandle = GLES20.glCreateProgram();
        GLES20.glAttachShader(this.programHandle, this.vertexShader);
        GLES20.glAttachShader(this.programHandle, this.yuvFragmentShader);
        GLES20.glLinkProgram(this.programHandle);
        GLES20.glGetProgramiv(this.programHandle, 35714, iArr, 0);
        if (iArr[0] == 0) {
            Log.e("createShaders", "link program err:" + GLES20.glGetProgramInfoLog(this.programHandle));
            destroyShaders();
        }
        this.texRangeSlot = GLES20.glGetAttribLocation(this.programHandle, "myTexCoord");
        this.textureSlot[0] = GLES20.glGetUniformLocation(this.programHandle, "Ytex");
        this.textureSlot[1] = GLES20.glGetUniformLocation(this.programHandle, "Utex");
        this.textureSlot[2] = GLES20.glGetUniformLocation(this.programHandle, "Vtex");
        this.positionSlot = GLES20.glGetAttribLocation(this.programHandle, "vPosition");
        Log.d("aaaaa", "texRangeSlot: " + this.texRangeSlot);
        Log.d("aaaaa", "positionSlot: " + this.positionSlot);
        Log.d("aaaaa", "textureSlot[0]: " + this.textureSlot[0]);
        Log.d("aaaaa", "textureSlot[1]: " + this.textureSlot[1]);
        Log.d("aaaaa", "textureSlot[2]: " + this.textureSlot[2]);
        return 0L;
    }

    public long destroyShaders() {
        if (this.programHandle != 0) {
            GLES20.glDetachShader(this.programHandle, this.yuvFragmentShader);
            GLES20.glDetachShader(this.programHandle, this.vertexShader);
            GLES20.glDeleteProgram(this.programHandle);
            this.programHandle = 0;
        }
        if (this.yuvFragmentShader != 0) {
            GLES20.glDeleteShader(this.yuvFragmentShader);
            this.yuvFragmentShader = 0;
        }
        if (this.vertexShader == 0) {
            return 0L;
        }
        GLES20.glDeleteShader(this.vertexShader);
        this.vertexShader = 0;
        return 0L;
    }

    public int draw(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, ByteBuffer byteBuffer3, int i, int i2) {
        GLES20.glClear(16384);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glUseProgram(this.programHandle);
        byteBuffer.position(0);
        GLES20.glActiveTexture(33984);
        loadTexture(this.texture[0], i, i2, byteBuffer);
        byteBuffer2.position(0);
        GLES20.glActiveTexture(33985);
        int i3 = i >> 1;
        int i4 = i2 >> 1;
        loadTexture(this.texture[1], i3, i4, byteBuffer2);
        byteBuffer3.position(0);
        GLES20.glActiveTexture(33986);
        loadTexture(this.texture[2], i3, i4, byteBuffer3);
        GLES20.glUniform1i(this.textureSlot[0], 0);
        GLES20.glUniform1i(this.textureSlot[1], 1);
        GLES20.glUniform1i(this.textureSlot[2], 2);
        this.positionBuffer.position(0);
        GLES20.glEnableVertexAttribArray(this.positionSlot);
        GLES20.glVertexAttribPointer(this.positionSlot, 4, 5126, false, 0, (Buffer) this.positionBuffer);
        this.textCoodBuffer.position(0);
        GLES20.glEnableVertexAttribArray(this.texRangeSlot);
        GLES20.glVertexAttribPointer(this.texRangeSlot, 4, 5126, false, 0, (Buffer) this.textCoodBuffer);
        GLES20.glDrawArrays(5, 0, 4);
        GLES20.glDisableVertexAttribArray(this.positionSlot);
        GLES20.glDisableVertexAttribArray(this.texRangeSlot);
        return 0;
    }

    public int loadTexture(int i, int i2, int i3, Buffer buffer) {
        GLES20.glBindTexture(3553, i);
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLES20.glTexParameteri(3553, 10240, 9729);
        GLES20.glTexParameteri(3553, 10242, 33071);
        GLES20.glTexParameteri(3553, 10243, 33071);
        GLES20.glTexImage2D(3553, 0, 6409, i2, i3, 0, 6409, 5121, buffer);
        return 0;
    }

    public int loadVBOs() {
        this.textCoodBuffer = ByteBuffer.allocateDirect(this.textCoodBufferData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.textCoodBuffer.put(this.textCoodBufferData).position(0);
        this.positionBuffer = ByteBuffer.allocateDirect(this.positionBufferData.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.positionBuffer.put(this.positionBufferData).position(0);
        return 0;
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(16384);
        synchronized (this) {
            if (this.mWidth != 0 && this.mHeight != 0 && this.mYByteBuffer != null && this.mUByteBuffer != null && this.mVByteBuffer != null) {
                if (this.bNeedSleep) {
                    try {
                        Thread.sleep(10L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                this.bNeedSleep = true;
                draw(this.mYByteBuffer, this.mUByteBuffer, this.mVByteBuffer, this.mWidth, this.mHeight);
            }
        }
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        GLES20.glViewport(0, 0, i, i2);
    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glGenTextures(3, this.texture, 0);
        createShaders();
        loadVBOs();
    }

    public int unloadVBOs() {
        if (this.positionBuffer == null) {
            return 0;
        }
        this.positionBuffer = null;
        return 0;
    }

    public int writeSample(byte[] bArr, int i, int i2, int i3) {
        synchronized (this) {
            if (i2 == 0 || i3 == 0) {
                Log.d("writesample", "invalid param");
                return 0;
            }
            if (i2 != this.mWidth || i3 != this.mHeight) {
                this.mWidth = i2;
                this.mHeight = i3;
                this.mYByteBuffer = ByteBuffer.allocate(this.mWidth * this.mHeight);
                this.mUByteBuffer = ByteBuffer.allocate((this.mWidth * this.mHeight) / 4);
                this.mVByteBuffer = ByteBuffer.allocate((this.mWidth * this.mHeight) / 4);
            }
            if (this.mYByteBuffer != null) {
                this.mYByteBuffer.position(0);
                this.mYByteBuffer.put(bArr, 0, this.mWidth * this.mHeight);
                this.mYByteBuffer.position(0);
            }
            if (this.mUByteBuffer != null) {
                this.mUByteBuffer.position(0);
                this.mUByteBuffer.put(bArr, this.mWidth * this.mHeight, (this.mWidth * this.mHeight) / 4);
                this.mUByteBuffer.position(0);
            }
            if (this.mVByteBuffer != null) {
                this.mVByteBuffer.position(0);
                this.mVByteBuffer.put(bArr, ((this.mWidth * this.mHeight) * 5) / 4, (this.mWidth * this.mHeight) / 4);
                this.mVByteBuffer.position(0);
            }
            this.bNeedSleep = false;
            return 1;
        }
    }

    public int writeSample(byte[] bArr, int i, int i2) {
        synchronized (this) {
            if (i == 0 || i2 == 0) {
                Log.d("writesample", "invalid param");
                return 0;
            }
            if (i != this.mWidth || i2 != this.mHeight) {
                this.mWidth = i;
                this.mHeight = i2;
                this.mYByteBuffer = ByteBuffer.allocate(this.mWidth * this.mHeight);
                this.mUByteBuffer = ByteBuffer.allocate((this.mWidth * this.mHeight) / 4);
                this.mVByteBuffer = ByteBuffer.allocate((this.mWidth * this.mHeight) / 4);
            }
            if (this.mYByteBuffer != null) {
                this.mYByteBuffer.position(0);
                this.mYByteBuffer.put(bArr, 0, this.mWidth * this.mHeight);
                this.mYByteBuffer.position(0);
            }
            if (this.mUByteBuffer != null) {
                this.mUByteBuffer.position(0);
                this.mUByteBuffer.put(bArr, this.mWidth * this.mHeight, (this.mWidth * this.mHeight) / 4);
                this.mUByteBuffer.position(0);
            }
            if (this.mVByteBuffer != null) {
                this.mVByteBuffer.position(0);
                this.mVByteBuffer.put(bArr, ((this.mWidth * this.mHeight) * 5) / 4, (this.mWidth * this.mHeight) / 4);
                this.mVByteBuffer.position(0);
            }
            this.bNeedSleep = false;
            return 1;
        }
    }
}
