using UnityEngine;
using RVO;
using System.Collections.Generic;
using System;

class GizmoDrawer : MonoBehaviour
{
    static private GizmoDrawer _instance;
    static public GizmoDrawer Instance { get { return _instance; } }

    private GizmoDrawer()
    {
        _instance = this;
    }

    private struct Segment
    {
        internal Vector3 _a;
        internal Vector3 _b;
        internal Color _color;
    }

    private IList<Segment> _segments = new List<Segment>();

    public void Clear()
    {
        _segments.Clear();
    }

    public void AddSegment(Vector3 a, Vector3 b, Color color)
    {
        Segment s = new Segment();
        s._a = a;
        s._b = b;
        s._color = color;
        _segments.Add(s);
    }

    void OnPostRender()
    {
        AssetHolder.Instance.GizmoMaterial.SetPass(0);
        GL.Begin(GL.LINES);
        foreach (Segment s in _segments)
        {
            GL.Color(s._color);
            GL.Vertex3(s._a.x, s._a.y, s._a.z);
            GL.Vertex3(s._b.x, s._b.y, s._b.z);
        }
        GL.End();
    }
}
