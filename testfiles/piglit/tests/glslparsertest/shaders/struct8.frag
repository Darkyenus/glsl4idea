// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

struct s {
    int i;
} s1;

struct ss {
    int i;
} s2;

void main()
{
    s1 = s2;  // two different structures cannot be assigned to each other
}
