// [config]
// expect_result: pass
// glsl_version: 1.50
// check_link: false
// [end config]

// Section 4.3.4 (Inputs) of GLSL 1.50 spec states:
//	"Fragment inputs can only be signed and
//	unsigned integers and integer vectors, float, floating-point 
//	vectors, matrices, or arrays or structures of
//	these."
#version 150

flat in struct A {
	int x;
	float y;
	vec3 z;
	uvec4 w;
	mat3 v;
} a;

void main () {
	gl_FragColor = vec4(a.x + a.y + a.z.x + a.w.x + a.v[0].x);
}
