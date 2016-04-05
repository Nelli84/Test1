uniform mat4 u_projTrans;

attribute vec4 a_position;
attribute vec2 a_texCoord0;
attribute vec4 a_color;

varying vec4 v_color;
varying vec2 v_texCoord;
varying vec2 v_blurTexCoords[14];

void main() {
    gl_Position = u_projTrans * a_position;
    v_texCoord = a_texCoord0;
    v_color = a_color;
    
     /* HBlurVertexShader.glsl */
    v_blurTexCoords[ 0] = v_texCoord + vec2(-0.007, -0.007);
    v_blurTexCoords[ 1] = v_texCoord + vec2(-0.006, -0.006);
    v_blurTexCoords[ 2] = v_texCoord + vec2(-0.005, -0.005);
    v_blurTexCoords[ 3] = v_texCoord + vec2(-0.004, -0.004);
    v_blurTexCoords[ 4] = v_texCoord + vec2(-0.003, -0.003);
    v_blurTexCoords[ 5] = v_texCoord + vec2(-0.002, -0.002);
    v_blurTexCoords[ 6] = v_texCoord + vec2(-0.001, -0.001);
    v_blurTexCoords[ 7] = v_texCoord + vec2( 0.001, 0.001);
    v_blurTexCoords[ 8] = v_texCoord + vec2( 0.002, 0.002);
    v_blurTexCoords[ 9] = v_texCoord + vec2( 0.003, 0.003);
    v_blurTexCoords[10] = v_texCoord + vec2( 0.004, 0.004);
    v_blurTexCoords[11] = v_texCoord + vec2( 0.005, 0.005);
    v_blurTexCoords[12] = v_texCoord + vec2( 0.006, 0.006);
    v_blurTexCoords[13] = v_texCoord + vec2( 0.007, 0.007);
    
    /* HBlurVertexShader.glsl */
  /*  v_blurTexCoords[ 0] = v_texCoord + vec2(-0.028, 0.0);
    v_blurTexCoords[ 1] = v_texCoord + vec2(-0.024, 0.0);
    v_blurTexCoords[ 2] = v_texCoord + vec2(-0.020, 0.0);
    v_blurTexCoords[ 3] = v_texCoord + vec2(-0.016, 0.0);
    v_blurTexCoords[ 4] = v_texCoord + vec2(-0.012, 0.0);
    v_blurTexCoords[ 5] = v_texCoord + vec2(-0.008, 0.0);
    v_blurTexCoords[ 6] = v_texCoord + vec2(-0.004, 0.0);
    v_blurTexCoords[ 7] = v_texCoord + vec2( 0.004, 0.0);
    v_blurTexCoords[ 8] = v_texCoord + vec2( 0.008, 0.0);
    v_blurTexCoords[ 9] = v_texCoord + vec2( 0.012, 0.0);
    v_blurTexCoords[10] = v_texCoord + vec2( 0.016, 0.0);
    v_blurTexCoords[11] = v_texCoord + vec2( 0.020, 0.0);
    v_blurTexCoords[12] = v_texCoord + vec2( 0.024, 0.0);
    v_blurTexCoords[13] = v_texCoord + vec2( 0.028, 0.0); */
    
    /* VBlurVertexShader.glsl */
   /*  v_blurTexCoords[ 0] = v_texCoord + vec2(0.0, -0.028);
    v_blurTexCoords[ 1] = v_texCoord + vec2(0.0, -0.024);
    v_blurTexCoords[ 2] = v_texCoord + vec2(0.0, -0.020);
    v_blurTexCoords[ 3] = v_texCoord + vec2(0.0, -0.016);
    v_blurTexCoords[ 4] = v_texCoord + vec2(0.0, -0.012);
    v_blurTexCoords[ 5] = v_texCoord + vec2(0.0, -0.008);
    v_blurTexCoords[ 6] = v_texCoord + vec2(0.0, -0.004);
    v_blurTexCoords[ 7] = v_texCoord + vec2(0.0,  0.004);
    v_blurTexCoords[ 8] = v_texCoord + vec2(0.0,  0.008);
    v_blurTexCoords[ 9] = v_texCoord + vec2(0.0,  0.012);
    v_blurTexCoords[10] = v_texCoord + vec2(0.0,  0.016);
    v_blurTexCoords[11] = v_texCoord + vec2(0.0,  0.020);
    v_blurTexCoords[12] = v_texCoord + vec2(0.0,  0.024);
    v_blurTexCoords[13] = v_texCoord + vec2(0.0,  0.028); */
    
}