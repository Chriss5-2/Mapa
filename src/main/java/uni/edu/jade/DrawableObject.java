package uni.edu.jade;

public interface DrawableObject {
    public void Draw(Shader shader, int GL_MODE, int count);
    public void CreateVAO(float[] vertexArray, int[] elementArray);
}
