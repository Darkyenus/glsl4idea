// [config]
// expect_result: pass
// glsl_version: 1.00
// [end config]
//
// Type int can have default precision.
//
// From section 4.5.3 of the GLSL 1.00 spec:
//     The precision statement
//         precision precision-qualifier type;
//     can be used to establish a default precision qualifier. The type field
//     can be either int or float,


#version 100

precision mediump int;

void f() { }
