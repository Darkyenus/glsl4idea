// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    const vec4 v = vec4(normalize(vec4(1))); // builtIn functions do not return const
    const vec4 v1 = vec4(clamp(1.0, .20, 3.0)); // builtIn functions do not return const
    float f = 1.0;
    const vec4 v2 = vec4(float(vec4(1,2,3,f))); // f is not constant

    gl_FragColor = v + v1 + v2;
}
