// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL
 * Without GLSL 1.40 or GL_ARB_shader_texture_lod, the "Lod" versions of the
 * texture lookup functions are not available in fragment shaders.
 */
uniform sampler1DShadow s;
varying vec3 coord;
varying float lod;

void main()
{
  gl_FragColor = shadow1DLod(s, coord, lod);
}
