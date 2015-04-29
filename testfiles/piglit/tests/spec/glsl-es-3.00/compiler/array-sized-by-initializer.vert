#version 300 es

/* [config]
 * expect_result: pass
 * glsl_version: 3.00
 * [end config]
 *
 * Page 28 (page 34 of the PDF) of the OpenGL ES Shading Language 3.00
 * spec says:
 *
 *     "An array type can also be formed without specifying a size if
 *     the definition includes an initializer:
 *
 *         float x[] = float[2] (1.0, 2.0);
 *         // declares an array of size 2
 *         float y[] = float[] (1.0, 2.0, 3.0); // declares an array of size 3
 *         float a[5];
 *         float b[] = a;"
 */

void main()
{
  float x[] = float[2] (1.0, 2.0);
  // declares an array of size 2
  float y[] = float[] (1.0, 2.0, 3.0); // declares an array of size 3
  float a[5];
  float b[] = a;
  float c[5] = b; // verify that b got the correct size

  gl_Position = vec4(0.);
}
