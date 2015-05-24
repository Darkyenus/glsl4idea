// [config]
// expect_result: fail
// glsl_version: 1.20
//
// [end config]

/* FAIL */
#version 120
void main() {
    float foo(float x) {
        return x + 0.5;
    }
    gl_FragColor = vec4(0.0, foo(0.5), 0.0, 1.0);
}
