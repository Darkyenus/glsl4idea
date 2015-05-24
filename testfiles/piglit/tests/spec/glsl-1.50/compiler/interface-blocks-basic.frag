// [config]
// expect_result: pass
// glsl_version: 1.50
// check_link: true
// [end config]

#version 150

in block_without_instance_name {
    vec4 iface_var;
};

uniform block_with_instance_name {
    vec4 iface_var;
} instance_name_for_iface;

in in_block {
    smooth in vec4 in_inside_in_block;
} in_block_instance;

void main()
{
    vec4 a = iface_var;
    a = instance_name_for_iface.iface_var;
    a = in_block_instance.in_inside_in_block;
}

