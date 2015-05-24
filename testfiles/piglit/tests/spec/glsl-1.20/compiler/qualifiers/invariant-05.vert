// [config]
// expect_result: fail
// glsl_version: 1.20
// [end config]
//
// all uses of invariant-qualifier must be at global scope.
#version 120

varying vec4 x;

void main()
{
  invariant x;		/* redeclaration */
  gl_Position = vec4(0);
}

