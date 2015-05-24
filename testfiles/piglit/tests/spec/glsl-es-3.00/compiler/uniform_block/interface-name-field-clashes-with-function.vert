/* [config]
 * expect_result: pass
 * glsl_version: 3.00 es
 * [end config]
 *
 * The GLSL ES 3.00 spec says:
 *
 *     "If an instance name (instance-name) is used, then it puts all the
 *     members inside a scope within its own name space..."
 */
#version 300 es

uniform transform_data {
  mat4 mvp;
  mat3 mv_for_normals;
} camera;

in vec4 position;
in vec3 normal;

// normal vector in camera space
out vec3 normal_cs;

// Since the uniform block has an instance name, its mvp field does
// not actually conflict with this function.
mat4 mvp(void)
{
  return mat4(1.);
}

void main()
{
  gl_Position = camera.mvp * position;
  normal_cs = camera.mv_for_normals * normal;
}
