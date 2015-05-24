// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: true
// [end config]
//
// Test that if an interface block contains an unsized array, it is
// illegal to use it as the LHS of a bulk assignment (even if both the
// LHS and the RHS of the assignment have the same implied array
// size).
//
// This test uses an unnamed interface block.

#version 150

out block {
  float foo[];
};

void main()
{
  float bar[2];
  bar[0] = 1.0;
  bar[1] = 2.0;
  foo = bar;
  foo[1] = 3.0;
}
