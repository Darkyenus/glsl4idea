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

struct st {
  sampler2D t;
  int i;
};

uniform sampler2D u[4];
uniform int ui;
varying vec2 coord;

void function(out st s)
{
  s.i = ui;
}

void main()
{
  st s;

  function(s);
  gl_FragColor = texture2D(u[s.i], coord);
}
