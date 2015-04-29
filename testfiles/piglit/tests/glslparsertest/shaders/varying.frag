// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    varying float foo; // varyings can only be declared at a global scope
}
