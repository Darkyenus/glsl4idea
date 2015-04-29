// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

#version 110
#extension extensionfoo : enable  // warning extension not supported 
#extension extensionfoo : disable  // warning extension not supported 
#extension extensionfoo : warn  // warning extension not supported 

#extension all : disable // no error in the program
#extension all : warn // no error in the program

#extension extensionfoo : enable  // warning extension not supported 
#extension extensionfoo : disable  // warning extension not supported 
#extension extensionfoo : warn  // warning extension not supported 

void main()
{
}
