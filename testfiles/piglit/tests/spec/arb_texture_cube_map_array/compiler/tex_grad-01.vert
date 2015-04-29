/* [config]
 * expect_result: pass
 * glsl_version: 1.30
 * require_extensions: GL_ARB_texture_cube_map_array
 * [end config]
 */
#version 130
#extension GL_ARB_texture_cube_map_array: require

uniform samplerCubeArray s;
attribute vec4 pos;
attribute vec4 coord;
attribute vec3 dPdx;
attribute vec3 dPdy;
varying vec4 color;

void main()
{
  gl_Position = pos;
  color = textureGrad(s, coord, dPdx, dPdy);
}
