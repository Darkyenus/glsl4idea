// [config]
// expect_result: pass
// glsl_version: 1.50
// check_link: false
// [end config]

/*
* Section 4.3.4 (Inputs) of the GLSLLangSpec.1.50.09 4.3.4 Inputs says:
*
* Vertex shader inputs can only be float, floating-point
* vectors, matrices, signed and unsigned integers and integer vectors.
* Vertex shader inputs can also form arrays of these types, but not
* structures.
*
*/

#version 150

in uint  a[2];
in uvec2 b[2];
in uvec3 c[2];
in uvec4 d[2];

void main()
{
    gl_Position = vec4(a[0] + a[1]
			+ b[0].x + b[1].x
			+ c[0].x + c[1].x
			+ d[0].x + d[1].x);
}
