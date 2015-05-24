// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL
 *
 * From page 17 (page 23 of the PDF) of the GLSL 1.20 spec:
 *
 *    "Samplers cannot be treated as l-values; hence cannot be used as
 *     out or inout function parameters, nor can they be assigned into."
 */
#version 120

uniform sampler2D u[4];
varying vec2 coord;

void main()
{
  sampler2D temp[4];
  temp = u;
  gl_FragColor = texture2D(temp[0], coord);
}
