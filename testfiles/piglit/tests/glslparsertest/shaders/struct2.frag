// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

struct s {
    int i = 1.0;  // struct members cannot be initialized at the time of structure declaration
} s1;

void main()
{
}
