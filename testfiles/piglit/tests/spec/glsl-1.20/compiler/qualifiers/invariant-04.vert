// [config]
// expect_result: fail
// glsl_version: 1.20
// [end config]
//
// all uses of invariant-qualifier must be at global scope.
#version 120

void main()
{
  invariant vec4 x;
  gl_Position = vec4(0);
}
