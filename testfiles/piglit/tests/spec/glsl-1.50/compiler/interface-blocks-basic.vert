// [config]
// expect_result: pass
// glsl_version: 1.50
// check_link: true
// [end config]

#version 150

uniform block_without_instance_name {
    vec4 iface_var;
};

out block_with_instance_name {
    vec4 iface_var;
} instance_name_for_iface;

out out_block {
    smooth out vec4 out_inside_out_block;
} out_block_instance;

void main()
{
    instance_name_for_iface.iface_var = iface_var;
    out_block_instance.out_inside_out_block = iface_var;
}

