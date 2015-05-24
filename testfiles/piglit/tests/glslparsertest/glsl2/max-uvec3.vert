// [config]
// expect_result: pass
// glsl_version: 1.30
// [end config]
#version 130

void main()
{
    uvec3 x = uvec3(1,2,3);
    uvec3 y = uvec3(1,2,3);
    uvec3 z = max(x,y);

    gl_Position = vec4(1,0,0,1);
}
