// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: true
// [end config]
//
// From section 3.7 (Identifiers) of the GLSL 1.50 spec:
//
//     Identifiers starting with "gl_" are reserved for use by OpenGL
//     and may not be declared in a shader as either a variable or a
//     function.
//
// Consequently, an unnamed interface block may not contain a name
// starting with "gl_".

#version 150

out block {
    vec4 gl_ProsciuttoHoagie;
};

void main()
{
}
