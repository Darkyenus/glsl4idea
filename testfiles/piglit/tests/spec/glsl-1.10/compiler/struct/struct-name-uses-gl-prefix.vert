// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: true
// [end config]
//
// From section 3.7 (Identifiers) of the GLSL 1.10 spec:
//
//     Identifiers starting with "gl_" are reserved for use by OpenGL
//     and may not be declared in a shader as either a variable or a
//     function.
//
// Consequently, a struct's name may not start with "gl_".

struct gl_ProsciuttoHoagie {
    vec4 a;
};

void main()
{
  gl_Position = vec4(0.0);
}
