#ifdef GL_ES
precision highp float;
#endif

uniform sampler2D u_texture; // 0
uniform vec2 center; // Mouse position
//uniform vec3 shockParams; // 10.0, 0.8, 0.1

varying vec2 v_texCoords;

void main()
{

	// pass through
	gl_FragColor = texture2D(u_texture, v_texCoords);
}