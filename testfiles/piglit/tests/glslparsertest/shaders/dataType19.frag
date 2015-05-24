// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

uniform sampler1D s;
void main()
{
    int i = int(s); // conversion not allowed
}
