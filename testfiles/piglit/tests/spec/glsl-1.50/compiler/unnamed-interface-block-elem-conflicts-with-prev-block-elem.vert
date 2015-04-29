// [config]
// expect_result: fail
// glsl_version: 1.50
// check_link: true
// [end config]
//
// From section 4.3.7 (Interface Blocks) of the GLSL 1.50 spec:
//
//     If an instance name (instance-name) is not used, the names
//     declared inside the block are scoped at the global level and
//     accessed as if they were declared outside the block.
//
// Consequently, inside an interface block lacking an instance-name,
// it is illegal to use a name that was previously declared inside a
// different interface block lacking an instance-name (just as it
// would be illegal to redeclare the name outside an interface block).

#version 150

out block1 {
    vec4 a;
};

out block2 {
    vec4 a;
};

void main()
{
}
