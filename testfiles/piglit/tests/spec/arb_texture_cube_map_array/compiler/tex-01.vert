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
varying vec4 coord;
varying vec4 color;

void main()
{
  gl_Position = pos;
  color = texture(s, coord);
}
