// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Type bool cannot have default precision.
//
// From section 4.5.3 of the GLSL 1.30 spec:
//     The precision statement
//         precision precision-qualifier type;
//     can be used to establish a default precision qualifier. The type field
//     can be either int or float. [...] Any other types or qualifiers will
//     result in an error.


#version 130

precision highp bool;

float f() {
	return 0.0;
}
