/* [config]
 * expect_result: pass
 * glsl_version: 1.20
 * [end config]
 *
 * From page 20 (page 26 of the PDF) of the GLSL 1.20 spec:
 *
 *     "In some situations, an expression and its type will be implicitly
 *     converted to a different type. The following table shows all allowed
 *     implicit conversions:
 *
 *     Type of expression   Can be implicitly converted to
 *     int                  float
 *     ivec2                vec2
 *     ivec3                vec3
 *     ivec4                vec4
 *
 *     There are no implicit array or structure conversions. For example, an
 *     array of int cannot be implicitly converted to an array of float.  When
 *     an implicit conversion is done, it is not just a re-interpretation of
 *     the expression's value, but a conversion of that value to an equivalent
 *     value in the new type. For example, the integer value 5 will be
 *     converted to the floating-point value 5.0.  The conversions in the
 *     table above are done only as indicated by other sections of this
 *     specification."
 */
#version 120

uniform ivec2 a[2];

void main()
{
  vec2 b[2] = vec2[](a[0], a[1]);

  gl_Position = vec4(b[0].x, b[0].y, b[1].x, b[1].y);
}
