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
  float x[][2] = float[][2](float[2](1., 2.),
                            float[2](1., 2.),
                            float[2](1., 2.));

  gl_Position = vec4(x[0][0]);
}
