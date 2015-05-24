/* [config]
 * expect_result: pass
 * glsl_version: 3.00 es
 * [end config]
 */
#version 300 es

uniform transform_data {
  mat4 mvp;
  mat3 mv_for_normals;
} camera[2];

in vec4 position;
in vec3 normal;

// normal vector in camera space
out vec3 normal_cs;

void main()
{
  gl_Position = camera[0].mvp * position;
  normal_cs = camera[1].mv_for_normals * normal;
}
