// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

#extension all : require // cannot use require or enable with all
#extension all : enable // cannot use require or enable with all

void main()
{
}
