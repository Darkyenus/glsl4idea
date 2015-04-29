/* [config]
 * expect_result: pass
 * glsl_version: 1.20
 * require_extensions: GL_ARB_arrays_of_arrays
 * [end config]
 */
#version 120
#extension GL_ARB_arrays_of_arrays: enable

void main()
{
  vec4 a[3][2][2] = vec4[3][2][2](vec4[2][2](vec4[2](vec4(0.0), vec4(1.0)),
                                             vec4[2](vec4(0.0), vec4(1.0))),
                                  vec4[2][2](vec4[2](vec4(0.0), vec4(1.0)),
                                             vec4[2](vec4(0.0), vec4(1.0))),
                                  vec4[2][2](vec4[2](vec4(0.0), vec4(1.0)),
                                             vec4[2](vec4(0.0), vec4(1.0))));

  gl_Position = a[2][1][1];
}
