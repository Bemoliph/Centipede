// Default Vertex Shader
// Thanks to Tenfour04 http://stackoverflow.com/questions/26132160/simulating-palette-swaps-with-opengl-shaders-in-libgdx/26141687#26141687

attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;

varying vec2 v_texCoords;
varying float paletteIndex;

void main() {
    paletteIndex = a_color.r;
    v_texCoords = a_texCoord0;
    gl_Position = u_projTrans * a_position;
}