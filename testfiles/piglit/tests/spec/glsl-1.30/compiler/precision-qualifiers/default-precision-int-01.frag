// [config]
// expect_result: pass
// glsl_version: 1.30
// [end config]
//
// Type int can have default precision.
//
// From section 4.5.3 of the GLSL 1.30 spec:
//     The precision statement
//         precision precision-qualifier type;
//     can be used to establish a default precision qualifier. The type field
//     can be either int or float,


#version 130

precision mediump int;

float f() {
	return 0.0;
}
