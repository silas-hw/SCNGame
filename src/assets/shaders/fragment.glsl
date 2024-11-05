#version 330

#ifdef GL_ES
precision highp float;
#endif

in vec4 gl_FragCoord;

uniform sampler2D u_texture;

uniform float worldHeight;
uniform float viewportHeight;

uniform float worldWidth;
uniform float viewportWidth;

uniform float u_time;
uniform float u_amplitude = 0.005;
uniform float u_frequency = 200;
uniform float u_timeCoefficient = 10;

varying vec2 v_texCoords;

void main()
{
	vec2 texSize = textureSize(u_texture, 0);

	float gutterHeight = (viewportHeight - texSize.y)/2;

	float screen_y = v_texCoords.y * texSize.y;
	float viewport_y = screen_y + gutterHeight;

	float pix_y = trunc(viewport_y/(viewportHeight/worldHeight));

	float offsetX = u_amplitude * sin(pix_y * u_frequency + u_time*u_timeCoefficient);

	offsetX = (trunc((offsetX * viewportWidth)/(viewportWidth/worldWidth)))/worldWidth;

	vec2 texCoords = v_texCoords + vec2(offsetX, 0);

	gl_FragColor = texture2D(u_texture, texCoords);
}