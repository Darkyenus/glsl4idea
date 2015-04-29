// [config]
// expect_result: fail
// glsl_version: 1.10
// [end config]
//
// From section 7.2 of the GLSL 1.10 spec:
//     The variable gl_FragCoord is available as a read-only variable
//     from within fragment shaders...

#version 110

void g() {
    gl_FragCoord = vec4(0.0);
}
