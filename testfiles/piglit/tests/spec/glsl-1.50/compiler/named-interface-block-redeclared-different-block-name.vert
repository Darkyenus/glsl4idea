// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: false
// [end config]
//
// From section 4.1.9 (Arrays) of the GLSL 4.40 spec (as of revision 7):
//
//     However, unless noted otherwise, blocks cannot be redeclared;
//     an unsized array in a user-declared block cannot be sized
//     through redeclaration.
//
// The only place where the spec notes that interface blocks can be
// redeclared is to allow for redeclaration of built-in interface
// blocks such as gl_PerVertex.  Therefore, user-defined interface
// blocks can never be redeclared.  This is a clarification of
// previous intent (see Khronos bug 10659:
// https://cvs.khronos.org/bugzilla/show_bug.cgi?id=10659), so we test
// it for GLSL version 1.50.
//
// In this test, the named interface block is redeclared using a
// different block name from the name that it had previously.

#version 150

out block1 {
    vec4 a;
} inst;

out block2 {
    vec4 a;
} inst;

void main()
{
}
