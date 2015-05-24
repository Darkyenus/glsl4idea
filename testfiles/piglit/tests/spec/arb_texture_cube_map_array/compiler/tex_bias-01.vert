/* [config]
 * expect_result: fail
 * glsl_version: 1.30
 * require_extensions: GL_ARB_texture_cube_map_array
 * [end config]
 */
#version 130
#extension GL_ARB_texture_cube_map_array: require

uniform samplerCubeArray s;
attribute vec4 pos;
varying vec4 coord;
varying float bias;
varying vec4 color;

void main()
{
  gl_Position = pos;
  color = texture(s, coord, bias);
}
