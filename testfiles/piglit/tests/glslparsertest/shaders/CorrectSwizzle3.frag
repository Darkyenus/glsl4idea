// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

void main()
{
    vec4 v = vec4(5,6,7,8);
    // value changes for lhs
    // 8765, 6758, 857, 75 i.e. replace v.zx
    // value changes for rhs
    // 8765, 6758, 86 i.e replace with v.wy
    // replace v.z with v.w
    // replace v.x with v.y
    // add 1.000000 to v.w and v.y
    v.wzyx.zywx.wzy.zy = (v.wzyx.zywx.wx)++;
    gl_FragColor = vec4(v);  // 6,7,8,9
}
