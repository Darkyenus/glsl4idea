// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS */
uniform vec4 a[3];

void main()
{
  gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}
