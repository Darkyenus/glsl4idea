// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS */
void main()
{
    mat4  result;
    result = gl_TextureMatrix[0];
    result = gl_TextureMatrixInverse[0];
    result = gl_TextureMatrixTranspose[0];
    result = gl_TextureMatrixInverseTranspose[0];
}
