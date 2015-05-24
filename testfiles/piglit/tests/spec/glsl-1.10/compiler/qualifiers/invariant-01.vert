// [config]
// expect_result: pass
// glsl_version: 1.10
// [end config]
//
// "invariant" is not a reserved word in GLSL 1.10
uniform vec4 invariant;

void main()
{
  gl_Position = invariant;
}
