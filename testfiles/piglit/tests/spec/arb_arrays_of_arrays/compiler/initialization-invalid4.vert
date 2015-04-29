/* [config]
 * expect_result: fail
 * glsl_version: 1.20
 * require_extensions: GL_ARB_arrays_of_arrays GL_ARB_shading_language_420pack
 * [end config]
 */
#version 120
#extension GL_ARB_arrays_of_arrays: enable
#extension GL_ARB_shading_language_420pack: enable

void main()
{
  vec4 a[2][2] = {vec4[2](vec4(0.0), vec4(1.0)),
                  vec4[2](vec4(0.0), vec4(1.0)),
                  vec4[2](vec4(0.0), vec4(1.0)) };

  gl_Position = a[2][1];
}
