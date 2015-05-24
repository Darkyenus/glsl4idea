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
  struct {
      vec4[2] a[3], b;
  } aggregate = {
      {
         {
           {1.0, 2.0, 3.0, 4.0}, // a[0][0]
           {5.0, 6.0, 7.0, 8.0}  // a[0][1]
         }, // a[0]
         {
           {1.0, 2.0, 3.0, 4.0}, // a[1][0]
           {5.0, 6.0, 7.0, 8.0}  // a[1][1]
         }, // a[1]
         {
           {1.0, 2.0, 3.0, 4.0}, // a[2][0]
           {5.0, 6.0, 7.0, 8.0}  // a[2][1]
         } // a[2]
      }, // a
      {
         { 1.0, 2.0, 3.0, 4.0 }, // b[0]
         { 1.0, 2.0, 3.0, 4.0 }  // b[1]
      }
  };

  gl_Position = aggregate.a[2][1];
}
