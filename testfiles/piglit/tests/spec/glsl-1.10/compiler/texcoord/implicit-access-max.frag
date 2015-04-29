/* [config]
 * expect_result: fail
 * glsl_version: 1.10
 * check_link: true
 * [end config]
 *
 * From the GLSL 1.10 spec section 7.6 (Varying Variables):
 *
 *   As with all arrays, indices used to subscript gl_TexCoord must
 *   either be an integral constant expressions, or this array must be
 *   re-declared by the shader with a size. The size can be at most
 *   gl_MaxTextureCoords.
 *
 * This implies that when gl_TexCoord is implicitly sized, it must not
 * be accessed with integral constant expressions larger than (or
 * equal to) gl_MaxTextureCoords.
 *
 * This test checks that the an error occurs when the size of
 * gl_TexCoord is implicit, and we try to access a non-existent
 * element (gl_TexCoord[gl_MaxTextureCoords]) using an integral
 * constant expression.
 */
void main()
{
  gl_FragColor = gl_TexCoord[gl_MaxTextureCoords];
}
