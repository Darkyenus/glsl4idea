/* [config]
 * expect_result: pass
 * glsl_version: 4.20
 * require_extensions: GL_ARB_arrays_of_arrays
 * [end config]
 *
 * Test the basic types added in glsl 4.20
 */
#version 420
#extension GL_ARB_arrays_of_arrays: enable

uniform atomic_uint array01[1][1];
writeonly uniform image1D array02[1][1];
writeonly uniform iimage1D array03[1][1];
writeonly uniform uimage1D array04[1][1];
writeonly uniform image2D array05[1][1];
writeonly uniform iimage2D array06[1][1];
writeonly uniform uimage2D array07[1][1];
writeonly uniform image3D array08[1][1];
writeonly uniform iimage3D array09[1][1];
writeonly uniform uimage3D array10[1][1];
writeonly uniform image2DRect array11[1][1];
writeonly uniform iimage2DRect array12[1][1];
writeonly uniform uimage2DRect array13[1][1];
writeonly uniform imageCube array14[1][1];
writeonly uniform iimageCube array15[1][1];
writeonly uniform uimageCube array16[1][1];
writeonly uniform imageBuffer array17[1][1];
writeonly uniform iimageBuffer array18[1][1];
writeonly uniform uimageBuffer array19[1][1];
writeonly uniform image1DArray array20[1][1];
writeonly uniform iimage1DArray array21[1][1];
writeonly uniform uimage1DArray array22[1][1];
writeonly uniform image2DArray array23[1][1];
writeonly uniform iimage2DArray array24[1][1];
writeonly uniform uimage2DArray array25[1][1];
writeonly uniform imageCubeArray array26[1][1];
writeonly uniform iimageCubeArray array27[1][1];
writeonly uniform uimageCubeArray array28[1][1];
writeonly uniform image2DMS array29[1][1];
writeonly uniform iimage2DMS array30[1][1];
writeonly uniform uimage2DMS array31[1][1];
writeonly uniform image2DMSArray array32[1][1];
writeonly uniform iimage2DMSArray array33[1][1];
writeonly uniform uimage2DMSArray array34[1][1];

void main() { gl_FragColor = vec4(0.0); }
