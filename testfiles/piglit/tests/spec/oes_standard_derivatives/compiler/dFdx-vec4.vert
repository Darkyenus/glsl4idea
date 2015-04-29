/* [config]
 * expect_result: fail
 * glsl_version: 1.00
 * require_extensions: GL_OES_standard_derivatives
 * [end config]
 */
#version 100
#extension GL_OES_standard_derivatives: require

attribute vec4 x;
void main()
{
    gl_Position = dFdx(x);
}
