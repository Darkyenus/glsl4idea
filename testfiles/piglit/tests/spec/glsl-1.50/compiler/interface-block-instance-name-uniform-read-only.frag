// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: false
// [end config]
//
// Check that an error is generated when trying
// to change the value of a uniform.

#version 150

uniform Block {
  vec4 invar;
} a;

void main()
{
  a.invar = vec4(1.0);
  gl_FragColor = a.invar;
}
