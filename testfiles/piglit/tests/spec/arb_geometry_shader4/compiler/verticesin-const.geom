// [config]
// expect_result: pass
// glsl_version: 1.30
// require_extensions: GL_ARB_geometry_shader4
// [end config]

#version 130
#extension GL_ARB_geometry_shader4: enable

uniform int idx;
uniform sampler2D tex;
uniform sampler2D tex_array[7];

void main()
{
	/* All quotes are from the glsl 1.3 specification. */

	/* "Samplers aggregated into arrays within a shader [...]
	 * can only be indexed with integral constant expressions"
	 */
	texture(tex_array[gl_VerticesIn], vec2(0, 0));

	/* "When an array size is specified in a declaration,
	 * it must be an integral constant expression"
	 */
	float array[gl_VerticesIn];
	array[idx];

	/* "Initializers for const declarations must be constant expression" */
	const int var = gl_VerticesIn;

	/* "The offset value must be a constant expression." */
	textureOffset(tex, vec2(0, 0), ivec2(gl_VerticesIn));
}

