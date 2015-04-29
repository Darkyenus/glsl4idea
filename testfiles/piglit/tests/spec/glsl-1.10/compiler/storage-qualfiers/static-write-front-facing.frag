// [config]
// expect_result: fail
// glsl_version: 1.10
// [end config]
//
// From section 7.2 of the GLSL 1.10 spec:
//     The fragment shader has access to the read-only built-in
//     variable gl_FrontFacing...

#version 110

void g() {
    gl_FrontFacing = true;
}
