#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 color;

out vec4 outColor;

uniform mat4 proj_mat;
uniform mat4 ml_vw_mat;

void main()
{
    gl_Position = proj_mat * ml_vw_mat * vec4(position, 1);
    outColor = vec4(color, 0);
}