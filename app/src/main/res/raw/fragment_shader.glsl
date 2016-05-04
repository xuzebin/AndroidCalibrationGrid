precision mediump float;
uniform vec3 u_Color;
vec2 pos;
void main()
{
    vec2 pos = mod(gl_FragCoord.xy, vec2(120));


    float gridWidth = 60.0;
    if ((pos.x > gridWidth && pos.y > gridWidth) || (pos.x < gridWidth && pos.y < gridWidth)) {
        gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);
    }
    if ((pos.x < gridWidth && pos.y > gridWidth) || (pos.x > gridWidth && pos.y < gridWidth)) {
        gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0);
    }
    //gl_FragColor = vec4(u_Color.x, u_Color.y, u_Color.z, 1);
}