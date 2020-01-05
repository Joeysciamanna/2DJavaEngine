#version 330



out vec4 fragColor;

in vec2 fragTexCoord;

uniform vec4 color;
uniform int textureEnabled;
uniform sampler2D texture;

void main(){

    fragColor = color;
	if (textureEnabled == 1){
		fragColor += texture(texture, fragTexCoord);
	}
	if(fragColor.a == 0){
		discard;
	}
}