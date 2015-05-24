// [config]
// expect_result: pass
// glsl_version: 1.10
// [end config]

uniform vec4 u;

void main()
{
    vec2 v = vec2(0);

    v -= u.xy;
    v.x += u.x;
    v.x += u.x;

    gl_Position = v.xxyy;
}
