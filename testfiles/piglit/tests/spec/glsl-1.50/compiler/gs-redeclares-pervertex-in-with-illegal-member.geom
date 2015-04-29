// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: true
// [end config]
//
// From section 7.1 (Built-In Language Variables) of the GLSL 4.10
// spec:
//
//   The gl_PerVertex block can be redeclared in a shader to explicitly
//   indicate what subset of the fixed pipeline interface will be
//   used. This is necessary to establish the interface between multiple
//   programs.  For example:
//
//   out gl_PerVertex {
//       vec4 gl_Position;    // will use gl_Position
//       float gl_PointSize;  // will use gl_PointSize
//       vec4 t;              // error, only gl_PerVertex members allowed
//   };  // no other members of gl_PerVertex will be used
//
//   This establishes the output interface the shader will use with the
//   subsequent pipeline stage. It must be a subset of the built-in members
//   of gl_PerVertex.
//
// This test verifies that a non-member of the geometry shader
// gl_PerVertex input may not be included in the redeclaration.

#version 150

layout(triangles) in;
layout(triangle_strip, max_vertices = 3) out;

in gl_PerVertex {
    vec4 gl_Position;
    float gl_PointSize;
    vec4 t;
} gl_in[];

void main()
{
}
