// [config]
// expect_result: pass
// glsl_version: 1.30
// require_extensions: GL_ARB_texture_query_levels GL_ARB_texture_cube_map_array
// [end config]

#version 130
#extension GL_ARB_texture_query_levels: require
#extension GL_ARB_texture_cube_map_array: require

uniform usampler1D s1D;
uniform usampler2D s2D;
uniform usampler3D s3D;
uniform usamplerCube sCube;
uniform usampler1DArray s1DArray;
uniform usampler2DArray s2DArray;
uniform usamplerCubeArray sCubeArray;

void main()
{
    int result = 0;

    result += textureQueryLevels(s1D);
    result += textureQueryLevels(s2D);
    result += textureQueryLevels(s3D);
    result += textureQueryLevels(sCube);
    result += textureQueryLevels(s1DArray);
    result += textureQueryLevels(s2DArray);
    result += textureQueryLevels(sCubeArray);

    gl_FragColor = vec4(result);
}
