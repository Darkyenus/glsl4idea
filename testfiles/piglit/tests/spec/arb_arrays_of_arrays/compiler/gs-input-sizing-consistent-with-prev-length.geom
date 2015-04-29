// Section 4.3.8.1 (Input Layout Qualifiers) of the GLSL 1.50 spec
// includes the following examples of compile-time errors:
//
//   // code sequence within one shader...
//   in vec4 Color1[];    // size unknown
//   ...Color1.length()...// illegal, length() unknown
//   in vec4 Color2[2];   // size is 2
//   ...Color1.length()...// illegal, Color1 still has no size
//   in vec4 Color3[3];   // illegal, input sizes are inconsistent
//   layout(lines) in;    // legal, input size is 2, matching (*)
//   in vec4 Color4[3];   // illegal, contradicts layout
//   ...Color1.length()...// legal, length() is 2, Color1 sized by layout()
//   layout(lines) in;    // legal, matches other layout() declaration
//   layout(triangles) in;// illegal, does not match earlier layout() declaration
//
// This test verifies the case marked with (*), namely that no error
// results from declaring a geometry shader input layout after
// declaraing geometry shader inputs that are either unsized or have a
// size consistent with the declared layout.
//
// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_arrays_of_arrays
// check_link: false
// [end config]

#version 150
#extension GL_ARB_arrays_of_arrays: enable

in vec4 Color1[][4];
in vec4 Color2[2][4];
layout(lines) in;
