// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL
 * Without GLSL 1.40 or GL_ARB_shader_texture_lod, the "Lod" versions of the
 * texture lookup functions are not available in fragment shaders.
 */
uniform sampler1D s;
varying vec4 coord;
varying float lod;

void main()
{
  gl_FragColor = texture1DProjLod(s, coord, lod);
}
