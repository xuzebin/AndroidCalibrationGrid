//uniform mat4 u_MVPMatrix;
attribute vec4 a_Position;
attribute vec2 a_TexCoordinate; // Per-vertex texture coordinate information we will pass in.

varying vec2 v_TexCoordinate;   // This will be passed into the fragment shader.
void main()
{
    v_TexCoordinate = a_TexCoordinate;
    //gl_Position = u_MVPMatrix * a_Position;
    gl_Position = a_Position;
}