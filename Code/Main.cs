//#define NO_RENDER
#define RENDER_USING_GAMEOBJECTS

using UnityEngine;
using System.Collections;
using RVO;
using System.IO;
using System.Globalization;
using System;
using System.Collections.Generic;

public class Main : MonoBehaviour
{
    private IExample _example;
    private IList<Type> _allTypes = new List<Type>();
    private Type _selectedType;

    private Camera _camera = null;
    private bool _cameraDirty = true;
    private bool _autoFocus = true;
    private bool _pause = false;
    private bool _singleStep = false;
    private GameObject _agentsRoot;

#if !NO_RENDER
#if RENDER_USING_GAMEOBJECTS
    private GameObject[] _gameObjects;
#else
    private Mesh _mesh;
    private Vector3[] _vertices;
#endif
#endif

    void Start()
    {
        _camera = Camera.main;
        _camera.gameObject.AddComponent<GizmoDrawer>();
		_camera.gameObject.transform.rotation = Quaternion.Euler(38f, 0.0f, 0.0f);
        _agentsRoot = new GameObject("Agents root");
        _agentsRoot.AddComponent<MeshFilter>();
        _agentsRoot.AddComponent<MeshRenderer>();

        foreach (Type type in GetType().Assembly.GetTypes())
        {
            if (typeof(IExample).IsAssignableFrom(type) && !type.IsInterface)
            {
                _allTypes.Add(type);
            }
        }
        _selectedType = _allTypes[_allTypes.Count - 1];

        reset();
    }

    void Update()
    {
        if (!_pause || _singleStep)
        {
            _example.setPreferredVelocities();
            Simulator.Instance.doStep();
        }

#if !NO_RENDER

        float yMin = float.PositiveInfinity;
        float yMax = float.NegativeInfinity;
#if RENDER_USING_GAMEOBJECTS
        for (int i = 0; i < _gameObjects.Length; ++i)
        {
            RVO.Vector2 p = Simulator.Instance.getAgentPosition(i);
            float radius = Simulator.Instance.getAgentRadius(i);
            _gameObjects[i].transform.position = new Vector3(p.x(), 2.4f, p.y());
            RVO.Vector2 v = Simulator.Instance.getAgentVelocity(i);
            //_gameObjects[i].transform.localRotation = Quaternion.AngleAxis((Mathf.Rad2Deg * Mathf.Atan2(v.x(), -v.y())) , Vector3.down);
			_gameObjects[i].transform.rotation = Quaternion.Euler(270.0f, (Mathf.Rad2Deg * Mathf.Atan2(v.x(), v.y())), 0.0f);
            yMin = Math.Min(yMin, p.y() );
            yMax = Math.Max(yMax, p.y() );
        }
#else
        for (int quadNum = 0; quadNum < _vertices.Length / 4; ++quadNum)
        {
            RVO.Vector2 p = Simulator.Instance.getAgentPosition(quadNum);
            RVO.Vector2 v = Simulator.Instance.getAgentVelocity(quadNum);
            float radius = Simulator.Instance.getAgentRadius(quadNum);
            setQuad(quadNum, p, v, radius);
            yMin = Math.Min(yMin, p.y() - radius);
            yMax = Math.Max(yMax, p.y() + radius);
        }
        _mesh.vertices = _vertices;
#endif

        if (_cameraDirty || _autoFocus)
        {
            _camera.transform.position = new Vector3(_camera.transform.position.x,
                50.0f,
                (.5f  * yMin / Mathf.Tan(_camera.fov * Mathf.Deg2Rad / 2)) - 60.0f);
			
            _cameraDirty = false;
        }
#endif
    }

    void OnGUI()
    {
        GUILayout.BeginVertical(GUILayout.Width(200));
        GUILayout.Label("Agents: " + Simulator.Instance.getNumAgents());
        GUILayout.BeginHorizontal();
        int numWorkers = Simulator.Instance.GetNumWorkers();
        GUILayout.Label("Workers: " + numWorkers);
        GUI.enabled = numWorkers > 1;
        if (GUILayout.Button("-")) Simulator.Instance.SetNumWorkers(numWorkers - 1);
        GUI.enabled = true;
        if (GUILayout.Button("+")) Simulator.Instance.SetNumWorkers(numWorkers + 1);
        GUILayout.EndHorizontal();

        foreach (Type type in _allTypes)
        {
            if (GUILayout.Toggle(type == _selectedType, type.Name) && type != _selectedType)
            {
                _selectedType = type;
                _pause = true;
                reset();
            }
        }
        if (GUILayout.Button(_pause ? "Play" : "Pause"))
        {
            _pause = !_pause;
        }
        GUI.enabled = _pause;
        _singleStep = GUILayout.Button("Single step");
        GUI.enabled = true;

        _autoFocus = GUILayout.Toggle(_autoFocus, "Automatic Camera");
        GUILayout.Label("Simulation time: " + Simulator.Instance.getGlobalTime().ToString());
        if (GUILayout.Button("Restart"))
        {
            reset();
        }
        GUILayout.EndVertical();
    }

    private static Color ColorFromHSV(float hue, float saturation, float value)
    {
        if (hue < 0) hue += 360;
        int hi = Convert.ToInt32(Mathf.Floor(hue / 60)) % 6;
        float f = hue / 60 - Mathf.Floor(hue / 60);

        float v = (value);
        float p = (value * (1f - saturation));
        float q = (value * (1f - f * saturation));
        float t = (value * (1f - (1f - f) * saturation));

        if (hi == 0)
            return new Color(v, t, p);
        else if (hi == 1)
            return new Color(q, v, p);
        else if (hi == 2)
            return new Color(p, v, t);
        else if (hi == 3)
            return new Color(p, q, v);
        else if (hi == 4)
            return new Color(t, p, v);
        else
            return new Color(v, p, q);
    }

    private void reset()
    {
        Simulator.Instance.Clear();
        _example = (IExample)_selectedType.GetConstructor(new Type[0]).Invoke(new object[0]);

        _example.setupScenario();
        _example.setPreferredVelocities();

#if !NO_RENDER
#if RENDER_USING_GAMEOBJECTS
        if (_gameObjects != null)
        {
            foreach (GameObject go in _gameObjects)
            {
                Destroy(go);
            }
        }
        _gameObjects = new GameObject[Simulator.Instance.getNumAgents()];

        Vector3 bounds = AssetHolder.Instance.AgentPrefab.renderer.bounds.extents;
        float extent = Math.Max(Math.Abs(bounds.x), Math.Abs(bounds.z));
        for (int i = 0; i < _gameObjects.Length; ++i)
        {
            _gameObjects[i] = GameObject.Instantiate(AssetHolder.Instance.AgentPrefab) as GameObject;
            _gameObjects[i].name = "Agent #" + i.ToString();
            _gameObjects[i].transform.parent = _agentsRoot.transform;
            float s = Simulator.Instance.getAgentRadius(i) / extent;
            _gameObjects[i].transform.localScale *= s;

            RVO.Vector2 v = Simulator.Instance.getAgentPrefVelocity(i);
            //_gameObjects[i].renderer.material.color = ColorFromHSV(Mathf.Rad2Deg * Mathf.Atan2(v.x(), -v.y()), 1, 1);
        }
#else
        _mesh = new Mesh();
        int quadCount = Simulator.Instance.getNumAgents();
        _vertices = new Vector3[4 * quadCount];
        int[] triangles = new int[6 * quadCount];
        Color[] colors = new Color[4 * quadCount];
        UnityEngine.Vector2[] uv = new UnityEngine.Vector2[_vertices.Length];

        for (int quadNum = 0; quadNum < quadCount; ++quadNum)
        {
            RVO.Vector2 p = Simulator.Instance.getAgentPosition(quadNum);
            RVO.Vector2 v = Simulator.Instance.getAgentPrefVelocity(quadNum);
            float radius = Simulator.Instance.getAgentRadius(quadNum);
            setQuad(quadNum, p, v, radius);
            Color color = ColorFromHSV(Mathf.Rad2Deg * Mathf.Atan2(v.x(), -v.y()), 1, 1);
            colors[quadNum * 4 + 0] = color;
            colors[quadNum * 4 + 1] = color;
            colors[quadNum * 4 + 2] = color;
            colors[quadNum * 4 + 3] = color;
            uv[quadNum * 4 + 0] = new UnityEngine.Vector2(0, 0);
            uv[quadNum * 4 + 1] = new UnityEngine.Vector2(0, 1);
            uv[quadNum * 4 + 2] = new UnityEngine.Vector2(1, 1);
            uv[quadNum * 4 + 3] = new UnityEngine.Vector2(1, 0);
            triangles[quadNum * 6 + 0] = quadNum * 4 + 0;
            triangles[quadNum * 6 + 1] = quadNum * 4 + 1;
            triangles[quadNum * 6 + 2] = quadNum * 4 + 2;
            triangles[quadNum * 6 + 3] = quadNum * 4 + 0;
            triangles[quadNum * 6 + 4] = quadNum * 4 + 2;
            triangles[quadNum * 6 + 5] = quadNum * 4 + 3;
        }
        _mesh.vertices = _vertices;
        _mesh.colors = colors;
        _mesh.triangles = triangles;
        _mesh.uv = uv;
        _mesh.RecalculateBounds();
        _mesh.RecalculateNormals();
        _mesh.Optimize();

        _agentsRoot.GetComponent<MeshFilter>().mesh = _mesh;
        _agentsRoot.GetComponent<MeshRenderer>().material = AssetHolder.Instance.AgentMaterial;
#endif
#endif
        GizmoDrawer.Instance.Clear();

        IList<int> toVisit = new List<int>();
        for (int i = 0; i < Simulator.Instance.getNumObstacleVertices(); ++i)
        {
            toVisit.Add(i);
        }
        while (toVisit.Count != 0)
        {
            int i0 = toVisit[0];
            int next = i0;
            do
            {
                toVisit.Remove(next);
                next = Simulator.Instance.getNextObstacleVertexNo(next);
                GizmoDrawer.Instance.AddSegment(
                    UnityHelper.Vector3(Simulator.Instance.getObstacleVertex(Simulator.Instance.getPrevObstacleVertexNo(next))),
                    UnityHelper.Vector3(Simulator.Instance.getObstacleVertex(next)),
                    Color.white);
            } while (next != i0);
        }
        _cameraDirty = true;
    }
#if !NO_RENDER
#if !RENDER_USING_GAMEOBJECTS
    const float sqrt2 = 1.4142135623730950488016887242097f;

    private void setQuad(int quadNum, RVO.Vector2 center, RVO.Vector2 velocity, float size)
    {
        size *= sqrt2;
        float a = Mathf.Atan2(velocity.y(), velocity.x()) - Mathf.PI / 4;
        float ca = size * Mathf.Cos(a);
        float sa = size * Mathf.Sin(a);
        _vertices[quadNum * 4 + 0] = new Vector3(center.x() - ca, 0, center.y() - sa);
        _vertices[quadNum * 4 + 1] = new Vector3(center.x() - sa, 0, center.y() + ca);
        _vertices[quadNum * 4 + 2] = new Vector3(center.x() + ca, 0, center.y() + sa);
        _vertices[quadNum * 4 + 3] = new Vector3(center.x() + sa, 0, center.y() - ca);
    }
#endif
#endif
}
