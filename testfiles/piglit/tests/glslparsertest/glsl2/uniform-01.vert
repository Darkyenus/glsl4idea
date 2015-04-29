// [config]
// expect_result: pass
// glsl_version: 1.20
//
// [end config]

/* PASS */
#version 120
uniform vec2 a = vec2(1.0, 2.0);

void main()
{
  gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}
