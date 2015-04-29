// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: true
// [end config]
//
// Test that if an interface block contains an unsized array, it is
// illegal to access it using a non-constant index.
//
// This test uses a named interface block.

#version 150

in block {
  float foo[];
} inst;

uniform int bar;

void main()
{
  gl_FragColor = vec4(inst.foo[bar]);
}
