/*
 * Copyright 2014 Google Inc. All Rights Reserved.

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.xuzebin.calibration;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Helper class to load and compile a shader.
 *
 * Created by xuzebin on 16/4/18.
 */
public class ShaderHelper {
    private static final String TAG = "ShaderHelper";

    /**
     * create/compile shaders and compile/link programs
     * @param context
     * @param vertShaderResId
     * @param fragShaderResId
     * @return the handle of program object
     */
    public static int initShadersAndProgram(Context context, int vertShaderResId, int fragShaderResId) {

        String vertshaderCode = readRawTextFile(context, vertShaderResId);
        String fragshaderCode = readRawTextFile(context, fragShaderResId);

        int vertshaderHandle = compileShader(GLES20.GL_VERTEX_SHADER, vertshaderCode);
        int fragshaderHandle = compileShader(GLES20.GL_FRAGMENT_SHADER, fragshaderCode);

        int programHandle = createAndLinkProgram(vertshaderHandle, fragshaderHandle);


        return programHandle;
    }


    /*
     * Read shader string from raw resources.
     *
     * @param context
     * @param resourceId
     * @return
     */
    public static String readRawTextFile(Context context, int resourceId) {
        InputStream inputStream = context.getResources().openRawResource(resourceId);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Create and compile a shader.
     *
     * @param shaderType   The shader type.
     * @param shaderCode The shader source code.
     * @return An OpenGL handle to the shader.
     */
    public static int compileShader(int shaderType, String shaderCode) {
        int shaderHandle = GLES20.glCreateShader(shaderType);

        if (shaderHandle != 0) {
            // Pass in the shader source.
            GLES20.glShaderSource(shaderHandle, shaderCode);

            // Compile the shader.
            GLES20.glCompileShader(shaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0) {
                Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shaderHandle));
                GLES20.glDeleteShader(shaderHandle);
                shaderHandle = 0;
            }
        }

        if (shaderHandle == 0) {
            throw new RuntimeException("Error creating shader.");
        }

        return shaderHandle;
    }

    /**
     * Helper function to compile and link a program.
     *
     * @param vertexShaderHandle   An OpenGL handle to an already-compiled vertex shader.
     * @param fragmentShaderHandle An OpenGL handle to an already-compiled fragment shader.
     * @return An OpenGL handle to the program.
     */
    public static int createAndLinkProgram(int vertexShaderHandle, int fragmentShaderHandle) {//, String[] attributes) {
        int programHandle = GLES20.glCreateProgram();

        if (programHandle != 0) {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(programHandle, vertexShaderHandle);

            // Bind the fragment shader to the program.
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);

            // Link the two shaders together into a program.
            GLES20.glLinkProgram(programHandle);

            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // If the link failed, delete the program.
            if (linkStatus[0] == 0) {
                Log.e(TAG, "Error compiling program: " + GLES20.glGetProgramInfoLog(programHandle));
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }

        if (programHandle == 0) {
            throw new RuntimeException("Error creating program.");
        }

        return programHandle;
    }
}
