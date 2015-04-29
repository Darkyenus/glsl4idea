/* [config]
 * expect_result: pass
 * glsl_version: 1.20
 * require_extensions: GL_ARB_arrays_of_arrays GL_ARB_shading_language_420pack
 * [end config]
 */
#version 120
#extension GL_ARB_arrays_of_arrays: enable
#extension GL_ARB_shading_language_420pack: enable

void main()
{
  vec4 a[3][2] = {
      {
        vec4(1.0), // a[0][0]
        vec4(1.0)  // a[0][1]
      },
      {
        vec4(1.0), // a[1][0]
        vec4(1.0)  // a[1][1]
      },
      {
        vec4(1.0), // a[2][0]
        vec4(1.0)  // a[2][1]
      }
    }, b[3][2] = {
      {
        vec4(1.0), // b[0][0]
        vec4(1.0)  // b[0][1]
      },
      {
        vec4(1.0), // b[1][0]
        vec4(1.0)  // b[1][1]
      },
      {
        vec4(1.0), // b[2][0]
        vec4(1.0)  // b[2][1]
      }
    }; // b

  gl_Position = b[2][1];
}
