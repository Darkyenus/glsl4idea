// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

#extension extensionfoo : enable  // warning extension not supported 
#extension extensionfoo : disable  // warning extension not supported 
#extension extensionfoo : warn  // warning extension not supported 

void main()
{
}
