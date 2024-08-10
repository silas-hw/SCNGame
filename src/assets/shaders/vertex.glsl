attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;

uniform float time;

uniform float yseed;
uniform float xseed;

varying vec4 v_color;
varying vec2 v_texCoords;

float rand(vec2 co){
    return fract(sin(dot(co, vec2(12.9898, 78.233))) * 43758.5453);
}

void main() {
    v_color = a_color;
    v_texCoords = a_texCoord0;

    vec2 seedx;

    seedx.x = xseed;
    seedx.y = xseed;

    vec2 seedy;
    seedy.x = yseed;
    seedy.y = yseed;

    a_position.x += rand(seedx);
    a_position.y += rand(seedy);

    gl_Position = u_projTrans * (a_position);

}
