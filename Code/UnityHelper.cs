using UnityEngine;

public class UnityHelper
{
    public static Vector3 Vector3(RVO.Vector2 v)
    {
        return new Vector3(v.x(), 0, v.y());
    }
}