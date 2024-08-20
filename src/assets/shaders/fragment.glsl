#ifdef GL_ES
precision highp float;
#endif

in vec4 gl_FragCoord;

uniform sampler2D u_texture;

uniform float u_time;
uniform float u_amplitude = 0.001;
uniform float u_frequency = 300;
uniform float u_timeCoefficient = 10;

varying vec2 v_texCoords;

void main()
{
	vec2 texSize = textureSize(u_texture, 0);

	float offsetX = u_amplitude * sin(v_texCoords.y * u_frequency + u_time*u_timeCoefficient);

	vec2 texCoords = v_texCoords + vec2(offsetX, 0);

	gl_FragColor = texture2D(u_texture, texCoords);
}