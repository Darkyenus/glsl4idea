/* [config]
 * expect_result: fail
 * glsl_version: 3.00 es
 * [end config]
 *
 * The GLSL ES 3.00 spec says:
 *
 * "interface-block:
 *     layout-qualifieropt uniform block-name { member-list } instance-nameopt ;
 *
 * ...
 *
 * instance-name:
 *     identifier
 *     identifier [ constant-integral-expression ]"
 *
 * If an interface name is not specified, it is impossible to specify an array
 * size.
 */
#version 300 es

uniform transform_data {
  mat4 mvp;
  mat3 mv_for_normals;
} [2];

in vec4 position;
in vec3 normal;

// normal vector in camera space
out vec3 normal_cs;

void main()
{
  gl_Position = mvp * position;
  normal_cs = mv_for_normals * normal;
}
