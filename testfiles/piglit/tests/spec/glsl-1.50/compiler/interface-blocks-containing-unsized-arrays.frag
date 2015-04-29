// [config]
// expect_result: pass
// glsl_version: 1.50
// check_link: true
// [end config]
//
// Test that an interface block may contain members which are unsized
// arrays.  Both GLSL 1.10 and GLSL 1.20 style array declarations are
// tested.

#version 150

in block {
  float foo[];
  float[] bar;
} inst;

void main()
{
}
