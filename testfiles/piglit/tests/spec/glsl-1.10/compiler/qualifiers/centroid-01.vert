// [config]
// expect_result: pass
// glsl_version: 1.10
// [end config]
//
// "centroid" is not a reserved word in GLSL 1.10

uniform vec4 centroid;

void main()
{
  gl_Position = centroid;
}
