// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

vec2 func()
{
    vec2 v;
    return v;
}

void main()
{
    const vec3 v = vec3(1.0, func()); // user defined functions do not return const value
    gl_FragColor = vec4(v, v);
}
