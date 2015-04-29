/* [config]
 * expect_result: pass
 * glsl_version: 1.20
 * [end config]
 *
 * From page 18 (page 24 of the PDF) of the GLSL 1.20 spec:
 *
 *     "Member declarators can contain arrays.  Such arrays must have a size
 *     specified, and the size must be an integral constant expression that's
 *     greater than zero (see Section 4.3.3 "Constant Expressions")."
 *
 * From page 19 (page 25 of the PDF) of the GLSL 1.20 spec:
 *
 *     "All basic types and structures can be formed into arrays."
 */
#version 120

struct s {
  float x[3];
  int y;
};

void main()
{
  s a[2];
  gl_Position = vec4(a.length() + a[0].x.length());
}
