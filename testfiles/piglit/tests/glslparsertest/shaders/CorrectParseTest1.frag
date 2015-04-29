// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

struct s {
    vec4 v;
} s2;

void main()
{
    s s1 = s(vec4(ivec4(4.0, vec2(5,6), 7.0)));
    vec4 v = vec4(2,ivec2(3.0, 4.0), 5.0);
    vec4 v4 = vec4(ivec4(8.0));
    
    gl_FragColor = v4 + v + s1.v;
}
