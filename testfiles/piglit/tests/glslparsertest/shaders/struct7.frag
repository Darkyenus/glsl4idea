// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

void main()
{
    struct s {
    } s1;  // structures have to be declared with atleast one member
}
