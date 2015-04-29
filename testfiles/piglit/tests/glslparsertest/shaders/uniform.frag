// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    uniform float foo;  // uniforms can only be declared at a global scope
}
