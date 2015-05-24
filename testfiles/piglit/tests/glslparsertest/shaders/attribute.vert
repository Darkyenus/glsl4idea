// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    attribute float foo;  // attributes can be declared at a global scope only
}
