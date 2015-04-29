// [config]
// expect_result: fail
// glsl_version: 1.10
// [end config]
//
// Check that a bool can't be used as a varying in GLSL 1.10.
//
// From section 4.3.6 ("Varying") of the GLSL 1.10 spec:
//     The varying qualifier can be used only with the data types
//     float, vec2, vec3, vec4, mat2, mat3, and mat4, or arrays of
//     these. Structures cannot be varying.

#version 110

varying bool foo;

void main()
{
  gl_FragColor = vec4(foo, 0.0, 0.0, 0.0);
}
