// [config]
// expect_result: pass
// glsl_version: 1.30
// require_extensions: GL_ARB_texture_query_levels GL_ARB_texture_cube_map_array
// [end config]

#version 130
#extension GL_ARB_texture_query_levels: require
#extension GL_ARB_texture_cube_map_array: require

uniform sampler1D s1D;
uniform sampler2D s2D;
uniform sampler3D s3D;
uniform samplerCube sCube;
uniform sampler1DArray s1DArray;
uniform sampler2DArray s2DArray;
uniform samplerCubeArray sCubeArray;

uniform sampler1DShadow s1DShadow;
uniform sampler2DShadow s2DShadow;
uniform samplerCubeShadow sCubeShadow;
uniform sampler1DArrayShadow s1DArrayShadow;
uniform sampler2DArrayShadow s2DArrayShadow;
uniform samplerCubeArrayShadow sCubeArrayShadow;

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

    result += textureQueryLevels(s1DShadow);
    result += textureQueryLevels(s2DShadow);
    result += textureQueryLevels(sCubeShadow);
    result += textureQueryLevels(s1DArrayShadow);
    result += textureQueryLevels(s2DArrayShadow);
    result += textureQueryLevels(sCubeArrayShadow);

    gl_FragColor = vec4(result);
}
