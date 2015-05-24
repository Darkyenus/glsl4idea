// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

void main()
{
    float f, f1, f2;
    f = f1 = f2;
    f += f1 += f2;
    
    vec4 v, v1, v2;
    v = v1 = v2;
    v += v1 += v2;
    v.wx = v1.zx = v2.yx;
    v.wx += v1.zx += v2.yx;

    mat4  m, m1, m2;
    m = m1 = m2;
    m += m1 += m2;
    m[3].wx = m1[2].zx = m2[1].yx;
    m[3].wx += m1[2].zx += m2[1].yx;

    mat4  am[4], am1[4], am2[4];
    am[3] = am1[2] = am2[1];
    am[3] += am1[2] += am2[1];
    am[3][3].wx = am1[2][2].zx = am2[1][1].yx;
    am[3][3].wx += am1[2][2].zx += am2[1][1].yx;
    am[3][3].wx += am1[2][2].zx += ++(am2[1][1].yx);
    am[3][3].wx += am1[2][2].zx += (am2[1][1].yx)++;

    gl_FragColor = vec4(am[3][3].z, m[3].w, v.w, f);
}
