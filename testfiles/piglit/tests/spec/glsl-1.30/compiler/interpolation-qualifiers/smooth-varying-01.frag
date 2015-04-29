// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// 'smooth' cannot be applied to 'varying'.
//
// From section 4.3 of the GLSL 1.30 spec:
//     These interpolation qualifiers may only precede the qualifiers in,
//     centroid in, out, or centroid out in a declaration. They do not apply
//     to the deprecated storage qualifiers varying or centroid varying.

#version 130

smooth varying float x;

float f() {
	return 0.0;
}
