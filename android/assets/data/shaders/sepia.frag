#ifdef GL_ES
precision mediump float;
precision mediump int;
#else
#define highp;
#endif


uniform sampler2D u_texture;

varying vec4 v_color;
varying vec2 v_texCoord;

const vec3 grayScaleMultiplier = vec3(0.299, 0.587, 0.114);
const vec3 sepia = vec3(1.2, 1.0, 0.8);

void main() {
	vec4 texColor = texture2D(u_texture, v_texCoord);
	vec3 gray = vec3(dot(texColor.rgb, grayScaleMultiplier));
	
	if(v_texCoord.t >= 0.5)
    gl_FragColor = vec4(gray * sepia, texColor.a);
    else
    gl_FragColor = vec4(gray , texColor.a);
   /* gl_FragColor = vec4(gray.r * sepia.r, gray.g * sepia.g, gray.b * sepia.b, texColor.a);*/
}