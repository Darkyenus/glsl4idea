// [config]
// expect_result: fail
// glsl_version: 1.20
// [end config]
//
// invariant-qualifier should not be allowed on struct members.
#version 120

struct x {
	invariant vec4 y;
};

void main()
{
  gl_Position = vec4(0);
}
