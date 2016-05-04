precision mediump float;
uniform vec3 u_Color;

void main()
{
    gl_FragColor = vec4(u_Color.x, u_Color.y, u_Color.z, 1);
}