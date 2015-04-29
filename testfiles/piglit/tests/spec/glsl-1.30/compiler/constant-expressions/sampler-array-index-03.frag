// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that sampler arrays cannot be indexed with non-constant expressions.
//
// From page 23 (29 of PDF) of GLSL 1.30 spec:
//     Samplers aggregated into arrays within a shader (using square brackets
//     [ ]) can only be indexed with integral constant expressions
//
// See also
// spec/glsl-1.10/compiler/constant-expressions/sampler-array-index-01.frag.
//
// This test verifies that the compiler will reject something sensible.  This is
// to the letter of the spec.  It is likely that many compilers will accept this
// case.

#version 130

varying vec2 tc[gl_MaxTextureImageUnits];
uniform sampler2D s[gl_MaxTextureImageUnits];

void main() {
	vec4 c = vec4(0.0);

	for (int i = 0; i < gl_MaxTextureImageUnits; i++)
		c += texture(s[i], tc[i]);

	gl_FragColor = c;
}
