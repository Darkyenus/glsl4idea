// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

// test that the precise qualifier must appear first with multiple qualifications,
// without ARB_shading_language_420pack.

// the spec says the order of qualification must be:
//	precise-qualifier invariant-qualifier interpolation-qualifier ...

#version 150
#extension GL_ARB_gpu_shader5: require

invariant precise vec4 x;
