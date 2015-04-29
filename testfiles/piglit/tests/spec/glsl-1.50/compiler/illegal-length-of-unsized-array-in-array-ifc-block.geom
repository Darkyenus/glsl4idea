// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: true
// [end config]
//
// Test that if an interface block contains an unsized array, it is
// illegal to call .length() on it.
//
// This test uses an interface block array.

#version 150

layout(triangles) in;
layout(points, max_vertices = 1) out;

in block {
  float foo[];
} inst[];

out vec2 bar;

void main()
{
  bar = vec2(inst[0].foo[0], inst[0].foo.length());
  EmitVertex();
}
