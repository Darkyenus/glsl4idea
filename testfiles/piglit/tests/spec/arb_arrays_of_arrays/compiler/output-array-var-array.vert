/* [config]
 * expect_result: pass
 * glsl_version: 1.30
 * require_extensions: GL_ARB_arrays_of_arrays
 * [end config]
 */
#version 130
#extension GL_ARB_arrays_of_arrays: enable

out vec4[2] a[3];

void main()
{
  a = vec4[3][2](vec4[2](vec4(0.0), vec4(1.0)),
                 vec4[2](vec4(0.0), vec4(1.0)),
                 vec4[2](vec4(0.0), vec4(1.0)));

  gl_Position = a[0][0];
}
