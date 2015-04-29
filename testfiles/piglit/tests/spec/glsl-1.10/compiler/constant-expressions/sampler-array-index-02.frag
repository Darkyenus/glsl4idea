// [config]
// expect_result: pass
// glsl_version: 1.10
// [end config]
//
// Check that sampler arrays can be indexed with non-constant expressions.
//
// The GLSL 1.10 and 1.20 specs place no restriction on how arrays of samplers
// may be indexed.  The restriction was added in GLSL 1.30.
//
// From page 106 (112 of PDF) of GLSL ES 1.00 spec:
//
//     GLSL ES 1.00 supports both arrays of samplers and arrays of structures
//     which contain samplers. In both these cases, for ES 2.0, support for
//     indexing with a constant integral expression is mandated but support
//     for indexing with other values is not mandated.
//
// This test should work on GLSL ES 1.00, but it is not required to.

#ifdef GL_ES
precision mediump float;
#endif

varying vec2 tc[gl_MaxTextureImageUnits];
uniform sampler2D s[gl_MaxTextureImageUnits];

void main() {
	vec4 c = vec4(0.0);

	for (int i = 0; i < gl_MaxTextureImageUnits; i++)
		c += texture2D(s[i], tc[i]);

	gl_FragColor = c;
}
