// Section 4.3.8.1 (Input Layout Qualifiers) of the GLSL 1.50 spec
// includes the following examples of compile-time errors:
//
//   // code sequence within one shader...
//   in vec4 Color1[];    // size unknown
//   ...Color1.length()...// illegal, length() unknown (*)
//   in vec4 Color2[2];   // size is 2
//   ...Color1.length()...// illegal, Color1 still has no size
//   in vec4 Color3[3];   // illegal, input sizes are inconsistent
//   layout(lines) in;    // legal, input size is 2, matching
//   in vec4 Color4[3];   // illegal, contradicts layout
//   ...Color1.length()...// legal, length() is 2, Color1 sized by layout()
//   layout(lines) in;    // legal, matches other layout() declaration
//   layout(triangles) in;// illegal, does not match earlier layout() declaration
//
// This test verifies the case marked with (*), namely that it is
// illegal to call .length() on an unsized geometry shader input
// array, even if an input layout declaration occurs later in the
// shader.
//
// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: false
// [end config]

#version 150

in vec4 Color1[];

int foo()
{
  return Color1.length();
}

layout(lines) in;
