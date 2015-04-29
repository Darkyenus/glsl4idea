/* [config]
 * expect_result: pass
 * glsl_version: 1.00
 * require_extensions: GL_OES_standard_derivatives
 * [end config]
 */
#version 100
#extension GL_OES_standard_derivatives: require
precision mediump float;

varying vec3 x;
void main()
{
    gl_FragColor = dFdx(x).xyzx;
}
