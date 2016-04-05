#ifdef GL_ES
precision mediump float;
precision mediump int;
#else
#define highp;
#endif


varying vec4 v_color;
varying vec2 v_texCoord;
varying vec2 v_blurTexCoords[14];

uniform sampler2D u_texture;

void main() {
	
	vec4 sum = vec4(0.0);
    sum += texture2D(u_texture, v_blurTexCoords[ 0])*0.0044299121055113265;
    sum += texture2D(u_texture, v_blurTexCoords[ 1])*0.00895781211794;
    sum += texture2D(u_texture, v_blurTexCoords[ 2])*0.0215963866053;
    sum += texture2D(u_texture, v_blurTexCoords[ 3])*0.0443683338718;
    sum += texture2D(u_texture, v_blurTexCoords[ 4])*0.0776744219933;
    sum += texture2D(u_texture, v_blurTexCoords[ 5])*0.115876621105;
    sum += texture2D(u_texture, v_blurTexCoords[ 6])*0.147308056121;
    sum += texture2D(u_texture, v_texCoord         )*0.159576912161;
    sum += texture2D(u_texture, v_blurTexCoords[ 7])*0.147308056121;
    sum += texture2D(u_texture, v_blurTexCoords[ 8])*0.115876621105;
    sum += texture2D(u_texture, v_blurTexCoords[ 9])*0.0776744219933;
    sum += texture2D(u_texture, v_blurTexCoords[10])*0.0443683338718;
    sum += texture2D(u_texture, v_blurTexCoords[11])*0.0215963866053;
    sum += texture2D(u_texture, v_blurTexCoords[12])*0.00895781211794;
    sum += texture2D(u_texture, v_blurTexCoords[13])*0.0044299121055113265;
    
    gl_FragColor = sum;
	
}