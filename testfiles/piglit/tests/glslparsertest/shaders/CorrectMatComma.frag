// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

void main()
{
    const mat4 tempMat = mat4(1,0,0,1,1,0,1,0,0,0,0,0,0,0,0,0);
    const vec4 v1 = tempMat[true ? 0 : 1];  // 1,0,0,1
    const vec4 v2 = tempMat[0,1]; // 1,0,1,0
    const vec4 v3 = tempMat[int(v2.x)]; // tempMat[1]; = 1,0,1,0
    const vec4 v4 = tempMat[1+2]; // 0,0,0,0
    const vec4 v5 = tempMat[int(!false)]; // 1,0,1,0
    const float f = v1[1,0]; // v[0] = 1
    const float f2 = v1[3-2]; // v[1] = 0

    vec4 v7 = tempMat[2,1]; // 1,0,1,0
    mat4 m = mat4(1,0,0,1,1,0,1,0,0,0,0,0,0,0,0,0);
    vec4 v9 = m[1,2]; 
     

    gl_FragColor = v1 + v2 + v3 + v4 + v5 + vec4(f) + vec4(f2) + v7 + v9;
}
