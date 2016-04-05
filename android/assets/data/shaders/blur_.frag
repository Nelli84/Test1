#ifdef GL_ES
precision mediump float;
precision mediump int;
#else
#define highp;
#endif


varying vec4 v_color;
varying vec2 v_texCoord;

uniform sampler2D u_texture;
uniform float resolution;
uniform float radius;
uniform vec2 dir;

void main() {
	vec4 sum = vec4(0.0);
	vec2 tc = v_texCoord;
	
	// Number of pixels off the central pixel to sample from
	float blur = radius/resolution; 
    
	// Blur direction
	float hstep = dir.x;
	float vstep = dir.y;
    
	// Apply blur using 9 samples and predefined gaussian weights
	sum += texture2D(u_texture, vec2(tc.x - 3.0*blur*hstep, tc.y - 7.0*blur*vstep)) * 0.0044299121055113265;
	sum += texture2D(u_texture, vec2(tc.x - 2.0*blur*hstep, tc.y - 6.0*blur*vstep)) * 0.00895781211794;
	sum += texture2D(u_texture, vec2(tc.x - 1.0*blur*hstep, tc.y - 5.0*blur*vstep)) * 0.0215963866053;
	sum += texture2D(u_texture, vec2(tc.x - 4.0*blur*hstep, tc.y - 4.0*blur*vstep)) * 0.0443683338718;
	sum += texture2D(u_texture, vec2(tc.x - 3.0*blur*hstep, tc.y - 3.0*blur*vstep)) * 0.0776744219933;
	sum += texture2D(u_texture, vec2(tc.x - 2.0*blur*hstep, tc.y - 2.0*blur*vstep)) * 0.115876621105;
	sum += texture2D(u_texture, vec2(tc.x - 1.0*blur*hstep, tc.y - 1.0*blur*vstep)) * 0.147308056121;
	
	sum += texture2D(u_texture, vec2(tc.x, tc.y)) * 0.159576912161;
	
	sum += texture2D(u_texture, vec2(tc.x + 1.0*blur*hstep, tc.y + 1.0*blur*vstep)) * 0.147308056121;
	sum += texture2D(u_texture, vec2(tc.x + 2.0*blur*hstep, tc.y + 2.0*blur*vstep)) * 0.115876621105;
	sum += texture2D(u_texture, vec2(tc.x + 3.0*blur*hstep, tc.y + 3.0*blur*vstep)) * 0.0776744219933;
	sum += texture2D(u_texture, vec2(tc.x + 4.0*blur*hstep, tc.y + 4.0*blur*vstep)) * 0.0443683338718;
	sum += texture2D(u_texture, vec2(tc.x - 3.0*blur*hstep, tc.y - 5.0*blur*vstep)) * 0.0215963866053;
	sum += texture2D(u_texture, vec2(tc.x - 2.0*blur*hstep, tc.y - 6.0*blur*vstep)) * 0.00895781211794;
	sum += texture2D(u_texture, vec2(tc.x - 1.0*blur*hstep, tc.y - 7.0*blur*vstep)) * 0.0044299121055113265;
	
	gl_FragColor = sum;
}