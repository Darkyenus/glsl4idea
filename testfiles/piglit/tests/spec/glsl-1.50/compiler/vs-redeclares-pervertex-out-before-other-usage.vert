// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: true
// [end config]
//
// From section 7.1 (Built-In Language Variables) of the GLSL 4.10
// spec:
//
//     It is also a compilation error to redeclare a built-in block
//     and then use a member from that built- in block that was not
//     included in the redeclaration.
//
// This appears to be a clarification to the behaviour established for
// gl_PerVertex by GLSL 1.50, therefore we test it using GLSL version
// 1.50.
//
// In this test the variable that we attempt to use before redeclaring
// gl_PerVertex is not included in the redeclaration of gl_PerVertex.

#version 150

out gl_PerVertex {
    vec4 gl_Position;
};

void foo()
{
  gl_PointSize = 1.0;
}

void main()
{
}
