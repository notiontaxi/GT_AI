using UnityEngine;
using System.Collections;

public class GridDrawer : MonoBehaviour
{
    public Material Material;

    Mesh _mesh;
    Vector3[] _vertices = new Vector3[4];

    // Use this for initialization
    void Start()
    {
        _mesh = new Mesh();

        _vertices[0] = new Vector3(1, 1, 0);
        _vertices[1] = new Vector3(1, -1, 0);
        _vertices[2] = new Vector3(-1, -1, 0);
        _vertices[3] = new Vector3(-1, 1, 0);

        _mesh.vertices = _vertices;
        _mesh.triangles = new int[] { 0, 1, 2, 0, 2, 3 };
        _mesh.RecalculateNormals();
        _mesh.RecalculateBounds();
        _mesh.Optimize();
    }

    // Update is called once per frame
    void Update()
    {
    }

    void OnPostRender()
    {
        Material.SetPass(0);
        Graphics.DrawMeshNow(_mesh, Vector3.zero, Quaternion.identity);
    }
}
